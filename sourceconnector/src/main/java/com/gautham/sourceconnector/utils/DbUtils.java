package com.gautham.sourceconnector.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DbUtils {
	public static boolean tableExist(Connection conn, String tableName) throws SQLException {
		boolean tExists = false;
		try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
			if (rs.next()) {
				tExists = true;
			}
		}
		return tExists;
	}

	public static void createTable(Connection conn, String tableName) throws SQLException {
		Statement stmt = conn.createStatement();
		String sql = "CREATE TABLE " + tableName + " (id INTEGER NOT NULL AUTO_INCREMENT, "
				+ " created_on DATETIME DEFAULT CURRENT_TIMESTAMP, " + " PRIMARY KEY ( id ))";
		stmt.executeUpdate(sql);
	}

	public static List<String> getColumns(Connection conn, String tableName) throws SQLException {
		List<String> columnNames = new ArrayList<>();

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM " + tableName + " LIMIT 0");
		ResultSetMetaData md = rs.getMetaData();
		int col = md.getColumnCount();
		for (int i = 1; i <= col; i++) {
			columnNames.add(md.getColumnName(i));
		}

		return columnNames;
	}

	public static void addColumn(Connection conn, String tableName, String columnName) throws SQLException {
		Statement stmt = conn.createStatement();
		String query = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " VARCHAR(255)";
		stmt.execute(query);
	}

	public static void addRow(Connection conn, String tableName, List<String> columnName, List<String> values) throws SQLException {
		String query = "INSERT INTO " + tableName + " " + StringUtils.fieldsClause(columnName) + " values "
				+ StringUtils.placeholderClause(values);
		PreparedStatement preparedStmt = conn.prepareStatement(query);
		IntStream.rangeClosed(1, values.size()).forEach(i -> {
			try {
				preparedStmt.setString(i, values.get(i-1));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		preparedStmt.execute();
	}
}
