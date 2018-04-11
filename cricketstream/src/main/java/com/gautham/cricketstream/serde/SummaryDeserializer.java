package com.gautham.cricketstream.serde;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gautham.cricketstream.model.Summary;

public class SummaryDeserializer implements Deserializer<Summary> {
	
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Summary deserialize(String topic, byte[] data) {
		if (data == null)
			return null;
		try {
			return objectMapper.readValue(data, Summary.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
