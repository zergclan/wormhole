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

package com.zergclan.wormhole.plugin.mysql.writer;

import com.zergclan.wormhole.common.WormholeException;
import com.zergclan.wormhole.common.util.StringUtil;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import com.zergclan.wormhole.plugin.mysql.writer.xsql.SqlExecutor;
import com.zergclan.wormhole.plugin.mysql.writer.xsql.SqlGenerator;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * load handlers.
 */
public class LoadHandler {

    public static final String SELECT_STR = "SELECT-";

    public static final String INSERT_STR = "INSERT-";

    public static final String UPDATE_STR = "UPDATE-";

    public static final Map<String, String> SQL_MAP = new ConcurrentHashMap<>(8);

    private final SqlExecutor sqlExecutor;

    public LoadHandler(final DataSource dataSource) {
        this.sqlExecutor = new SqlExecutor(new JdbcTemplate(dataSource));
    }

    /**
     * execute select sql.
     * @param tableMetadata {@link TableMetadata}
     * @param mapData {@link Map}
     * @return int
     */
    public int executeSelect(final TableMetadata tableMetadata, final Map<String, Object> mapData) {
        String selectSql = SQL_MAP.get(SELECT_STR + tableMetadata.getDataSourceIdentifier());
        if (StringUtil.isBlank(selectSql)) {
            registerCache(SELECT_STR, tableMetadata);
            selectSql = SQL_MAP.get(SELECT_STR + tableMetadata.getDataSourceIdentifier());
        }
        int count = sqlExecutor.queryCount(selectSql, mapData);

        return count;
    }

    /**
     * execute batch insert sql.
     * @param tableMetadata {@link TableMetadata}
     * @param insertData {@link List}
     */
    public void executeBatchInsert(final TableMetadata tableMetadata, final List<Map<String, Object>> insertData) {
        String insertSql = SQL_MAP.get(INSERT_STR + tableMetadata.getDataSourceIdentifier());
        if (StringUtil.isBlank(insertSql)) {
            registerCache(INSERT_STR, tableMetadata);
            insertSql = SQL_MAP.get(INSERT_STR + tableMetadata.getDataSourceIdentifier());
        }
        sqlExecutor.batchInsert(insertSql, insertData);
    }

    /**
     * execute batch insert sql.
     * @param tableMetadata {@link TableMetadata}
     * @param updateData {@link List}
     */
    public void executeBatchUpdate(final TableMetadata tableMetadata, final List<Map<String, Object>> updateData) {
        String updateStr = SQL_MAP.get(UPDATE_STR + tableMetadata.getDataSourceIdentifier());
        if (StringUtil.isBlank(updateStr)) {
            registerCache(UPDATE_STR, tableMetadata);
            updateStr = SQL_MAP.get(UPDATE_STR + tableMetadata.getDataSourceIdentifier());
        }
        sqlExecutor.batchUpdate(updateStr, updateData);
    }

    /**
     * register sql catche.
     * @param strType {@link String}
     * @param tableMetadata {@link TableMetadata}
     */
    private void registerCache(final String strType, final TableMetadata tableMetadata) {
        synchronized (SQL_MAP) {
            String sqlStr;
            switch (strType) {
                case SELECT_STR:
                    sqlStr = SqlGenerator.createSelectSql(tableMetadata);
                    break;
                case INSERT_STR:
                    sqlStr = SqlGenerator.createBatchInsertSql(tableMetadata);
                    break;
                case UPDATE_STR:
                    sqlStr = SqlGenerator.createBatchUpdateSql(tableMetadata);
                    break;
                default: throw new WormholeException("no impl load sqlType:" + strType);
            }
            SQL_MAP.put(strType + tableMetadata.getDataSourceIdentifier(), sqlStr);
        }
    }
}
