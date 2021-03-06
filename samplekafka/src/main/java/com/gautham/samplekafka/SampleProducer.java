package com.gautham.samplekafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SampleProducer {
	public void produce() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "10.200.128.58:9092");
		// props.put("acks", "all");
		//props.put("retries", 5);
		// props.put("batch.size", 16384);
		// props.put("linger.ms", 1);
		// props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, String> producer = new KafkaProducer<>(props);

		for (int i = 10; i < 20; i++)
			producer.send(new ProducerRecord<String, String>("gautham-topic", "key" + i, "value" + i));

		producer.close();
		
		System.out.println("Sample producer completed!");
	}
}
