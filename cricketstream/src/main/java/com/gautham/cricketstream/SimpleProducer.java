package com.gautham.cricketstream;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gautham.cricketstream.config.AppConfig;
import com.gautham.cricketstream.model.Innings;

public class SimpleProducer {
	@SuppressWarnings("unchecked")
	public void produce() throws InterruptedException {
		Properties props = new Properties();
		props.put("bootstrap.servers", "10.200.128.169:9092");
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.connect.json.JsonSerializer");
		@SuppressWarnings("rawtypes")
		Producer producer = new KafkaProducer(props);
		ObjectMapper objectMapper = new ObjectMapper();
		for (int i = 0; i < 2000; i++) {
			Innings innings = new Simulator().getInnings();
			System.out.println(innings);
			JsonNode jsonNode = objectMapper.valueToTree(innings);
			ProducerRecord<String, JsonNode> rec = new ProducerRecord<String, JsonNode>(AppConfig.producerTopic,
					jsonNode);
			producer.send(rec);
			Thread.sleep(1000);
		}
		producer.close();
	}
}
