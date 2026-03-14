package io.trino.plugin.iotdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.trino.spi.connector.ConnectorTransactionHandle;

public class IoTDBTransactionHandle implements ConnectorTransactionHandle {
	
	public static final IoTDBTransactionHandle INSTANCE = new IoTDBTransactionHandle();
	
	@JsonCreator
	public IoTDBTransactionHandle() {
	}
	
	@JsonProperty
	public boolean getInstance() {
		return true;
	}
}
