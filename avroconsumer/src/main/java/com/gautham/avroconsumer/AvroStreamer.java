package com.gautham.avroconsumer;

import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

public class AvroStreamer {
	@SuppressWarnings({ "deprecation"})
	public static void stream() {
		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "twitter-stream-application");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "10.200.128.169:9092");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		config.put("schema.registry.url", "http://10.200.128.169:8081");
		config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
		config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
		StreamsBuilder builder = new StreamsBuilder();
		KStream<String, GenericRecord> tweets = builder.stream("mondaymotivation");
		KStream<String, String> newTweets = tweets
				.filter((key, value) -> value.get("text").toString().contains("Raman"))
				.map((key, value) -> KeyValue.pair(value.get("user_name").toString(), value.get("text").toString()));
		newTweets.to(Serdes.String(), Serdes.String(), "mondaytest2");
		Topology topology = builder.build();
		KafkaStreams streams = new KafkaStreams(topology, config);
		streams.start();
	}

}
