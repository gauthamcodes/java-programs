package com.gautham.sourceconnector;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gautham.sourceconnector.config.SinkConfig;
import com.gautham.sourceconnector.jdbc.JdbcTemplate;
import com.gautham.sourceconnector.jdbc.impl.JdbcTemplateImpl;

public class MysqlSinkTask extends SinkTask {
	private static final Logger log = LoggerFactory.getLogger(MysqlSinkTask.class);
	private List<String> tableColumns;
	private String tableName;
	private JdbcTemplate jdbcTemplate;

	@Override
	public String version() {
		return new MysqlSinkConnector().version();
	}

	@Override
	public void start(Map<String, String> props) {
		tableName = props.get(SinkConfig.JDBC_TABLE);
		jdbcTemplate = new JdbcTemplateImpl(props.get(SinkConfig.JDBC_URL), props.get(SinkConfig.JDBC_USERNAME),
				props.get(SinkConfig.JDBC_PASSWORD));
		try {
			if (!jdbcTemplate.isTableExists(tableName)) {
				jdbcTemplate.createTable(tableName);
			}
			tableColumns = jdbcTemplate.getColumns(tableName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void put(Collection<SinkRecord> records) {
		log.info("new records..............................................{}", records.size());
		for (SinkRecord record : records) {
			List<String> dbColumnsUnavailable = record.valueSchema().fields().stream()
					.filter(item -> tableColumns.indexOf(item.name()) == -1).map(item -> item.name())
					.collect(Collectors.toList());
			if (dbColumnsUnavailable.size() > 0) {
				dbColumnsUnavailable.stream().forEach(item -> {
					try {
						jdbcTemplate.addColumn(tableName, item);
						tableColumns.add(item);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});
			}
			List<String> columns = record.valueSchema().fields().stream().filter(item -> !item.name().equals(SinkConfig.PRIMARY_KEY)).map(item -> item.name())
					.collect(Collectors.toList());
			List<String> values = columns.stream().map(item -> (String) ((Struct) record.value()).get(item)).collect(Collectors.toList());
			
			columns.forEach(item -> log.info(item));
			values.forEach(item -> log.info(item));
			
			Integer primaryKey = Integer.valueOf((String) ((Struct) record.value()).get(SinkConfig.PRIMARY_KEY));
			try {
				if (jdbcTemplate.isRowExists(tableName, primaryKey)) {
					jdbcTemplate.updateRow(tableName, columns, values, primaryKey);
				} else {
					jdbcTemplate.addRow(tableName, columns, values, primaryKey);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void stop() {
	}

}
