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

package com.zergclan.wormhole.jdbc.core;

import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.core.metadata.resource.IndexMetadata;
import com.zergclan.wormhole.core.metadata.resource.SchemaMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import com.zergclan.wormhole.jdbc.api.MetaDataLoader;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.LinkedList;

/**
 * mysql metadata loader.
 */
public class MySQLMetaDataLoader implements MetaDataLoader {

    private final DatabaseMetaData databaseMetaData;

    public MySQLMetaDataLoader(final Connection connection) throws SQLException {
        databaseMetaData = connection.getMetaData();
    }

    @Override
    public Collection<SchemaMetadata> loadSchemas() throws SQLException {
        Collection<SchemaMetadata> collection = new LinkedList<>();
        try (ResultSet catalogs = databaseMetaData.getCatalogs()) {
            while (catalogs.next()) {
                String schema = catalogs.getNString("TABLE_CAT");
                SchemaMetadata schemaMetadata = new SchemaMetadata(schema, schema);
                collection.add(schemaMetadata);
            }
        }
        return collection;
    }

    @Override
    public Collection<TableMetadata> loadTables(final String schema) throws SQLException {
        Collection<TableMetadata> collection = new LinkedList<>();
        try (ResultSet tables = databaseMetaData.getTables(schema, null, null, new String[] {"TABLE"})) {
            while (tables.next()) {
                String table = tables.getString("TABLE_NAME");
                TableMetadata tableMetadata = new TableMetadata(table, schema, table);
                collection.add(tableMetadata);
            }
        }
        System.out.println(schema + " have " + collection.size() + " tables.");
        return collection;
    }

    @Override
    public Collection<TableMetadata> loadViews(final String schema) throws SQLException {
        Collection<TableMetadata> collection = new LinkedList<>();
        try (ResultSet tables = databaseMetaData.getTables(schema, null, null, new String[] {"VIEW"})) {
            while (tables.next()) {
                String table = tables.getString("TABLE_NAME");
                TableMetadata tableMetadata = new TableMetadata(table, schema, table);
                collection.add(tableMetadata);
            }
        }
        System.out.println(schema + " have " + collection.size() + " views.");
        return collection;
    }

    @Override
    public Collection<ColumnMetadata> loadColumns(final String schema, final String table) throws SQLException {
        Collection<ColumnMetadata> collection = new LinkedList<>();
        try (ResultSet columns = databaseMetaData.getColumns(schema, null, table, null)) {
            while (columns.next()) {
                String column = columns.getString("COLUMN_NAME");
                String type = columns.getString("TYPE_NAME");
                boolean nullable = "0".equals(columns.getString("NULLABLE"));
                String def = columns.getString("COLUMN_DEF");
                ColumnMetadata columnMetadata = new ColumnMetadata(column, schema, table, column, type, def, nullable);
                collection.add(columnMetadata);
            }
        }
        System.out.println(table + " have " + collection.size() + " columns.");
        return collection;
    }

    @Override
    public Optional<IndexMetadata> getPrimaryKeys(final String schema, final String table) throws SQLException {
        IndexMetadata indexMetadata;
        try (ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(schema, null, table)) {
            Collection<String> columnNames = new LinkedList<>();
            String pk = "";
            while (primaryKeys.next()) {
                String column = primaryKeys.getString("COLUMN_NAME");
                pk = primaryKeys.getString("PK_NAME");
                columnNames.add(column);
            }
            indexMetadata = new IndexMetadata(pk, schema, table, pk, true, columnNames);
        }
        return null == indexMetadata ? Optional.empty() : Optional.of(indexMetadata);
    }

    @Override
    public Collection<IndexMetadata> loadIndexes(final String schema, final String table) throws SQLException {
        Map<String, IndexMetadata> indexMap = new HashMap<>(16);
        try (ResultSet indexInfo = databaseMetaData.getIndexInfo(schema, null, table, false, false)) {
            while (indexInfo.next()) {
                String index = indexInfo.getString("INDEX_NAME");
                String column = indexInfo.getString("COLUMN_NAME");
                boolean unique = !(new Boolean(indexInfo.getString("NON_UNIQUE")));
                IndexMetadata indexMetadata = indexMap.get(index);
                if (null == indexMetadata) {
                    Collection<String> columnNames = new LinkedList<>();
                    columnNames.add(column);
                    indexMetadata = new IndexMetadata(index, schema, table,
                            index, unique, columnNames);
                    indexMap.put(index, indexMetadata);
                } else {
                    Collection<String> columnNames = indexMetadata.getColumnNames();
                    columnNames.add(column);
                }
            }
        }
        System.out.println(table + " have " + indexMap.size() + " indexes.");
        return indexMap.values();
    }
}
