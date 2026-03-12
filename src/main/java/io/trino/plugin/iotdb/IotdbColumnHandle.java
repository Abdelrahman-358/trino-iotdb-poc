package io.trino.plugin.iotdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.trino.spi.connector.ColumnHandle;
import io.trino.spi.type.Type;

public class IotdbColumnHandle implements ColumnHandle {
	
	private final String columnName;
	
	private final Type columnType;
	
	private final int ordinalPosition;
	
	@JsonCreator
	public IotdbColumnHandle(
			@JsonProperty("columnName") String columnName,
			@JsonProperty("columnType") Type columnType,
			@JsonProperty("ordinalPosition") int ordinalPosition) {
		this.columnName = columnName;
		this.columnType = columnType;
		this.ordinalPosition = ordinalPosition;
	}
	
	@JsonProperty
	public String getColumnName() {
		return columnName;
	}
	
	@JsonProperty
	public Type getColumnType() {
		return columnType;
	}
	
	@JsonProperty
	public int getOrdinalPosition() {
		return ordinalPosition;
	}
}
