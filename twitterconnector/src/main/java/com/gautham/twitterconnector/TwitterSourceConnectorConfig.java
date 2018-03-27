package com.gautham.twitterconnector;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Iterables;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;

public class TwitterSourceConnectorConfig extends AbstractConfig {

  public static final String TWITTER_DEBUG_CONF = "twitter.debug";
  public static final String TWITTER_OAUTH_CONSUMER_KEY_CONF = "twitter.oauth.consumerKey";
  public static final String TWITTER_OAUTH_SECRET_KEY_CONF = "twitter.oauth.consumerSecret";
  public static final String TWITTER_OAUTH_ACCESS_TOKEN_CONF = "twitter.oauth.accessToken";
  public static final String TWITTER_OAUTH_ACCESS_TOKEN_SECRET_CONF = "twitter.oauth.accessTokenSecret";
  public static final String FILTER_KEYWORDS_CONF = "filter.keywords";
  public static final String KAFKA_STATUS_TOPIC_CONF = "kafka.status.topic";
  public static final String KAFKA_STATUS_TOPIC_DOC = "Kafka topic to write the statuses to.";
  public static final String PROCESS_DELETES_CONF = "process.deletes";
  public static final String PROCESS_DELETES_DOC = "Should this connector process deletes.";
  private static final String TWITTER_DEBUG_DOC = "Flag to enable debug logging for the twitter api.";
  private static final String TWITTER_OAUTH_CONSUMER_KEY_DOC = "OAuth consumer key";
  private static final String TWITTER_OAUTH_SECRET_KEY_DOC = "OAuth consumer secret";
  private static final String TWITTER_OAUTH_ACCESS_TOKEN_DOC = "OAuth access token";
  private static final String TWITTER_OAUTH_ACCESS_TOKEN_SECRET_DOC = "OAuth access token secret";
  private static final String FILTER_KEYWORDS_DOC = "Twitter keywords to filter for.";

  public final String topic;
  public final boolean twitterDebug;
  public final boolean processDeletes;

  public TwitterSourceConnectorConfig(Map<String, String> parsedConfig) {
    super(conf(), parsedConfig);
    this.topic = this.getString(KAFKA_STATUS_TOPIC_CONF);
    this.twitterDebug = this.getBoolean(TWITTER_DEBUG_CONF);
    this.processDeletes = this.getBoolean(PROCESS_DELETES_CONF);
  }

  public static ConfigDef conf() {
    return new ConfigDef()
        .define(TWITTER_DEBUG_CONF, Type.BOOLEAN, false, Importance.LOW, TWITTER_DEBUG_DOC)
        .define(TWITTER_OAUTH_CONSUMER_KEY_CONF, Type.PASSWORD, Importance.HIGH, TWITTER_OAUTH_CONSUMER_KEY_DOC)
        .define(TWITTER_OAUTH_SECRET_KEY_CONF, Type.PASSWORD, Importance.HIGH, TWITTER_OAUTH_SECRET_KEY_DOC)
        .define(TWITTER_OAUTH_ACCESS_TOKEN_CONF, Type.PASSWORD, Importance.HIGH, TWITTER_OAUTH_ACCESS_TOKEN_DOC)
        .define(TWITTER_OAUTH_ACCESS_TOKEN_SECRET_CONF, Type.PASSWORD, Importance.HIGH, TWITTER_OAUTH_ACCESS_TOKEN_SECRET_DOC)
        .define(FILTER_KEYWORDS_CONF, Type.LIST, Importance.HIGH, FILTER_KEYWORDS_DOC)
        .define(KAFKA_STATUS_TOPIC_CONF, Type.STRING, Importance.HIGH, KAFKA_STATUS_TOPIC_DOC)
        .define(PROCESS_DELETES_CONF, Type.BOOLEAN, Importance.HIGH, PROCESS_DELETES_DOC);
  }


  public Configuration configuration() {
    Properties properties = new Properties();
    /*
      Grab all of the key/values that have a key that starts with twitter. This will strip 'twitter.' from beginning of
      each key. This aligns with what the twitter4j framework is expecting.
     */
    properties.putAll(this.originalsWithPrefix("twitter."));
    return new PropertyConfiguration(properties);
  }

  public String[] filterKeywords() {
    List<String> keywordList = this.getList(FILTER_KEYWORDS_CONF);
    String[] keywords = Iterables.toArray(keywordList, String.class);
    return keywords;
  }
}