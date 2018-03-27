package com.gautham.twitterstream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import twitter4j.JSONException;
import twitter4j.JSONObject;

public class TwitterSourceTask extends SourceTask {
	private static final Logger log = LoggerFactory.getLogger(TwitterSourceTask.class);
	private BlockingQueue<String> queue;
	private BasicClient client;
	private String topicName;
	private Schema schema;

	@Override
	public String version() {
		return new TwitterSourceConnector().version();
	}

	@Override
	public void start(Map<String, String> props) {
		queue = new LinkedBlockingQueue<>(10000);
		String[] tags = props.get(TwitterConfig.TWITTER_TAG.getValue()).split(",");
		List<String> terms = new ArrayList<>();
		for (String tag : tags) {
			terms.add(tag);
		}
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
		endpoint.stallWarnings(false);
		endpoint.trackTerms(terms);
		endpoint.addPostParameter(Constants.LANGUAGE_PARAM, "en");

		topicName = props.get(TwitterConfig.TOPIC_CONFIG.getValue());
		schema = getSchema(props.get(TwitterConfig.SCHEMA_NAME.getValue()));
		Authentication auth = new OAuth1(props.get(TwitterConfig.CONSUMERKEY_CONFIG.getValue()),
				props.get(TwitterConfig.CONSUMERSECRET_CONFIG.getValue()),
				props.get(TwitterConfig.TOKEN_CONFIG.getValue()), props.get(TwitterConfig.SECRET_CONFIG.getValue()));

		// Create a new BasicClient. By default gzip is enabled.
		client = new ClientBuilder().name(TwitterConfig.NAME.getValue()).hosts(Constants.STREAM_HOST).endpoint(endpoint)
				.authentication(auth).processor(new StringDelimitedProcessor(queue)).build();

		// Establish a connection
		client.connect();
		log.info("started");
	}

	@Override
	public void stop() {
		client.stop();
		log.info("stop");
	}

	@Override
	public List<SourceRecord> poll() throws InterruptedException {
		log.info("poll");
		if (client.isDone()) {
			System.out.println("Client connection closed unexpectedly: " + client.getExitEvent().getMessage());
			return null; // TODO
		}
		String msg = queue.poll(1, TimeUnit.SECONDS);
		if (msg == null) {
			log.info("Did not receive a message in 1 seconds");
			return null;
		} else {
			log.info(msg);
			List<SourceRecord> records = new ArrayList<>();
			records.add(new SourceRecord(null, null, topicName, schema, getStruct(msg)));
			return records;
		}
	}

	private Schema getSchema(String schemaName) {
		SchemaBuilder builder = SchemaBuilder.struct().name(schemaName);
		builder.field(TweetObj.CREATED_AT, Schema.STRING_SCHEMA);
		builder.field(TweetObj.ID, Schema.INT64_SCHEMA);
		builder.field(TweetObj.TEXT, Schema.STRING_SCHEMA);
		builder.field(TweetObj.USERNAME, Schema.STRING_SCHEMA);
		builder.field(TweetObj.USERSCREENNAME, Schema.STRING_SCHEMA);
		builder.field(TweetObj.USERDESC, Schema.STRING_SCHEMA);
		builder.field(TweetObj.USERLOCATION, Schema.STRING_SCHEMA);
		builder.field(TweetObj.RTCOUNT, Schema.INT32_SCHEMA);
		builder.field(TweetObj.FAVCOUNT, Schema.INT32_SCHEMA);
		return builder.build();
	}

	private Struct getStruct(String msg) {
		Struct struct = new Struct(schema);
		JSONObject jsonObj;
		JSONObject userObj;
		try {
			jsonObj = new JSONObject(msg);
			struct.put(TweetObj.CREATED_AT, jsonObj.get(TweetObj.CREATED_AT));
			struct.put(TweetObj.ID, jsonObj.get(TweetObj.ID));
			struct.put(TweetObj.TEXT, jsonObj.get(TweetObj.TEXT));
			struct.put(TweetObj.RTCOUNT, jsonObj.get(TweetObj.RTCOUNT));
			struct.put(TweetObj.FAVCOUNT, jsonObj.get(TweetObj.FAVCOUNT));
			userObj = jsonObj.getJSONObject("user");
			if (!userObj.equals(JSONObject.NULL)) {
				struct.put(TweetObj.USERNAME, userObj.get("name"));
				struct.put(TweetObj.USERSCREENNAME, userObj.get("screen_name"));
				struct.put(TweetObj.USERDESC,
						(userObj.get("description").equals(JSONObject.NULL) ? "" : userObj.get("description")));
				struct.put(TweetObj.USERLOCATION,
						(userObj.get("location").equals(JSONObject.NULL) ? "" : userObj.get("location")));

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return struct;
	}
}