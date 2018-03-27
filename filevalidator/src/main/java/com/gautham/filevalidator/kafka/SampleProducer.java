package com.gautham.filevalidator.kafka;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SampleProducer {

	public void produce(String topicName, List<String> data) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "10.200.128.58:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, String> producer = new KafkaProducer<>(props);
		
		for (int i = 0; i < data.size(); i++)
			producer.send(new ProducerRecord<String, String>(topicName, "student" + i, data.get(i)));
		
		producer.close();

		System.out.println("Sample producer completed!");
	}
}
