package io.trino.plugin.iotdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.trino.spi.connector.ConnectorTableHandle;

public class IotdbTableHandle implements ConnectorTableHandle {
	
	private final String schemaName;
	
	private final String tableName;
	
	@JsonCreator
	public IotdbTableHandle(
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
