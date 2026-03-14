package io.trino.plugin.iotdb;

import io.trino.spi.connector.ColumnHandle;
import io.trino.spi.connector.ConnectorRecordSetProvider;
import io.trino.spi.connector.ConnectorSession;
import io.trino.spi.connector.ConnectorSplit;
import io.trino.spi.connector.ConnectorTableHandle;
import io.trino.spi.connector.ConnectorTransactionHandle;
import io.trino.spi.connector.RecordSet;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class IoTDBRecordSetProvider implements ConnectorRecordSetProvider {
	
	private final IoTDBConfig config;
	
	public IoTDBRecordSetProvider(IoTDBConfig config) {
		this.config = requireNonNull(config, "config is null");
	}
	
	@Override
	public RecordSet getRecordSet(
			ConnectorTransactionHandle transactionHandle,
			ConnectorSession session,
			ConnectorSplit split,
			ConnectorTableHandle table,
			List<? extends ColumnHandle> columns) {
		
		IoTDBTableHandle tableHandle = (IoTDBTableHandle) table;
		List<IoTDBColumnHandle> columnHandles = columns.stream()
				.map(IoTDBColumnHandle.class::cast)
				.collect(Collectors.toList());
		return new IoTDBRecordSet(config, tableHandle, columnHandles);
	}
}
