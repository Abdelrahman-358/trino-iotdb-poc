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

public class IotdbRecordSetProvider implements ConnectorRecordSetProvider {
	
	private final IotdbConfig config;
	
	public IotdbRecordSetProvider(IotdbConfig config) {
		this.config = requireNonNull(config, "config is null");
	}
	
	@Override
	public RecordSet getRecordSet(
			ConnectorTransactionHandle transactionHandle,
			ConnectorSession session,
			ConnectorSplit split,
			ConnectorTableHandle table,
			List<? extends ColumnHandle> columns) {
		
		IotdbTableHandle tableHandle = (IotdbTableHandle) table;
		List<IotdbColumnHandle> columnHandles = columns.stream()
				.map(IotdbColumnHandle.class::cast)
				.collect(Collectors.toList());
		return new IotdbRecordSet(config, tableHandle, columnHandles);
	}
}
