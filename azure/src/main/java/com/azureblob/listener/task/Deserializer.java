package com.azureblob.listener.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;

import com.azureblob.listener.config.AzureConfig;
import com.azureblob.listener.model.Record;
import com.azureblob.listener.utils.AzureUtils;
import com.google.gson.Gson;

public class Deserializer implements Callable<Record> {

	private Record record;
	private AzureConfig config;

	public Deserializer(Record record, AzureConfig config) {
		this.record = record;
		this.config = config;
	}

	@Override
	public Record call() throws Exception {
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
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			AzureUtils.deleteFile(record.getFile().toPath());
		}
		return record;
	}

}
