package io.trino.plugin.iotdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.trino.spi.connector.ConnectorSplit;

import java.util.Map;

public class IotdbSplit implements ConnectorSplit {
	
	public static final IotdbSplit INSTANCE = new IotdbSplit();
	
	@JsonCreator
	public IotdbSplit() {
	}
	
	@JsonProperty
	public boolean getInstance() {
		return true;
	}
	
	@Override
	public Map<String, String> getSplitInfo() {
		return Map.of();
	}
	
	@Override
	public long getRetainedSizeInBytes() {
		return 0;
	}
}
