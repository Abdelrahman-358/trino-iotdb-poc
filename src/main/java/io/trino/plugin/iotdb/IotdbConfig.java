package io.trino.plugin.iotdb;

import static java.util.Objects.requireNonNull;

import java.util.Map;

public class IotdbConfig {
	
	private final String jdbcUrl;
	
	private final String user;
	
	private final String password;
	
	public IotdbConfig(String jdbcUrl, String user, String password) {
		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
	}
	
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public static IotdbConfig from(Map<String, String> config) {
		String url = requireNonNull(config.get("iotdb.jdbc-url"), "Missing required config: iotdb.jdbc-url");
		String user = requireNonNull(config.get("iotdb.user"), "Missing required config: iotdb.user");
		String password = requireNonNull(config.get("iotdb.password"), "Missing required config: iotdb.password");
		
		return new IotdbConfig(url, user, password);
	}
}
