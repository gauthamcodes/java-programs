package com.gautham.twitterstream;

public enum TwitterConfig {
	NAME("name"), 
	TOPIC_CONFIG("topic"), 
	CONSUMERKEY_CONFIG("twitter.consumerkey"), 
	CONSUMERSECRET_CONFIG("twitter.consumersecret"), 
	TOKEN_CONFIG("twitter.token"), 
	SECRET_CONFIG("twitter.secret"), 
	TWITTER_TAG("twitter.tag"), 
	SCHEMA_NAME("schema.name");

	private String value;

	TwitterConfig(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
