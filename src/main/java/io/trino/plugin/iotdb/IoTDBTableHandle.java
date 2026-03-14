package io.trino.plugin.iotdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.trino.spi.connector.ConnectorTableHandle;

public class IoTDBTableHandle implements ConnectorTableHandle {
	
	private final String schemaName;
	
	private final String tableName;
	
	@JsonCreator
	public IoTDBTableHandle(
			@JsonProperty("schemaName") String schemaName,
			@JsonProperty("tableName") String tableName) {
		this.schemaName = schemaName;
		this.tableName = tableName;
	}
	
	@JsonProperty
	public String getSchemaName() {
		return schemaName;
	}
	
	@JsonProperty
	public String getTableName() {
		return tableName;
	}
}
