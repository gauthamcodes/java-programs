package com.gautham.cricketstream.serde;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gautham.cricketstream.model.Innings;

public class InningsDeserializer implements Deserializer<Innings> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub

	}

	@Override
	public Innings deserialize(String topic, byte[] data) {
		if (data == null)
			return null;
		try {
			return objectMapper.readValue(data, Innings.class);
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
