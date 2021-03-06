package com.gautham.cricketstream.serde;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gautham.cricketstream.model.Innings;

public class InningsSerializer implements Serializer<Innings> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public byte[] serialize(String topic, Innings innings) {
		if (innings == null) {
			return null;
		}
		try{
			return objectMapper.writeValueAsBytes(innings);
		} catch (IOException | RuntimeException e) {
			throw new SerializationException("Error serializing value", e);
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
