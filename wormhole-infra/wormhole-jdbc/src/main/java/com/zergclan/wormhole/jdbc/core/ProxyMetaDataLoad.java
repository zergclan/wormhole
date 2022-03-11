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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.Optional;
import java.util.HashMap;

/**
 * proxy metaData.
 */
public class ProxyMetaDataLoad extends AbstractMetaDataHandler {

    private final DatabaseMetaData databaseMetaData;

    private final String databaseProductName;

    ProxyMetaDataLoad(final Connection connection) throws SQLException {
        databaseMetaData = connection.getMetaData();
        databaseProductName = connection.getMetaData().getDatabaseProductName().toUpperCase();
    }

    @Override
    public Collection<SchemaMetadata> loadSchemas() throws SQLException {
        //mysql->getCatalogs,oracle->getSchemas
        List<String> schemas;
        if ("MYSQL".equals(databaseProductName)) {
            schemas = initCatalogs(databaseMetaData);
        } else {
            schemas = initSchemas(databaseMetaData);
        }
        Collection<SchemaMetadata> list = new LinkedList<>();
        for (String schema : schemas) {
            SchemaMetadata schemaMetadata = new SchemaMetadata(schema, schema);
            list.add(schemaMetadata);
        }
        return list;
    }

    @Override
    public Collection<TableMetadata> loadTables(final String schema) throws SQLException {
        Collection<TableMetadata> tables = new LinkedList<>();
        List<Map<String, String>> tableList;
        if ("MYSQL".equals(databaseProductName)) {
            tableList = initTables(databaseMetaData, schema, null, null, new String[] {"TABLE"});
        } else {
            tableList = initTables(databaseMetaData, null, schema, null, new String[] {"TABLE"});
        }
        for (Map<String, String> map : tableList) {
            TableMetadata tableMetadata = new TableMetadata(map.get("TABLE_NAME"), schema, map.get("TABLE_NAME"));
            tables.add(tableMetadata);
        }
        System.out.println(schema + " have " + tables.size() + " tables.");
        return tables;
    }

    @Override
    public Collection<TableMetadata> loadViews(final String schema) throws SQLException {
        Collection<TableMetadata> views = new LinkedList<>();
        List<Map<String, String>> tableList;
        if ("MYSQL".equals(databaseProductName)) {
            tableList = initTables(databaseMetaData, schema, null, null, new String[] {"VIEW"});
        } else {
            tableList = initTables(databaseMetaData, null, schema, null, new String[] {"VIEW"});
        }
        for (Map<String, String> map : tableList) {
            TableMetadata tableMetadata = new TableMetadata(map.get("TABLE_NAME"), schema, map.get("TABLE_NAME"));
            views.add(tableMetadata);
        }
        System.out.println(schema + " have " + views.size() + " views.");
        return views;
    }

    @Override
    public Collection<ColumnMetadata> loadColumns(final String schema, final String table) throws SQLException {
        Collection<ColumnMetadata> columns = new LinkedList<>();
        List<Map<String, String>> columnsList;
        if ("MYSQL".equals(databaseProductName)) {
            columnsList = initColumns(databaseMetaData, schema, null, table, null);
        } else {
            columnsList = initColumns(databaseMetaData, null, schema, table, null);
        }
        for (Map<String, String> map : columnsList) {
            ColumnMetadata columnMetadata = new ColumnMetadata(map.get("COLUMN_NAME"), schema, map.get("TABLE_NAME"),
                    map.get("COLUMN_NAME"), map.get("TYPE_NAME"), "0".equals(map.get("NULLABLE")));
            columns.add(columnMetadata);
        }
        System.out.println(table + " have " + columns.size() + " columns.");
        return columns;
    }

    @Override
    public Optional<IndexMetadata> getPrimaryKeys(final String schema, final String table) throws SQLException {
        IndexMetadata indexMetadata = null;
        List<Map<String, String>> primaryKeList;
        if ("MYSQL".equals(databaseProductName)) {
            primaryKeList = initPrimaryKeys(databaseMetaData, schema, null, table);
        } else {
            primaryKeList = initPrimaryKeys(databaseMetaData, null, schema, table);
        }
        for (Map<String, String> map : primaryKeList) {
            Collection<String> columnNames = new LinkedList<>();
            columnNames.add(map.get("COLUMN_NAME"));
            indexMetadata = new IndexMetadata(map.get("INDEX_NAME"), schema, map.get("TABLE_NAME"),
                    map.get("PK_NAME"), true, columnNames);
        }
        return null == indexMetadata ? Optional.empty() : Optional.of(indexMetadata);
    }

    @Override
    public Collection<IndexMetadata> loadIndexes(final String schema, final String table) throws SQLException {
        Map<String, IndexMetadata> indexMap = new HashMap<>(16);
        List<Map<String, String>> indexInfoList;
        if ("MYSQL".equals(databaseProductName)) {
            indexInfoList = initIndex(databaseMetaData, schema, null, table, false, false);
        } else {
            indexInfoList = initIndex(databaseMetaData, null, schema, table, false, false);
        }
        for (Map<String, String> map : indexInfoList) {
            IndexMetadata indexMetadata = indexMap.get(map.get("INDEX_NAME"));
            if (null == indexMetadata) {
                Collection<String> columnNames = new LinkedList<>();
                columnNames.add(map.get("COLUMN_NAME"));
                indexMetadata = new IndexMetadata(map.get("INDEX_NAME"), schema, map.get("TABLE_NAME"),
                        map.get("INDEX_NAME"), !(new Boolean(map.get("NON_UNIQUE"))), columnNames);
                indexMap.put(map.get("INDEX_NAME"), indexMetadata);
            } else {
                Collection<String> columnNames = indexMetadata.getColumnNames();
                columnNames.add(map.get("COLUMN_NAME"));
            }
        }
        System.out.println(table + " have " + indexMap.size() + " indexes.");
        return indexMap.values();
    }
}
