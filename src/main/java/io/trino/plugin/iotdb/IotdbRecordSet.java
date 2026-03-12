package io.trino.plugin.iotdb;

import io.trino.spi.connector.RecordCursor;
import io.trino.spi.connector.RecordSet;
import io.trino.spi.type.Type;

import java.util.List;

public class IotdbRecordSet implements RecordSet {
	
	private final IotdbConfig config;
	
	private final IotdbTableHandle table;
	
	private final List<IotdbColumnHandle> columns;
	
	public IotdbRecordSet(IotdbConfig config, IotdbTableHandle table, List<IotdbColumnHandle> columns) {
		this.config = config;
		this.table = table;
		this.columns = columns;
	}
	
	@Override
	public List<Type> getColumnTypes() {
		return columns.stream().map(IotdbColumnHandle::getColumnType).toList();
	}
	
	@Override
	public RecordCursor cursor() {
		return new IotdbRecordCursor(config, table, columns);
	}
}
