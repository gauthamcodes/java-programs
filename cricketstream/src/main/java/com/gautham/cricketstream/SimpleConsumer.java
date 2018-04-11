package com.gautham.cricketstream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gautham.cricketstream.config.AppConfig;
import com.gautham.cricketstream.constant.Batsman;
import com.gautham.cricketstream.model.Summary;

public class SimpleConsumer {

	private final Logger logger = LoggerFactory.getLogger(SimpleConsumer.class);

	private Integer offset;
	private Map<String, Summary> summary;

	public void consume() {
		initialize();

		ObjectMapper mapper = new ObjectMapper();
		Properties props = new Properties();
		props.put("bootstrap.servers", "10.200.128.169:9092");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.connect.json.JsonDeserializer");
		props.put("group.id", AppConfig.groupId);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
		Consumer<String, JsonNode> consumer = new KafkaConsumer(props);
		consumer.subscribe(Arrays.asList(AppConfig.streamTopic));
		while (true) {
			ConsumerRecords<String, JsonNode> records = consumer.poll(100);
			for (ConsumerRecord<String, JsonNode> record : records) {
				try {
					summary.put(record.key(), mapper.treeToValue(record.value(), Summary.class));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				offset++;

				if (offset % 5 == 0)
					print();
			}
		}
	}

	private void initialize() {
		offset = 0;
		summary = new HashMap<>();
		for (Batsman batsman : Batsman.values()) {
			summary.put(batsman.getName(), new Summary());
		}
	}

	private void print() {
		logger.info(
				"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info(
				"Player Name            Total innings    Not outs     TotalRuns     4's     6's      High Score     Average     Strike Rate     50's     100's");
		logger.info(
				"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for (String player : summary.keySet()) {
			Summary sum = summary.get(player);
			logger.info("{}{}{}{}{}{}{}{}{}{}{}", toString(player, 23), toString(sum.getTotalInnings().toString(), 17),
					toString(sum.getNotOuts().toString(), 13), toString(sum.getTotalRuns().toString(), 14),
					toString(sum.getFours().toString(), 8), toString(sum.getSixes().toString(), 9),
					toString(sum.getHighScore().toString(), 15), toString(String.format("%.2f", sum.getAverage()), 12),
					toString(String.format("%.2f", sum.getStrikeRate()), 16),
					toString(sum.getHalfCenturies().toString(), 9), toString(sum.getCenturies().toString(), 10));
		}
		logger.info(
				"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for (int i = 0; i < 25; i++)
			System.out.println(" ");
	}

	private Object toString(String str, int i) {
		char[] charArray = new char[i - str.length()];
		Arrays.fill(charArray, ' ');
		String strr = new String(charArray);
		return str + strr;
	}
}
