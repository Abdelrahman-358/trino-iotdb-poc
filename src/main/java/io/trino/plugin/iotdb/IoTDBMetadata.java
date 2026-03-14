package io.trino.plugin.iotdb;

import io.trino.spi.connector.ColumnHandle;
import io.trino.spi.connector.ColumnMetadata;
import io.trino.spi.connector.ConnectorMetadata;
import io.trino.spi.connector.ConnectorSession;
import io.trino.spi.connector.ConnectorTableHandle;
import io.trino.spi.connector.ConnectorTableMetadata;
import io.trino.spi.connector.ConnectorTableVersion;
import io.trino.spi.connector.SchemaTableName;
import io.trino.spi.type.BigintType;
import io.trino.spi.type.BooleanType;
import io.trino.spi.type.DoubleType;
import io.trino.spi.type.IntegerType;
import io.trino.spi.type.RealType;
import io.trino.spi.type.Type;
import io.trino.spi.type.VarcharType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IoTDBMetadata implements ConnectorMetadata {
	
	private final IoTDBJdbcHelper jdbc;
	
	public IoTDBMetadata(IoTDBConfig config) {
		this.jdbc = new IoTDBJdbcHelper(config);
	}
	
	@Override
	public List<String> listSchemaNames(ConnectorSession session) {
		return jdbc.queryColumn("SHOW DATABASES", 1);
	}
	
	@Override
	public List<SchemaTableName> listTables(ConnectorSession session, Optional<String> schemaName) {
		List<SchemaTableName> tables = new ArrayList<>();
		
		List<String> schemas = schemaName.isPresent()
				? Collections.singletonList(schemaName.get())
				: listSchemaNames(session);
		
		for (String schema : schemas) {
			List<String> tableNames = jdbc.queryColumn("SHOW TABLES FROM " + schema, 1);
			for (String table : tableNames) {
				tables.add(new SchemaTableName(schema, table));
			}
		}
		return tables;
	}
	
	@Override
	public ConnectorTableHandle getTableHandle(ConnectorSession session, SchemaTableName tableName,
			Optional<ConnectorTableVersion> startVersion, Optional<ConnectorTableVersion> endVersion) {
		// Check if the table exists
		List<String> tableNames = jdbc.queryColumn(
				"SHOW TABLES FROM " + tableName.getSchemaName(), 1);
		
		if (tableNames.stream().anyMatch(t -> t.equalsIgnoreCase(tableName.getTableName()))) {
			return new IoTDBTableHandle(tableName.getSchemaName(), tableName.getTableName());
		}
		return null; // table not found
	}
	
	@Override
	public ConnectorTableMetadata getTableMetadata(ConnectorSession session, ConnectorTableHandle table) {
		IoTDBTableHandle iotdbTable = (IoTDBTableHandle) table;
		List<ColumnMetadata> columns = getColumns(iotdbTable.getSchemaName(), iotdbTable.getTableName());
		
		return new ConnectorTableMetadata(
				new SchemaTableName(iotdbTable.getSchemaName(), iotdbTable.getTableName()),
				columns);
	}
	
	@Override
	public Map<String, ColumnHandle> getColumnHandles(ConnectorSession session, ConnectorTableHandle tableHandle) {
		IoTDBTableHandle iotdbTable = (IoTDBTableHandle) tableHandle;
		List<ColumnMetadata> columns = getColumns(iotdbTable.getSchemaName(), iotdbTable.getTableName());
		
		Map<String, ColumnHandle> handles = new HashMap<>();
		for (int i = 0; i < columns.size(); i++) {
			ColumnMetadata col = columns.get(i);
			handles.put(col.getName(), new IoTDBColumnHandle(col.getName(), col.getType(), i));
		}
		return handles;
	}
	
	@Override
	public ColumnMetadata getColumnMetadata(ConnectorSession session, ConnectorTableHandle tableHandle,
			ColumnHandle columnHandle) {
		IoTDBColumnHandle col = (IoTDBColumnHandle) columnHandle;
		return new ColumnMetadata(col.getColumnName(), col.getColumnType());
	}
	
	private List<ColumnMetadata> getColumns(String schema, String tableName) {
		List<ColumnMetadata> columns = new ArrayList<>();
		String sql = "DESCRIBE " + schema + "." + tableName;
		
		try (Connection conn = jdbc.getConnection();
		     Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				String colName = rs.getString(1);
				String colType = rs.getString(2);
				columns.add(new ColumnMetadata(colName, mapType(colType)));
			}
		}
		catch (SQLException e) {
			throw new RuntimeException("Failed to describe table: " + schema + "." + tableName, e);
		}
		return columns;
	}
	
	//TODO: incomplete type mapping
	private static Type mapType(String iotdbType) {
		return switch (iotdbType.toUpperCase()) {
			case "BOOLEAN" -> BooleanType.BOOLEAN;
			case "INT32" -> IntegerType.INTEGER;
			case "INT64", "TIMESTAMP" -> BigintType.BIGINT;
			case "FLOAT" -> RealType.REAL;
			case "DOUBLE" -> DoubleType.DOUBLE;
			case "TEXT", "STRING" -> VarcharType.VARCHAR;
			default -> VarcharType.VARCHAR; // Fallback to VARCHAR for unknown types
		};
	}
}
