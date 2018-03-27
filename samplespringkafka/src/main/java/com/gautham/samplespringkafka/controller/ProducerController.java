package com.gautham.samplespringkafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
public class ProducerController {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Value("${kafka.sample.topic}")
	private String topicName;

	@RequestMapping(value = "/produce")
	public void produce(@RequestParam String data) {
		kafkaTemplate.send(topicName, data);
	}
}
