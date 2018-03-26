package com.azureblob.listener.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azureblob.listener.config.AzureConfig;
import com.azureblob.listener.model.Record;
import com.azureblob.listener.utils.AzureUtils;
import com.google.gson.Gson;

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
					new GenericDatumReader<GenericRecord>())) {
				List<GenericRecord> genericRecords = new ArrayList<>();
				GenericRecord genericRecord = null;
				while (dataFileReader.hasNext()) {
					genericRecord = dataFileReader.next(genericRecord);
					genericRecords.add(genericRecord);
				}
				record.setSize(genericRecords.size());
				String json = config.getJson() + record.getFile().getName() + AzureConfig.JSON_EXT;
				AzureUtils.writeJsonToFile(new Gson().toJson(genericRecords), json);
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

}
