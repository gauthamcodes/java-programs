package com.gautham.sourceconnector.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.dbcp2.BasicDataSource;

import com.gautham.sourceconnector.jdbc.JdbcTemplate;
import com.gautham.sourceconnector.utils.StringUtils;

public class JdbcTemplateImpl implements JdbcTemplate {
	private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
	private BasicDataSource dataSource;

	public JdbcTemplateImpl() {

	}

	public JdbcTemplateImpl(String jdbcUrl, String username, String password) {
		BasicDataSource pool = new BasicDataSource();
		pool.setDriverClassName(DRIVER_NAME);
		pool.setUrl(jdbcUrl);
		pool.setUsername(username);
		pool.setPassword(password);
		this.dataSource = pool;
	}

	@Override
	public boolean isTableExists(String tableName) throws SQLException {
		Connection conn = dataSource.getConnection();
		boolean tExists = false;
		try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
			if (rs.next()) {
				tExists = true;
			}
		}
		conn.close();
		return tExists;
	}

	@Override
	public void createTable(String tableName) throws SQLException {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		String sql = "CREATE TABLE " + tableName + " (id INTEGER NOT NULL, "
				+ " created_on DATETIME DEFAULT CURRENT_TIMESTAMP, " + " PRIMARY KEY ( id ))";
		stmt.executeUpdate(sql);
		conn.close();
	}

	@Override
	public List<String> getColumns(String tableName) throws SQLException {
		Connection conn = dataSource.getConnection();
		List<String> columnNames = new ArrayList<>();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM " + tableName + " LIMIT 0");
		ResultSetMetaData md = rs.getMetaData();
		int col = md.getColumnCount();
		for (int i = 1; i <= col; i++) {
			columnNames.add(md.getColumnName(i));
		}
		conn.close();
		return columnNames;
	}

	@Override
	public void addColumn(String tableName, String columnName) throws SQLException {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		String query = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " VARCHAR(255)";
		stmt.execute(query);
		conn.close();
	}

	@Override
	public boolean isRowExists(String tableName, Integer id) throws SQLException {
		Integer count = 0;
		Connection conn = dataSource.getConnection();
		String query = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
		PreparedStatement preparedStmt = conn.prepareStatement(query);
		preparedStmt.setInt(1, id);
		ResultSet resultSet = preparedStmt.executeQuery();		
		if(resultSet.next()) {
			count = resultSet.getInt(1);
		}
		conn.close();		
		return count > 0;
	}

	@Override
	public void addRow(String tableName, List<String> columns, List<String> values, Integer primaryKey)
			throws SQLException {
		Connection conn = dataSource.getConnection();
		String query = "INSERT INTO " + tableName + " (id," + StringUtils.fieldsClause(columns) + ") values ("
				+ primaryKey + "," + StringUtils.placeholderClause(values) + ")";
		PreparedStatement preparedStmt = conn.prepareStatement(query);
		IntStream.rangeClosed(1, values.size()).forEach(i -> {
			try {
				preparedStmt.setString(i, values.get(i - 1));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		preparedStmt.execute();
		conn.close();
	}

	@Override
	public void updateRow(String tableName, List<String> fields, List<String> values, Integer primaryKey)
			throws SQLException {
		Connection conn = dataSource.getConnection();
		String query = "UPDATE "+ tableName + " SET " + StringUtils.updateClause(fields) + " WHERE id = " + primaryKey;
		PreparedStatement preparedStmt = conn.prepareStatement(query);
		IntStream.rangeClosed(1, values.size()).forEach(i -> {
			try {
				preparedStmt.setString(i, values.get(i - 1));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		preparedStmt.execute();
		conn.close();
	}

}
