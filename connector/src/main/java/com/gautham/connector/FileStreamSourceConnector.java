package com.gautham.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;

public class FileStreamSourceConnector extends SourceConnector {

	public static final String TOPIC_CONFIG = "topic";
	public static final String FILE_CONFIG = "file";
	public static final String TASK_BATCH_SIZE_CONFIG = "batch.size";
	public static final int DEFAULT_TASK_BATCH_SIZE = 2000;

	private String filename;
	private String topic;
	private int batchsize = DEFAULT_TASK_BATCH_SIZE;

	private static final ConfigDef CONFIG_DEF = new ConfigDef()
			.define(FILE_CONFIG, Type.STRING, null, Importance.HIGH,
					"Source filename. If not specified, the standard input will be used")
			.define(TOPIC_CONFIG, Type.STRING, Importance.HIGH, "The topic to publish data to")
			.define(TASK_BATCH_SIZE_CONFIG, Type.INT, Importance.LOW,
					"The maximum number of records the Source task can read from file one time");

	@Override
	public String version() {
		return AppInfoParser.getVersion();
	}

	@Override
	public void start(Map<String, String> props) {
		filename = props.get(FILE_CONFIG);
        topic = props.get(TOPIC_CONFIG);
        if (topic == null || topic.isEmpty())
            throw new ConnectException("FileStreamSourceConnector configuration must include 'topic' setting");
        if (topic.contains(","))
            throw new ConnectException("FileStreamSourceConnector should only have a single topic when used as a source.");
	}

	@Override
	public Class<? extends Task> taskClass() {
		return FileStreamSourceTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(int maxTasks) {
		ArrayList<Map<String, String>> configs = new ArrayList<>();
		// Only one input stream makes sense.
		Map<String, String> config = new HashMap<>();
		if (filename != null)
			config.put(FILE_CONFIG, filename);
		config.put(TOPIC_CONFIG, topic);
		config.put(TASK_BATCH_SIZE_CONFIG, String.valueOf(batchsize));
		configs.add(config);
		return configs;
	}

	@Override
	public void stop() {
		// Nothing to do since FileStreamSourceConnector has no background monitoring.
	}

	@Override
	public ConfigDef config() {
		return CONFIG_DEF;
	}
}