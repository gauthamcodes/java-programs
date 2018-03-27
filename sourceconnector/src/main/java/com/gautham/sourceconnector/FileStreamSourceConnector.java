package com.gautham.sourceconnector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gautham.sourceconnector.config.SourceConfig;
import com.gautham.sourceconnector.utils.BlobUtils;
import com.gautham.sourceconnector.utils.HttpUtils;
import com.gautham.sourceconnector.utils.StringUtils;
import com.microsoft.azure.storage.StorageException;

public class FileStreamSourceConnector extends SourceConnector {
	private static final Logger log = LoggerFactory.getLogger(FileStreamSourceConnector.class);
	private static final Integer DEFAULT_CRON_TIME = 10000;
	private static final String DEFAULT_BATCH_SIZE = "2000";
	private String schemaName;
	private String topicName;
	private String batchSize;
	private List<String> fileName;

	private static final ConfigDef CONFIG_DEF = new ConfigDef()
			.define(SourceConfig.DIRECTORY_CONFIG, Type.STRING, null, Importance.HIGH,
					"Source file path. If not specified, the standard input will be used")
			.define(SourceConfig.TOPIC_NAME_CONFIG, Type.STRING, Importance.HIGH, "The topic to publish data to")
			.define(SourceConfig.SCHEMA_NAME_CONFIG, Type.STRING, Importance.HIGH, "The schema to publish schema to")
			.define(SourceConfig.BATCH_SIZE_CONFIG, Type.INT, Importance.LOW,
					"The maximum number of records the Source task can read from file one time");

	@Override
	public String version() {
		return AppInfoParser.getVersion();
	}

	@Override
	public void start(Map<String, String> props) {
		String fileDirectory = props.get(SourceConfig.DIRECTORY_CONFIG);
		topicName = props.get(SourceConfig.TOPIC_NAME_CONFIG);
		batchSize = props.get(SourceConfig.BATCH_SIZE_CONFIG) != null ? props.get(SourceConfig.BATCH_SIZE_CONFIG)
				: DEFAULT_BATCH_SIZE;
		schemaName = props.get(SourceConfig.SCHEMA_NAME_CONFIG);
		fileName = new ArrayList<>();
		try {
			HttpUtils.fullConfig(topicName + "-value");
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					List<String> newFiles = BlobUtils.move(fileDirectory);
					if (StringUtils.isNewValAdded(newFiles, fileName)) {
						fileName = newFiles;
						context.requestTaskReconfiguration();
					}
				} catch (InvalidKeyException | URISyntaxException | StorageException e) {
					e.printStackTrace();
				}
			}
		}, 0, DEFAULT_CRON_TIME);
	}

	@Override
	public Class<? extends Task> taskClass() {
		return FileStreamSourceTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(int maxTasks) {
		return fileName.stream().map(item -> {
			Map<String, String> config = new HashMap<>();
			config.put(SourceConfig.FILE_CONFIG, item);
			config.put(SourceConfig.TOPIC_CONFIG, topicName);
			config.put(SourceConfig.SCHEMA_CONFIG, schemaName);
			config.put(SourceConfig.BATCH_SIZE_CONFIG, batchSize);
			return config;
		}).collect(Collectors.toList());
	}

	@Override
	public void stop() {
		log.info("Stopped");
	}

	@Override
	public ConfigDef config() {
		return CONFIG_DEF;
	}
}