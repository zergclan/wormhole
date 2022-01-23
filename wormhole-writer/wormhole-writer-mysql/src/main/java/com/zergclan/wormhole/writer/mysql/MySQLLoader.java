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

package com.zergclan.wormhole.writer.mysql;

import com.zergclan.wormhole.common.util.StringUtil;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import com.zergclan.wormhole.loader.JdbcLoadContent;
import com.zergclan.wormhole.loader.LoadContent;
import com.zergclan.wormhole.loader.Loader;
import com.zergclan.wormhole.writer.xsql.SqlExecutor;
import com.zergclan.wormhole.writer.xsql.SqlGenerator;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

/**
 * Loader for MySQL.
 */
@Setter
public class MySQLLoader implements Loader {

    public final static String SELECT_STR = "SELECT-";
    public final static String INSERT_STR = "INSERT-";
    public final static String UPDATE_STR = "UPDATE-";

    private final SqlExecutor sqlExecutor;
    private final Map<String,String> sqlMap = new HashMap<>(8);

    public MySQLLoader(final JdbcTemplate jdbcTemplate) {
        this.sqlExecutor =  new SqlExecutor(jdbcTemplate);
    }

    @Override
    public void loaderData(final LoadContent loadContent) {
        JdbcLoadContent jdbcLoadContent = (JdbcLoadContent) loadContent;
        Collection<Map<String, Object>> listData = jdbcLoadContent.getLoadData();
        Map<String, TableMetadata> tables = jdbcLoadContent.getTables();
        for (Map.Entry<String, TableMetadata> entry : tables.entrySet()) {
            String table = entry.getKey();
            TableMetadata tableMetadata = entry.getValue();
            System.out.println("load data into "+table);

            List<Map<String, Object>> insertData = new LinkedList<>();
            List<Map<String, Object>> updateData = new LinkedList<>();
            for(Map<String, Object> mapData : listData) {
                int count = executeSelect(tableMetadata,mapData);
                if(count > 0) {
                    updateData.add(mapData);
                } else {
                    insertData.add(mapData);
                }
            }
            executeBatchInsert(tableMetadata,insertData);
            executeBatchUpdate(tableMetadata,insertData);
        }
    }

    private int executeSelect(final TableMetadata tableMetadata, final Map<String, Object> mapData) {
        String selectSql = sqlMap.get(SELECT_STR+tableMetadata.getDataSourceIdentifier());
        if(StringUtil.isBlank(selectSql)) {
            selectSql = registerSqlMap(SELECT_STR, tableMetadata);
        }
        int count = sqlExecutor.queryCount(selectSql,mapData);
        return count;
    }

    private void executeBatchInsert(final TableMetadata tableMetadata, final List<Map<String, Object>> insertData) {
        String insertSql = sqlMap.get(INSERT_STR+tableMetadata.getDataSourceIdentifier());
        if(StringUtil.isBlank(insertSql)) {
            insertSql = registerSqlMap(INSERT_STR, tableMetadata);
        }
        int[] result = sqlExecutor.batchInsert(insertSql,insertData);
    }

    private void executeBatchUpdate(final TableMetadata tableMetadata, final List<Map<String, Object>> insertData) {
        String updateStr = sqlMap.get(UPDATE_STR+tableMetadata.getDataSourceIdentifier());
        if(StringUtil.isBlank(updateStr)) {
            updateStr = registerSqlMap(UPDATE_STR, tableMetadata);
        }
        int[] result = sqlExecutor.batchUpdate(updateStr,insertData);
    }

    private String registerSqlMap(final String strType, final TableMetadata tableMetadata) {
        String sqlStr;
        switch(strType) {
            case SELECT_STR : {
                sqlStr = SqlGenerator.createSelectSql(tableMetadata);
                break;
            }
            case INSERT_STR : {
                sqlStr = SqlGenerator.createBatchInsertSql(tableMetadata);
                break;
            }
            case UPDATE_STR : {
                sqlStr = SqlGenerator.createBatchUpdateSql(tableMetadata);
                break;
            }
            default : sqlStr = "";  // TODO throws Exception
        }
        sqlMap.put(SELECT_STR+tableMetadata.getDataSourceIdentifier(), sqlStr);
        return sqlStr;
    }
}
