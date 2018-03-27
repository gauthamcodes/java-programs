package com.gautham.avroconsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

public class AvroConsumer {
	private static final Logger log = LoggerFactory.getLogger(AvroConsumer.class);
	private static Properties kafkaProps = new Properties();
	static {
		kafkaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		kafkaProps.put("bootstrap.servers", "10.200.128.169:9092");
		kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
		kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, "AvroGenericConsumer-GroupOne");
		kafkaProps.put("schema.registry.url", "http://10.200.128.169:8081");
	}

	public static void infiniteConsumer() throws IOException, InterruptedException {
		try (KafkaConsumer kafkaConsumer = new KafkaConsumer<>(kafkaProps)) {
			kafkaConsumer.subscribe(Arrays.asList("mondaymotivation"));
			while (true) {
				ConsumerRecords<String, GenericRecord> records = kafkaConsumer.poll(100);
				for(ConsumerRecord<String, GenericRecord> record:records) {
					GenericRecord genericRecord = record.value();
					log.info(genericRecord.get("user_name") + " @"+ genericRecord.get("user_screen_name"));
					log.info(genericRecord.get("text").toString());
					log.info(genericRecord.get("created_at") + " - " + genericRecord.get("user_location"));
					log.info("...........................................................................");
				}
				Thread.sleep(5000);
			}
		}
	}
}
