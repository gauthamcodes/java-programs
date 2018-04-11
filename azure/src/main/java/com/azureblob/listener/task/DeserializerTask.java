package com.azureblob.listener.task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonEncoder;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azureblob.listener.config.AzureConfig;
import com.azureblob.listener.model.Record;
import com.azureblob.listener.utils.AzureUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class DeserializerTask implements Runnable {

	private final static Logger log = LoggerFactory.getLogger(DeserializerTask.class);

	private List<Record> records;
	private List<Long> durations;
	private AzureConfig config;

	private Long timestamp;

	public DeserializerTask(List<Record> records, AzureConfig config) {
		this.config = config;
		this.records = records;
		this.durations = new ArrayList<>();
		this.timestamp = System.currentTimeMillis();
	}

	@Override
	public void run() {
		log.info("Deserializer task started!");
		while (true) {
			try {
				deserialize();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void deserialize() throws InterruptedException {
		synchronized (records) {
			if (records.size() == 0) {
				records.wait();
			}
			Record record = records.remove(0);
			records.notifyAll();
			try (DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(record.getFile(),
					new GenericDatumReader<GenericRecord>());
					PrintWriter out = new PrintWriter(
							AzureUtils.createDirectory(record.getDirectory(), config.getJson())
									+ record.getFile().getName() + AzureConfig.JSON_EXT);) {
				Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
				JsonParser jp = new JsonParser();
				StringBuilder jsonString = new StringBuilder();
				jsonString.append("[");
				GenericRecord genericRecord = null;
				while (dataFileReader.hasNext()) {
					genericRecord = dataFileReader.next(genericRecord);
					jsonString.append(getJsonString(genericRecord) + ",");
				}
				jsonString.deleteCharAt(jsonString.length() - 1);
				jsonString.append("]");
				JsonElement je = jp.parse(jsonString.toString());
				out.write(gson.toJson(je));
				AzureUtils.appendLineToFile(record.toCSV(), config.getCsv());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				log.info("{} processed in {} ms", record.getFile().getName(),
						System.currentTimeMillis() - record.getTimestamp());
				if (AzureUtils.deleteFile(record.getFile().toPath())) {
					durations.add(System.currentTimeMillis() - timestamp);
					timestamp = System.currentTimeMillis();
					if (durations.size() == config.getBufferSize()) {
						log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						Double totalTime = Double.valueOf(durations.stream().mapToLong(i -> i).sum());
						log.info("Processing speed {} rpm (Records per minute)",
								Math.round(60 / (totalTime / (1000 * config.getBufferSize()))));
						log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						durations.removeAll(durations);
					}
				}
				if (config.getIsComplete() && records.size() == 0) {
					System.exit(0);
				}
			}
		}
	}

	private static String getJsonString(GenericRecord record) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		JsonEncoder encoder = EncoderFactory.get().jsonEncoder(record.getSchema(), os);
		DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>();
		if (record instanceof SpecificRecord) {
			writer = new SpecificDatumWriter<GenericRecord>();
		}
		writer.setSchema(record.getSchema());
		writer.write(record, encoder);
		encoder.flush();
		String jsonString = new String(os.toByteArray(), Charset.forName("UTF-8"));
		os.close();
		return jsonString;
	}

}
