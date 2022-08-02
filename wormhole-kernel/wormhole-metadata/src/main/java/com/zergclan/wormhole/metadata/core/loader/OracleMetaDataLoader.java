/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zergclan.wormhole.metadata.core.loader;

import com.zergclan.wormhole.metadata.core.datasource.ColumnMetaData;
import com.zergclan.wormhole.metadata.core.datasource.IndexMetaData;
import com.zergclan.wormhole.metadata.core.datasource.SchemaMetaData;
import com.zergclan.wormhole.metadata.core.datasource.TableMetaData;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

/**
 * oracle metadata loader.
 */
public class OracleMetaDataLoader implements MetaDataLoader {

    private final DatabaseMetaData databaseMetaData;

    public OracleMetaDataLoader(final Connection connection) throws SQLException {
        databaseMetaData = connection.getMetaData();
    }

    @Override
    public Collection<SchemaMetaData> loadSchemas(final String dataSourceIdentifier) throws SQLException {
        Collection<SchemaMetaData> collection = new LinkedList<>();
        try (ResultSet catalogs = databaseMetaData.getSchemas()) {
            while (catalogs.next()) {
                String schema = catalogs.getNString("TABLE_SCHEM");
                SchemaMetaData schemaMetadata = new SchemaMetaData(dataSourceIdentifier, schema);
                collection.add(schemaMetadata);
            }
        }
        return collection;
    }

    @Override
    public Collection<TableMetaData> loadTables(final String dataSourceIdentifier, final String schema) throws SQLException {
        Collection<TableMetaData> collection = new LinkedList<>();
        try (ResultSet tables = databaseMetaData.getTables(null, schema, null, new String[] {"TABLE"})) {
            while (tables.next()) {
                String table = tables.getString("TABLE_NAME");
                TableMetaData tableMetadata = new TableMetaData(dataSourceIdentifier, schema, table);
                collection.add(tableMetadata);
            }
        }
        return collection;
    }

    @Override
    public Collection<TableMetaData> loadViews(final String dataSourceIdentifier, final String schema) throws SQLException {
        Collection<TableMetaData> collection = new LinkedList<>();
        try (ResultSet tables = databaseMetaData.getTables(null, schema, null, new String[] {"VIEW"})) {
            while (tables.next()) {
                String table = tables.getString("TABLE_NAME");
                TableMetaData tableMetadata = new TableMetaData(dataSourceIdentifier, schema, table);
                collection.add(tableMetadata);
            }
        }
        return collection;
    }

    @Override
    public Collection<ColumnMetaData> loadColumns(final String dataSourceIdentifier, final String schema, final String table) throws SQLException {
        Collection<ColumnMetaData> collection = new LinkedList<>();
        try (ResultSet columns = databaseMetaData.getColumns(null, schema, table, null)) {
            while (columns.next()) {
                String column = columns.getString("COLUMN_NAME");
                String type = columns.getString("TYPE_NAME");
                boolean nullable = "0".equals(columns.getString("NULLABLE"));
                String defaultValue = columns.getString("COLUMN_DEF");
                ColumnMetaData columnMetadata = new ColumnMetaData(dataSourceIdentifier, schema, table, column, type, defaultValue, nullable);
                collection.add(columnMetadata);
            }
        }
        return collection;
    }

    @Override
    public Optional<IndexMetaData> getPrimaryKeys(final String dataSourceIdentifier, final String schema, final String table) throws SQLException {
        IndexMetaData indexMetadata;
        try (ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(null, schema, table)) {
            Collection<String> columnNames = new LinkedList<>();
            String primaryKeyName = "";
            while (primaryKeys.next()) {
                String column = primaryKeys.getString("COLUMN_NAME");
                primaryKeyName = primaryKeys.getString("PK_NAME");
                columnNames.add(column);
            }
            indexMetadata = new IndexMetaData(dataSourceIdentifier, schema, table, primaryKeyName, true, columnNames);
        }
        return Optional.of(indexMetadata);
    }

    @Override
    public Collection<IndexMetaData> loadIndexes(final String dataSourceIdentifier, final String schema, final String table) throws SQLException {
        Map<String, IndexMetaData> indexMap = new HashMap<>(16);
        try (ResultSet indexInfo = databaseMetaData.getIndexInfo(null, schema, table, false, false)) {
            while (indexInfo.next()) {
                String index = indexInfo.getString("INDEX_NAME");
                String column = indexInfo.getString("COLUMN_NAME");
                String nonUnique = indexInfo.getString("NON_UNIQUE");
                boolean unique = null == nonUnique ? Boolean.FALSE : Boolean.valueOf(nonUnique);
                IndexMetaData indexMetadata = indexMap.get(index);
                if (null == indexMetadata) {
                    Collection<String> columnNames = new LinkedList<>();
                    columnNames.add(column);
                    indexMetadata = new IndexMetaData(index, schema, table,
                            index, unique, columnNames);
                    indexMap.put(index, indexMetadata);
                } else {
                    Collection<String> columnNames = indexMetadata.getColumnNames();
                    columnNames.add(column);
                }
            }
        }
        return indexMap.values();
    }
}
