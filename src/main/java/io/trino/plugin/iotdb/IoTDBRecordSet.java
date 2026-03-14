package io.trino.plugin.iotdb;

import io.trino.spi.connector.RecordCursor;
import io.trino.spi.connector.RecordSet;
import io.trino.spi.type.Type;

import java.util.List;

public class IoTDBRecordSet implements RecordSet {
	
	private final IoTDBConfig config;
	
	private final IoTDBTableHandle table;
	
	private final List<IoTDBColumnHandle> columns;
	
	public IoTDBRecordSet(IoTDBConfig config, IoTDBTableHandle table, List<IoTDBColumnHandle> columns) {
		this.config = config;
		this.table = table;
		this.columns = columns;
	}
	
	@Override
	public List<Type> getColumnTypes() {
		return columns.stream().map(IoTDBColumnHandle::getColumnType).toList();
	}
	
	@Override
	public RecordCursor cursor() {
		return new IoTDBRecordCursor(config, table, columns);
	}
}
