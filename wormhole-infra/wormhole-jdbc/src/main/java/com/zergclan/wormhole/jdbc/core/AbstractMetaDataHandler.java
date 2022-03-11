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

import com.zergclan.wormhole.jdbc.api.MetaDataLoader;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractMetaDataHandler implements MetaDataLoader {

    /**
     * init catalogs by databaseMetaData.getCatalogs.
     * @param databaseMetaData {@link DatabaseMetaData}
     * @return String
     * @throws SQLException exception
     */
    public static List<String> initCatalogs(final DatabaseMetaData databaseMetaData) throws SQLException {
        ResultSet catalog = databaseMetaData.getCatalogs();
        List<String> catalogs = new LinkedList<>();
        while (catalog.next()) {
            catalogs.add(catalog.getNString("TABLE_CAT"));
        }
        return catalogs;
    }

    /**
     * init schemas by databaseMetaData.getSchemas.
     * @param databaseMetaData {@link DatabaseMetaData}
     * @return String
     * @throws SQLException exception
     */
    public static List<String> initSchemas(final DatabaseMetaData databaseMetaData) throws SQLException {
        ResultSet schema = databaseMetaData.getSchemas();
        List<String> schemas = new LinkedList<>();
        while (schema.next()) {
            schemas.add(schema.getNString("TABLE_SCHEM"));
        }
        return schemas;
    }

    /**
     * init tables by databaseMetaData.getTables.
     * @param databaseMetaData {@link DatabaseMetaData}
     * @param catalog {@link String}
     * @param schemaPattern {@link String}
     * @param tableNamePattern {@link String}
     * @param types {@link String}
     * @return List
     * @throws SQLException notNull
     */
    public static List<Map<String, String>> initTables(final DatabaseMetaData databaseMetaData,
                                                       final String catalog,
                                                       final String schemaPattern,
                                                       final String tableNamePattern,
                                                       final String[] types) throws SQLException {
        ResultSet tables = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
        List<Map<String, String>> tableList = new LinkedList<>();
        while (tables.next()) {
            Map<String, String> rsValue = new HashMap<>(16);
            rsValue.put("TABLE_CAT", tables.getString("TABLE_CAT"));
            rsValue.put("TABLE_SCHEM", tables.getString("TABLE_SCHEM"));
            rsValue.put("TABLE_NAME", tables.getString("TABLE_NAME"));
            rsValue.put("TABLE_TYPE", tables.getString("TABLE_TYPE"));
            rsValue.put("REMARKS", tables.getString("REMARKS"));
            rsValue.put("TYPE_SCHEM", tables.getString("TYPE_SCHEM"));
            rsValue.put("TYPE_NAME", tables.getString("TYPE_NAME"));
            rsValue.put("SELF_REFERENCING_COL_NAME", tables.getString("SELF_REFERENCING_COL_NAME"));
            rsValue.put("REF_GENERATION", tables.getString("REF_GENERATION"));
            tableList.add(rsValue);
        }
        return tableList;
    }

    /**
     * init columns by databaseMetaData.getColumns.
     * @param databaseMetaData {@link DatabaseMetaData}
     * @param catalog {@link String}
     * @param schemaPattern {@link String}
     * @param tableNamePattern {@link String}
     * @param columnNamePattern {@link String}
     * @return List
     * @throws SQLException notNull
     */
    public static List<Map<String, String>> initColumns(final DatabaseMetaData databaseMetaData,
                                                        final String catalog,
                                                        final String schemaPattern,
                                                        final String tableNamePattern,
                                                        final String columnNamePattern) throws SQLException {
        ResultSet columns = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        List<Map<String, String>> columnsList = new LinkedList<>();
        while (columns.next()) {
            Map<String, String> rsValue = new HashMap<>(16);
            rsValue.put("TABLE_CAT", columns.getString("TABLE_CAT"));
            rsValue.put("TABLE_SCHEM", columns.getString("TABLE_SCHEM"));
            rsValue.put("TABLE_NAME", columns.getString("TABLE_NAME"));
            rsValue.put("COLUMN_NAME", columns.getString("COLUMN_NAME"));
            rsValue.put("TYPE_NAME", columns.getString("TYPE_NAME"));
            rsValue.put("COLUMN_SIZE", columns.getString("COLUMN_SIZE"));
            rsValue.put("REMARKS", columns.getString("REMARKS"));
            rsValue.put("NULLABLE", columns.getString("NULLABLE"));
            rsValue.put("COLUMN_DEF", columns.getString("COLUMN_DEF"));
            columnsList.add(rsValue);
        }
        return columnsList;
    }

    /**
     * init index by databaseMetaData.getIndexInfo.
     * @param databaseMetaData {@link DatabaseMetaData}
     * @param catalog {@link String}
     * @param schema {@link String}
     * @param table {@link String}
     * @param unique {@link boolean}
     * @param approximate {@link boolean}
     * @return List
     * @throws SQLException notNull
     */
    public static List<Map<String, String>> initIndex(final DatabaseMetaData databaseMetaData,
                                                      final String catalog,
                                                      final String schema,
                                                      final String table,
                                                      final boolean unique,
                                                      final boolean approximate) throws SQLException {
        ResultSet indexInfo = databaseMetaData.getIndexInfo(catalog, schema, table, unique, approximate);
        List<Map<String, String>> indexInfoList = new LinkedList<>();
        while (indexInfo.next()) {
            Map<String, String> rsValue = new HashMap<>(16);
            rsValue.put("TABLE_CAT", indexInfo.getString("TABLE_CAT"));
            rsValue.put("TABLE_SCHEM", indexInfo.getString("TABLE_SCHEM"));
            rsValue.put("TABLE_NAME", indexInfo.getString("TABLE_NAME"));
            rsValue.put("INDEX_NAME", indexInfo.getString("INDEX_NAME"));
            rsValue.put("COLUMN_NAME", indexInfo.getString("COLUMN_NAME"));
            rsValue.put("NON_UNIQUE", indexInfo.getString("NON_UNIQUE"));
            indexInfoList.add(rsValue);
        }
        return indexInfoList;
    }

    /**
     * init primaryKeys by databaseMetaData.getPrimaryKeys.
     * @param databaseMetaData {@link DatabaseMetaData}
     * @param catalog {@link String}
     * @param schema {@link String}
     * @param table {@link String}
     * @return List
     * @throws SQLException notNull
     */
    public static List<Map<String, String>> initPrimaryKeys(final DatabaseMetaData databaseMetaData,
                                                            final String catalog,
                                                            final String schema,
                                                            final String table) throws SQLException {
        ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(catalog, schema, table);
        List<Map<String, String>> primaryKeyList = new LinkedList<>();
        while (primaryKeys.next()) {
            Map<String, String> rsValue = new HashMap<>(16);
            rsValue.put("TABLE_CAT", primaryKeys.getString("TABLE_CAT"));
            rsValue.put("TABLE_SCHEM", primaryKeys.getString("TABLE_SCHEM"));
            rsValue.put("TABLE_NAME", primaryKeys.getString("TABLE_NAME"));
            rsValue.put("PK_NAME", primaryKeys.getString("PK_NAME"));
            rsValue.put("COLUMN_NAME", primaryKeys.getString("COLUMN_NAME"));
            primaryKeyList.add(rsValue);
        }
        return primaryKeyList;
    }

}
