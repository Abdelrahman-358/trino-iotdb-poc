package io.trino.plugin.iotdb;

import io.trino.spi.connector.Connector;
import io.trino.spi.connector.ConnectorMetadata;
import io.trino.spi.connector.ConnectorRecordSetProvider;
import io.trino.spi.connector.ConnectorSession;
import io.trino.spi.connector.ConnectorSplitManager;
import io.trino.spi.connector.ConnectorTransactionHandle;
import io.trino.spi.transaction.IsolationLevel;

public class IoTDBConnector implements Connector {
	
	private final IoTDBMetadata metadata;
	
	private final IoTDBSplitManager splitManager;
	
	private final IoTDBRecordSetProvider recordSetProvider;
	
	public IoTDBConnector(IoTDBConfig config) {
		this.metadata = new IoTDBMetadata(config);
		this.splitManager = new IoTDBSplitManager();
		this.recordSetProvider = new IoTDBRecordSetProvider(config);
	}
	
	@Override
	public ConnectorTransactionHandle beginTransaction(
			IsolationLevel isolationLevel,
			boolean readOnly,
			boolean autoCommit) {
		return IoTDBTransactionHandle.INSTANCE;
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
