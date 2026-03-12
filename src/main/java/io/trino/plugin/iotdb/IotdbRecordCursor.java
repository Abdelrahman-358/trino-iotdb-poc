package io.trino.plugin.iotdb;

import io.airlift.slice.Slice;
import io.airlift.slice.Slices;
import io.trino.spi.connector.RecordCursor;
import io.trino.spi.type.RealType;
import io.trino.spi.type.Type;

import java.sql.*;
import java.util.List;

public class IotdbRecordCursor implements RecordCursor {
	
	private final List<IotdbColumnHandle> columns;
	
	private final Connection connection;
	
	private final Statement statement;
	
	private final ResultSet resultSet;
	
	public IotdbRecordCursor(IotdbConfig config, IotdbTableHandle table, List<IotdbColumnHandle> columns) {
		this.columns = columns;
		
		try {
			IotdbJdbcHelper jdbcHelper = new IotdbJdbcHelper(config);
			this.connection = jdbcHelper.getConnection();
			
			this.statement = connection.createStatement();
			
			String sql = buildSelectSql(table, columns);
			this.resultSet = statement.executeQuery(sql);
		}
		catch (SQLException e) {
			throw new RuntimeException("Failed to open IoTDB JDBC cursor", e);
		}
	}
	
	private String buildSelectSql(IotdbTableHandle table, List<IotdbColumnHandle> columns) {
		if (columns.isEmpty()) {
			return "SELECT * FROM " + table.getSchemaName() + "." + table.getTableName();
		}
		String cols = columns.stream().map(IotdbColumnHandle::getColumnName).reduce((a, b) -> a + ", " + b).orElse("*");
		return "SELECT " + cols + " FROM " + table.getSchemaName() + "." + table.getTableName();
	}
	
	@Override
	public boolean advanceNextPosition() {
		try {
			return resultSet.next();
		}
		catch (SQLException e) {
			throw new RuntimeException("Failed to advance cursor", e);
		}
	}
	
	@Override
	public boolean getBoolean(int field) {
		try {
			return resultSet.getBoolean(field + 1);
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public long getLong(int field) {
		try {
			Type type = getType(field);
			if (type.equals(RealType.REAL)) {
				return Float.floatToIntBits(resultSet.getFloat(field + 1));
			}
			return resultSet.getLong(field + 1);
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public double getDouble(int field) {
		try {
			return resultSet.getDouble(field + 1);
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Slice getSlice(int field) {
		try {
			String value = resultSet.getString(field + 1);
			return value == null ? null : Slices.utf8Slice(value);
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Object getObject(int field) {
		try {
			return resultSet.getObject(field + 1);
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public long getCompletedBytes() {
		return 0;
	}
	
	@Override
	public long getReadTimeNanos() {
		return 0;
	}
	
	@Override
	public Type getType(int field) {
		return columns.get(field).getColumnType();
	}
	
	@Override
	public boolean isNull(int field) {
		try {
			resultSet.getObject(field + 1);
			return resultSet.wasNull();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void close() {
		try {
			resultSet.close();
			statement.close();
			connection.close();
		}
		catch (SQLException ignored) {
		}
	}
}
