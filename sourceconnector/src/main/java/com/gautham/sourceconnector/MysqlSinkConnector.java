package com.gautham.sourceconnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gautham.sourceconnector.config.SinkConfig;

public class MysqlSinkConnector extends SinkConnector {

	private static final Logger log = LoggerFactory.getLogger(MysqlSinkConnector.class);
	private Map<String, String> props;
	
	@Override
	public String version() {
		return AppInfoParser.getVersion();
	}

	@Override
	public void start(Map<String, String> props) {
		this.props = props;
	}

	@Override
	public Class<? extends Task> taskClass() {
		return MysqlSinkTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(int maxTasks) {
		List<Map<String, String>> taskConfigs = new ArrayList<Map<String, String>>();
        Map<String, String> taskProps = new HashMap<String, String>();
        taskProps.putAll(props);
        for (int i = 0; i < maxTasks; i++) {
            taskConfigs.add(taskProps);
        }
        return taskConfigs;
	}

	@Override
	public void stop() {
		log.info("stopped");
	}

	@Override
	public ConfigDef config() {
		return new ConfigDef().define(SinkConfig.JDBC_URL, ConfigDef.Type.STRING, null, ConfigDef.Importance.HIGH,
				"JDBC URL to database (jdbc:postgresql://localhost:5432/db)");
	}

}
