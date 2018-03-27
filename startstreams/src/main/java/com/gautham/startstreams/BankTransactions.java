package com.gautham.startstreams;

import java.time.Instant;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BankTransactions {
	public void stream() {
		Properties config = new Properties();

		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "score-card-application");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "10.200.128.58:9092");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		// we disable the cache to demonstrate all the "steps" involved in the
		// transformation - not recommended in prod
		config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");

		// Exactly once processing!!
		config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);

		// json Serde
		final Serializer<JsonNode> jsonSerializer = new JsonSerializer();
		final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
		final Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonSerializer, jsonDeserializer);

		KStreamBuilder builder = new KStreamBuilder();

		KStream<String, JsonNode> innings = builder.stream(Serdes.String(), jsonSerde, "sample-innings-3");

		// create the initial json object for balances
		ObjectNode player = JsonNodeFactory.instance.objectNode();
		player.put("Total runs", 0);
		player.put("Balls Faced", 0);
		player.put("Time", Instant.ofEpochMilli(0L).toString());

		KTable<String, JsonNode> bankBalance = innings.groupByKey(Serdes.String(), jsonSerde).aggregate(
				() -> player, (key, transaction, balance) -> newBalance(transaction, balance), jsonSerde,
				"bank-balance-agg");

		bankBalance.to(Serdes.String(), jsonSerde, "bank-balance-exactly-once");

		KafkaStreams streams = new KafkaStreams(builder, config);
		streams.cleanUp();
		streams.start();

		// print the topology
		System.out.println(streams.toString());

		// shutdown hook to correctly close the streams application
		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
	}
	
	private static JsonNode newBalance(JsonNode transaction, JsonNode balance) {
        // create a new balance json object
        ObjectNode newBalance = JsonNodeFactory.instance.objectNode();
        newBalance.put("count", balance.get("count").asInt() + 1);
        newBalance.put("balance", balance.get("balance").asInt() + transaction.get("amount").asInt());

        Long balanceEpoch = Instant.parse(balance.get("time").asText()).toEpochMilli();
        Long transactionEpoch = Instant.parse(transaction.get("time").asText()).toEpochMilli();
        Instant newBalanceInstant = Instant.ofEpochMilli(Math.max(balanceEpoch, transactionEpoch));
        newBalance.put("time", newBalanceInstant.toString());
        return newBalance;
    }
}
