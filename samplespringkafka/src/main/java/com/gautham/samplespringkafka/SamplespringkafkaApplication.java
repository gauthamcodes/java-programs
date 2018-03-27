package com.gautham.samplespringkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class SamplespringkafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamplespringkafkaApplication.class, args);
	}

	@KafkaListener(topics = "samplekafkatopic", group = "samplegroup")
	public void listen(String message) {
		System.out.println("Received Messasge in group foo: " + message);
	}
}
