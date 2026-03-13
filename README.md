# Trino–IoTDB Connector (Prototype)

This repository contains a minimal prototype Trino connector for Apache IoTDB (Table Model) using **IoTDB JDBC**.  
It is primarily intended for learning.

## Status

- [x] Trino catalog loads
- [x] List schemas 
- [x] List tables
- [x] Basic `SELECT` queries

## Requirements

- Java 17+ 
- Maven
- Trino server + CLI
- Apache IoTDB 2.0.1-beta 

## Build

```bash
mvn -DskipTests package
```

This should produce a connector JAR.

## Install the connector into Trino

1. Create a plugin directory (example):

```bash
mkdir -p /opt/trino/trino-server/plugin/iotdb
```

2. Copy the built JAR into the plugin directory:

```bash
cp target/*.jar /opt/trino/trino-server/plugin/iotdb/
```

3. Add a catalog config file:

```properties
# /opt/trino/trino-server/etc/catalog/iotdb.properties
connector.name=iotdb
iotdb.jdbc-url=jdbc:iotdb://127.0.0.1:6667?sql_dialect=table
iotdb.user=root
iotdb.password=root
```

4. Restart Trino.

## Quick test

### In IoTDB

Example schema/table:

```sql
CREATE DATABASE test1;

CREATE TABLE test1.table1(
  region_id STRING TAG,
  plant_id  STRING TAG,
  device_id STRING TAG,
  model     STRING ATTRIBUTE,
  temperature FLOAT FIELD,
  humidity    DOUBLE FIELD
) WITH (TTL=3600000);
```

Insert using current time (TTL is 1 hour, so avoid old timestamps):

```sql
INSERT INTO test1.table1(time, region_id, plant_id, device_id, model, temperature, humidity)
VALUES (now(), 'region1', 'plant1', 'device1', 'modelA', 25.5, 60.0);
```

### In Trino

```sql
SHOW SCHEMAS FROM iotdb;
     
USE iotdb.test1;
    
SHOW TABLES;

SELECT * FROM table1;

SELECT humidity FROM table1;
```

## License

Apache-2.0.
