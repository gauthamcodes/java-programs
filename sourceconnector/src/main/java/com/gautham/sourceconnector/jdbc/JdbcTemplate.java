package com.gautham.sourceconnector.jdbc;

import java.sql.SQLException;
import java.util.List;

public interface JdbcTemplate {
	boolean isTableExists(String tableName) throws SQLException;
	
	boolean isRowExists(String tableName, Integer id) throws SQLException;

	void createTable(String tableName) throws SQLException;

	List<String> getColumns(String tableName) throws SQLException;

	void addColumn(String tableName, String columnName) throws SQLException;

	void addRow(String tableName, List<String> columns, List<String> values, Integer primaryKey) throws SQLException;

	void updateRow(String tableName, List<String> fields, List<String> values, Integer primaryKey) throws SQLException;
}
