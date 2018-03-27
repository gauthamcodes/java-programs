package com.gautham.startstreams;

import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TransactionProducer {
	private static final Integer MATCH_FREQUENCY = 10000;
	private static final Integer MATCH_MAX_SCORE = 100;
	private static final Integer MINIMUM_BALLS_FACED = 20;

	public void produce() {
		Properties properties = new Properties();

		// kafka bootstrap server
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.200.128.58:9092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		// producer acks
		properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // strongest producing guarantee
		properties.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
		properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");
		// leverage idempotent producer from Kafka 0.11 !
		properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // ensure we don't push duplicates

		Producer<String, String> producer = new KafkaProducer<>(properties);

		int i = 0;
		while (true) {
			System.out.println("Producing batch: " + i);
			try {
				producer.send(newRandomTransaction(Players.VK.getName()));
				Thread.sleep(getRandomNum(0, MATCH_FREQUENCY));
				producer.send(newRandomTransaction(Players.ABD.getName()));
				Thread.sleep(getRandomNum(0, MATCH_FREQUENCY));
				producer.send(newRandomTransaction(Players.BEN.getName()));
				Thread.sleep(getRandomNum(0, MATCH_FREQUENCY));
				producer.send(newRandomTransaction(Players.CBR.getName()));
				Thread.sleep(getRandomNum(0, MATCH_FREQUENCY));
				producer.send(newRandomTransaction(Players.DW.getName()));
				Thread.sleep(getRandomNum(0, MATCH_FREQUENCY));
				i += 1;
			} catch (InterruptedException e) {
				break;
			}
		}
		producer.close();
	}

	public static ProducerRecord<String, String> newRandomTransaction(String name) {
		// creates an empty json {}
		ObjectNode innings = JsonNodeFactory.instance.objectNode();

		// { "amount" : 46 } (46 is a random number between 0 and 100 excluded)
		Integer runScored = getRandomNum(0, MATCH_MAX_SCORE);
		Integer ballsFaced = 0;

		if (runScored == 0)
			ballsFaced = getRandomNum(0, MINIMUM_BALLS_FACED);
		else
			ballsFaced = runScored - (getRandomNum(0, runScored)) + (getRandomNum(0, runScored));
		// Instant.now() is to get the current time using Java 8
		Instant now = Instant.now();
		innings.put(Innings.RUNS.getField(), runScored);
		innings.put(Innings.BALLS.getField(), ballsFaced);
		innings.put(Innings.TIME.getField(), now.toString());

		return new ProducerRecord<>("sample-innings-3", name, innings.toString());
	}

	private static Integer getRandomNum(Integer rangeStart, Integer rangeEnd) {
		return ThreadLocalRandom.current().nextInt(rangeStart, rangeEnd);
	}
}
