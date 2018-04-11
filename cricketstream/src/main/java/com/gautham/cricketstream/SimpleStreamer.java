package com.gautham.cricketstream;

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
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.state.KeyValueStore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gautham.cricketstream.config.AppConfig;
import com.gautham.cricketstream.model.Innings;
import com.gautham.cricketstream.model.Summary;
import com.gautham.cricketstream.serde.InningsDeserializer;
import com.gautham.cricketstream.serde.InningsSerializer;
import com.gautham.cricketstream.serde.SummaryDeserializer;
import com.gautham.cricketstream.serde.SummarySerializer;

public class SimpleStreamer {

	public static void main(String[] args) {
		SimpleStreamer streamer = new SimpleStreamer();
		streamer.stream();
	}

	public void stream() {
		ObjectMapper mapper = new ObjectMapper();

		final Serializer<JsonNode> jsonSerializer = new JsonSerializer();
		final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
		final Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonSerializer, jsonDeserializer);
		final Consumed<String, JsonNode> consumed = Consumed.with(Serdes.String(), jsonSerde);
		final Produced<String, JsonNode> produced = Produced.with(Serdes.String(), jsonSerde);

		final Serializer<Innings> inningsSerializer = new InningsSerializer();
		final Deserializer<Innings> inningsDeserializer = new InningsDeserializer();
		final Serde<Innings> inningsSerde = Serdes.serdeFrom(inningsSerializer, inningsDeserializer);
		final Serialized<String, Innings> inningsSerialized = Serialized.with(Serdes.String(), inningsSerde);

		final Serializer<Summary> summarySerializer = new SummarySerializer();
		final Deserializer<Summary> summaryDeserializer = new SummaryDeserializer();
		final Serde<Summary> summarySerde = Serdes.serdeFrom(summarySerializer, summaryDeserializer);
		final Materialized<String, Summary, KeyValueStore<org.apache.kafka.common.utils.Bytes, byte[]>> materialized = Materialized
				.with(Serdes.String(), summarySerde);

		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, AppConfig.applicationId);
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "10.200.128.169:9092");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
		config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);

		StreamsBuilder builder = new StreamsBuilder();
		KStream<String, Innings> scores = builder.stream(AppConfig.producerTopic, consumed).filter((k, v) -> v != null)
				.map((k, v) -> {
					try {
						Innings innings = mapper.treeToValue(v, Innings.class);
						System.out.println(innings);
						return new KeyValue<>(innings.getBatsman().getName(), innings);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
					return null;
				});
		KTable<String, Summary> analysis = scores.groupByKey(inningsSerialized).aggregate(() -> new Summary(),
				(key, incoming, initial) -> calculate(incoming, initial), materialized);
		analysis.toStream().map((k, v) -> new KeyValue<>(k, mapper.valueToTree(v))).to(AppConfig.streamTopic, produced);
		Topology topology = builder.build();
		KafkaStreams streams = new KafkaStreams(topology, config);
		streams.start();
	}

	private Summary calculate(Innings incoming, Summary initial) {
		Summary consolidated = new Summary();
		consolidated.setTotalInnings(initial.getTotalInnings() + 1);
		consolidated.setFours(initial.getFours() + incoming.getNoOfFours());
		consolidated.setSixes(initial.getSixes() + incoming.getNoOfSixes());
		consolidated.setTotalRuns(initial.getTotalRuns() + incoming.getRunScored());
		consolidated.setBallsFaced(initial.getBallsFaced() + incoming.getBallsFaced());
		if (initial.getHighScore() < incoming.getRunScored())
			consolidated.setHighScore(incoming.getRunScored());
		else
			consolidated.setHighScore(initial.getHighScore());

		if (incoming.getNotOut())
			consolidated.setNotOuts(initial.getNotOuts() + 1);
		else
			consolidated.setNotOuts(initial.getNotOuts());

		if (consolidated.getTotalInnings() - consolidated.getNotOuts() != 0)
			consolidated.setAverage(consolidated.getTotalRuns().doubleValue()
					/ (consolidated.getTotalInnings() - consolidated.getNotOuts()));
		
		consolidated.setStrikeRate((100 * consolidated.getTotalRuns().doubleValue()) / consolidated.getBallsFaced());
		consolidated.setCenturies(initial.getCenturies());
		consolidated.setHalfCenturies(initial.getHalfCenturies());
		if (incoming.getRunScored() >= 50 && incoming.getRunScored() < 100) {
			consolidated.setHalfCenturies(initial.getHalfCenturies() + 1);
		} else if (incoming.getRunScored() >= 100) {
			consolidated.setCenturies(initial.getCenturies() + 1);
		}
		return consolidated;
	}
}
