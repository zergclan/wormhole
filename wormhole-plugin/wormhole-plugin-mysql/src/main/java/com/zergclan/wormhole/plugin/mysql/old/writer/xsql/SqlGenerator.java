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

package com.zergclan.wormhole.plugin.mysql.old.writer.xsql;

import com.zergclan.wormhole.common.util.StringUtil;
import com.zergclan.wormhole.core.metadata.resource.ColumnMetaData;
import com.zergclan.wormhole.core.metadata.resource.IndexMetaData;
import com.zergclan.wormhole.core.metadata.resource.TableMetaData;
import com.zergclan.wormhole.plugin.mysql.old.writer.util.SqlUtil;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * generate sql statement.
 */
public class SqlGenerator {

    private static Map<String, String> dataType = new ConcurrentHashMap<>(16);

//    static {
//        dataType.put("VARCHAR","S");
//        dataType.put("BIGINT","L");
//        dataType.put("DATETIME","O");
//        dataType.put("INT","I");
//        dataType.put("DECIMAL","B");
//        dataType.put("DOUBLE","D");
//        dataType.put("CHAR","S");
//        dataType.put("LONGTEXT","S");
//        dataType.put("TEXT","S");
//        dataType.put("DATE","O");
//        dataType.put("TIMESTAMP","O");
//        dataType.put("TIME","O");
//        dataType.put("FLOAT","O");
//    }

    /**
     * create select sql.
     * @param tableMetadata {@link String}
     * @return String
     */
    public static String createSelectSql(final TableMetaData tableMetadata) {

        Map<String, IndexMetaData> indexes = tableMetadata.getIndexes();
        Iterator<Map.Entry<String, IndexMetaData>> it = indexes.entrySet().iterator();
        IndexMetaData indexMetadata = it.next().getValue();
        Collection<String> columnNames = indexMetadata.getColumnNames();
        String selectStr = " select count(" + ((List<String>) columnNames).get(0) + ") count from " + tableMetadata.getName() + " where 1=1 ";
        for (int i = 0; i < columnNames.size(); i++) {
            String column = ((List<String>) columnNames).get(i);
            selectStr += " and " + column + " = " + getDataTypeFormat(column, tableMetadata.getColumns());
        }
        System.out.println("-----------------createSelectSql------------------:" + selectStr);
        return selectStr;
    }

    /**
     * create batch insert sql.
     *
     * @param tableMetadata {@link String}
     * @return String
     */
    public static String createBatchInsertSql(final TableMetaData tableMetadata) {
        StringBuilder sb = new StringBuilder();
        sb.append(" insert into ").append(tableMetadata.getName());

        Map<String, ColumnMetaData> columns = tableMetadata.getColumns();
        String tableColumn = "";
        String valueColumn = "";
        for (Map.Entry<String, ColumnMetaData> entry : columns.entrySet()) {
            tableColumn += entry.getValue().getName() + ",";
            valueColumn += getDataTypeFormat(entry.getKey(), entry.getValue()) + ",";
        }

        sb.append(" (").append(tableColumn.substring(0, tableColumn.length() - 1)).append(") ")
                .append(" values (").append(valueColumn.substring(0, valueColumn.length() - 1)).append(")");

        System.out.println("-----------------createBatchInsertSql------------------:" + sb);
        return sb.toString();
    }

    /**
     * create batch update sql.
     *
     * @param tableMetadata {@link String}
     * @return String
     */
    public static String createBatchUpdateSql(final TableMetaData tableMetadata) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update ").append(tableMetadata.getName()).append(" set ");
        Map<String, ColumnMetaData> columns = tableMetadata.getColumns();
        for (Map.Entry<String, ColumnMetaData> entry : columns.entrySet()) {
            sb.append(entry.getValue().getName() + " = ");
            sb.append(getDataTypeFormat(entry.getKey(), entry.getValue()));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" where 1=1 ");
        Map<String, IndexMetaData> indexes = tableMetadata.getIndexes();
        Iterator<Map.Entry<String, IndexMetaData>> it = indexes.entrySet().iterator();
        IndexMetaData indexMetadata = it.next().getValue();
        Collection<String> columnNames = indexMetadata.getColumnNames();
        for (int i = 0; i < columnNames.size(); i++) {
            String column = ((List<String>) columnNames).get(i);
            sb.append(" and ").append(column).append(" = ").append(getDataTypeFormat(column, tableMetadata.getColumns()));
        }

        System.out.println("-----------------createBatchUpdateSql------------------:" + sb);
        return sb.toString();
    }

    /**
     * format dataType.
     * @param column  {@link String}
     * @param columns {@link Map}
     * @return String
     */
    private static String getDataTypeFormat(final String column, final Map<String, ColumnMetaData> columns) {
        String key = SqlUtil.sqlToJava(column);
        ColumnMetaData columnMetadata = columns.get(key);
        String dataType = columnMetadata.getDataType().split("\\(")[0];
        String value = SqlGenerator.dataType.get(dataType.toUpperCase());
        if (StringUtil.isBlank(value)) {
            value = "O";
        }
        String format = String.format(" $%s{%s} ", value, key);
        return format;
    }

    /**
     * format dataType.
     * @param column         {@link String}
     * @param columnMetadata {@link ColumnMetaData}
     * @return String
     */
    private static String getDataTypeFormat(final String column, final ColumnMetaData columnMetadata) {
        String key = SqlUtil.sqlToJava(column);
        String dataType = columnMetadata.getDataType().split("\\(")[0];
        String value = SqlGenerator.dataType.get(dataType.toUpperCase());
        if (StringUtil.isBlank(value)) {
            value = "O";
        }
        String format = String.format(" $%s{%s} ", value, key);
        return format;
    }
}
