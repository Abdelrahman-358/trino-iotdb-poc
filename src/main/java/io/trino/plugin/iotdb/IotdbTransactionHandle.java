package io.trino.plugin.iotdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.trino.spi.connector.ConnectorTransactionHandle;

public class IotdbTransactionHandle implements ConnectorTransactionHandle {
	
	public static final IotdbTransactionHandle INSTANCE = new IotdbTransactionHandle();
	
	@JsonCreator
	public IotdbTransactionHandle() {
	}
	
	@JsonProperty
	public boolean getInstance() {
		return true;
	}
}
