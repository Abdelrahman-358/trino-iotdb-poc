package io.trino.plugin.iotdb;

import io.trino.spi.connector.Connector;
import io.trino.spi.connector.ConnectorContext;
import io.trino.spi.connector.ConnectorFactory;

import java.util.Map;

import static java.util.Objects.requireNonNull;

public class IotdbConnectorFactory implements ConnectorFactory {
	
	@Override
	public String getName() {
		return "iotdb";
	}
	
	@Override
	public Connector create(String catalogName, Map<String, String> config, ConnectorContext context) {
		requireNonNull(config, "config is null");
		
		IotdbConfig iotdbConfig = IotdbConfig.from(config);
		return new IotdbConnector(iotdbConfig);
	}
}
