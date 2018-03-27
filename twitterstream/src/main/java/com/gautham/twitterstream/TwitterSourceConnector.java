package com.gautham.twitterstream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterSourceConnector extends SourceConnector {

	private static final ConfigDef CONFIG_DEF = new ConfigDef().define(TwitterConfig.TOPIC_CONFIG.getValue(),
			Type.STRING, null, Importance.HIGH, "Topic");

	private static final Logger log = LoggerFactory.getLogger(TwitterSourceTask.class);

	private Map<String, String> props;

	@Override
	public String version() {
		return AppInfoParser.getVersion();
	}

	@Override
	public void start(Map<String, String> props) {
		log.info("start");
		this.props = props;
	}

	@Override
	public Class<? extends Task> taskClass() {
		return TwitterSourceTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(int maxTasks) {
		log.info("cfgs");
		ArrayList<Map<String, String>> configs = new ArrayList<>();
		Map<String, String> config = new HashMap<>();
		for(TwitterConfig value:TwitterConfig.values()) {
			config.put(value.getValue(), props.get(value.getValue()));
		}
		configs.add(config);
		return configs;
	}

	@Override
	public void stop() {
	}

	@Override
	public ConfigDef config() {
		return CONFIG_DEF;
	}
}
