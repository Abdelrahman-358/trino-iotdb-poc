package io.trino.plugin.iotdb;

import io.trino.spi.connector.Connector;
import io.trino.spi.connector.ConnectorMetadata;
import io.trino.spi.connector.ConnectorRecordSetProvider;
import io.trino.spi.connector.ConnectorSession;
import io.trino.spi.connector.ConnectorSplitManager;
import io.trino.spi.connector.ConnectorTransactionHandle;
import io.trino.spi.transaction.IsolationLevel;

public class IotdbConnector implements Connector {
	
	private final IotdbMetadata metadata;
	
	private final IotdbSplitManager splitManager;
	
	private final IotdbRecordSetProvider recordSetProvider;
	
	public IotdbConnector(IotdbConfig config) {
		this.metadata = new IotdbMetadata(config);
		this.splitManager = new IotdbSplitManager();
		this.recordSetProvider = new IotdbRecordSetProvider(config);
	}
	
	@Override
	public ConnectorTransactionHandle beginTransaction(
			IsolationLevel isolationLevel,
			boolean readOnly,
			boolean autoCommit) {
		return IotdbTransactionHandle.INSTANCE;
	}
	
	@Override
	public void commit(ConnectorTransactionHandle transaction) {
	}
	
	@Override
	public void rollback(ConnectorTransactionHandle transaction) {
	}
	
	@Override
	public ConnectorMetadata getMetadata(ConnectorSession session, ConnectorTransactionHandle transactionHandle) {
		return metadata;
	}
	
	@Override
	public ConnectorSplitManager getSplitManager() {
		return splitManager;
	}
	
	@Override
	public ConnectorRecordSetProvider getRecordSetProvider() {
		return recordSetProvider;
	}
}
