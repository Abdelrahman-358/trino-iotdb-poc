package io.trino.plugin.iotdb;

import io.trino.spi.connector.ConnectorSplitManager;
import io.trino.spi.connector.ConnectorSplitSource;
import io.trino.spi.connector.ConnectorTableHandle;
import io.trino.spi.connector.ConnectorTransactionHandle;
import io.trino.spi.connector.FixedSplitSource;
import io.trino.spi.connector.ConnectorSession;
import io.trino.spi.connector.DynamicFilter;
import io.trino.spi.connector.Constraint;

import java.util.List;

public class IoTDBSplitManager implements ConnectorSplitManager {
	
	@Override
	public ConnectorSplitSource getSplits(
			ConnectorTransactionHandle transactionHandle,
			ConnectorSession session,
			ConnectorTableHandle tableHandle,
			DynamicFilter dynamicFilter,
			Constraint constraint) {
		return new FixedSplitSource(List.of(IoTDBSplit.INSTANCE));
	}
}
