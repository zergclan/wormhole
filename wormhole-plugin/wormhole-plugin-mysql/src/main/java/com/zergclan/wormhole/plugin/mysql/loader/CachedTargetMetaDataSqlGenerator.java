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

package com.zergclan.wormhole.plugin.mysql.loader;

import com.zergclan.wormhole.common.util.StringUtil;
import com.zergclan.wormhole.metadata.core.catched.CachedTargetMetaData;
import com.zergclan.wormhole.metadata.core.node.DataNodeMetaData;
import com.zergclan.wormhole.plugin.mysql.xsql.SqlGenerator;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * generate sql statement.
 */
@RequiredArgsConstructor
public final class CachedTargetMetaDataSqlGenerator implements SqlGenerator {

    public static final String SELECT_STR = "SELECT-";

    public static final String INSERT_STR = "INSERT-";

    public static final String UPDATE_STR = "UPDATE-";
    
    private final CachedTargetMetaData cachedTargetMetaData;

    private final Map<String, String> sqlMap = new ConcurrentHashMap<>();
    
    @Override
    public String createSelectSql() {
        String sqlKey = SELECT_STR + cachedTargetMetaData.getIdentifier();
        String selectSql = sqlMap.get(sqlKey);
        if (StringUtil.isBlank(selectSql)) {
            StringBuilder stringBuilder = new StringBuilder(" select ");
            Collection<String> compareNodes = cachedTargetMetaData.getCompareNodes();
            Iterator<String> compareNodesIterator = compareNodes.iterator();
            String selectField = StringUtil.join(compareNodesIterator, ",");
            stringBuilder.append(selectField);
            stringBuilder.append(" from ").append(cachedTargetMetaData.getTable()).append(" where 1=1 ");
            Collection<String> uniqueNodes = cachedTargetMetaData.getUniqueNodes();
            for (String each : uniqueNodes) {
                stringBuilder.append(" and ").append(each).append(" = ").append(String.format(" $%s{%s} ", "O", each));
            }
            selectSql = stringBuilder.toString();
            sqlMap.put(sqlKey, selectSql);
        }
        System.out.println("-----------------createSelectSql------------------:" + selectSql);
        return selectSql;
    }

    @Override
    public String createInsertSql() {
        String sqlKey = INSERT_STR + cachedTargetMetaData.getIdentifier();
        String insertSql = sqlMap.get(sqlKey);
        if (StringUtil.isBlank(insertSql)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" insert into ").append(cachedTargetMetaData.getTable());

            Map<String, DataNodeMetaData> dataNodes = cachedTargetMetaData.getDataNodes();
            String tableColumn = "";
            String valueColumn = "";
            for (Map.Entry<String, DataNodeMetaData> entry : dataNodes.entrySet()) {
                String columnName = entry.getValue().getName();
                tableColumn += columnName + ",";
                valueColumn += String.format(" $%s{%s} ", "O", columnName) + ",";
            }
            stringBuilder.append(" (").append(tableColumn.substring(0, tableColumn.length() - 1)).append(") ")
                    .append(" values (").append(valueColumn.substring(0, valueColumn.length() - 1)).append(")");
            insertSql = stringBuilder.toString();
            sqlMap.put(sqlKey, insertSql);
        }
        System.out.println("-----------------createInsertSql------------------:" + insertSql);
        return insertSql;
    }

    @Override
    public String createUpdateSql() {
        String sqlKey = UPDATE_STR + cachedTargetMetaData.getIdentifier();
        String updateSql = sqlMap.get(sqlKey);
        if (StringUtil.isBlank(updateSql)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" update ").append(cachedTargetMetaData.getTable()).append(" set ");
            Map<String, DataNodeMetaData> dataNodes = cachedTargetMetaData.getDataNodes();
            for (Map.Entry<String, DataNodeMetaData> entry : dataNodes.entrySet()) {
                String columnName = entry.getValue().getName();
                stringBuilder.append(columnName + " = ");
                stringBuilder.append(String.format(" $%s{%s} ", "O", columnName));
                stringBuilder.append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append(" where 1=1 ");
            Collection<String> uniqueNodes = cachedTargetMetaData.getUniqueNodes();
            for (int i = 0; i < uniqueNodes.size(); i++) {
                String columnName = ((List<String>) uniqueNodes).get(i);
                stringBuilder.append(" and ").append(columnName).append(" = ").append(String.format(" $%s{%s} ", "O", columnName));
            }
            updateSql = stringBuilder.toString();
            sqlMap.put(sqlKey, updateSql);
        }
        System.out.println("-----------------createUpdateSql------------------:" + updateSql);
        return updateSql;
    }

}
