package io.trino.plugin.iotdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: This class is a temporary workaround for JDBC driver issue.
 *       It should be removed once the driver is fixed.
 */
public class IoTDBJdbcHelper {
	
	private final String jdbcUrl;
	
	private final String user;
	
	private final String password;
	
	public IoTDBJdbcHelper(IoTDBConfig config) {
		this.jdbcUrl = config.getJdbcUrl();
		this.user = config.getUser();
		this.password = config.getPassword();
		try {
			Class.forName("org.apache.iotdb.jdbc.IoTDBDriver");
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException("IoTDB JDBC driver not found", e);
		}
	}
	
	public Connection getConnection()
			throws SQLException {
		return DriverManager.getConnection(jdbcUrl, user, password);
	}
	
	public List<String> queryColumn(String sql, int columnIndex) {
		List<String> results = new ArrayList<>();
		try (Connection conn = getConnection();
		     Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				results.add(rs.getString(columnIndex));
			}
		}
		catch (SQLException e) {
			throw new RuntimeException("IoTDB query failed: " + sql, e);
		}
		return results;
	}
}


