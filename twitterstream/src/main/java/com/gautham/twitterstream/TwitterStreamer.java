package com.gautham.twitterstream;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import com.fasterxml.jackson.databind.JsonNode;

public class TwitterStreamer {
	public void stream() {
		final Serializer<JsonNode> jsonSerializer = new JsonSerializer();
		final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
		final Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonSerializer, jsonDeserializer);
		
		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "twitter-stream-application");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "10.200.128.169:9092");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
		config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
		
		 final Consumed<String, JsonNode> consumed = Consumed.with(Serdes.String(), jsonSerde);
		
		StreamsBuilder builder = new StreamsBuilder();
		KStream<String, JsonNode> tweets = builder.stream("mondaymotivation", consumed);
		tweets.foreach((key,value)-> System.out.println(value.get("text")));
		Topology topology = builder.build();
		KafkaStreams streams = new KafkaStreams(topology, config);
		streams.start();
	}
}
