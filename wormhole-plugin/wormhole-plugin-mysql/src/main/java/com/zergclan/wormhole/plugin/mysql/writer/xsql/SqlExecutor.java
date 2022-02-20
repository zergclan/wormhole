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

package com.zergclan.wormhole.plugin.mysql.writer.xsql;

import com.zergclan.wormhole.common.WormholeException;
import com.zergclan.wormhole.plugin.mysql.writer.util.SqlUtil;
import com.zergclan.wormhole.plugin.mysql.writer.xsql.parameter.ParameterHandler;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * execute sql statement.
 */
public class SqlExecutor {

    private static final Map<String, ParameterHandler> PARAMETER_HANDLER_CACHE = new ConcurrentHashMap<>(8);

    private final JdbcTemplate jdbcTemplate;

    public SqlExecutor(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * query count.
     *
     * @param querySql {@link String}
     * @param mapData  {@link Map}
     * @return int
     */
    public int queryCount(final String querySql, final Map<String, Object> mapData) {
        try {
            try (PreparedStatement ps = prepareSql(querySql, mapData)) {
                System.out.println("----------------------queryCount-------------------:" + ps);
                ResultSet rs = ps.executeQuery();
                int row = -1;
                if (rs.next()) {
                    row = rs.getInt("count");
                }

                return row;
            }
        } catch (SQLException e) {
            throw new WormholeException(e);
        }
    }

    /**
     * batch insert.
     *
     * @param insertSql  {@link String}
     * @param insertData {@link Map}
     */
    public void batchInsert(final String insertSql, final List<Map<String, Object>> insertData) {
        try {
            try (PreparedStatement ps = prepareBatchInsertSql(insertSql, insertData)) {
                System.out.println("----------------------batchInsert-------------------:" + ps);
                ps.execute();
            }
        } catch (SQLException e) {
            throw new WormholeException(e);
        }
    }

    /**
     * batch update.
     * @param updateStr  {@link String}
     * @param updateData {@link Map}
     */
    public void batchUpdate(final String updateStr, final List<Map<String, Object>> updateData) {
        updateData.forEach(map -> {
            try {
                try (PreparedStatement ps = prepareSql(updateStr, map)) {
                    System.out.println("----------------------batchUpdate-------------------:" + ps);
                    ps.execute();
                }
            } catch (SQLException e) {
                throw new WormholeException(e);
            }
        });
    }

    /**
     * prepare sql.
     *
     * @param sql {@link String}
     * @param params {@link Object}
     * @return PreparedStatement
     * @throws SQLException notNull
     */
    private PreparedStatement prepareSql(final String sql, final Object params) throws SQLException {

        ParameterHandler parameterHandler = PARAMETER_HANDLER_CACHE.get(sql);
        if (parameterHandler == null) {
            ParameterPlanner parameterPlanner = new ParameterPlanner();
            parameterHandler = parameterPlanner.plan(sql, params);
            PARAMETER_HANDLER_CACHE.put(sql, parameterHandler);
        }
        String sql2 = parameterHandler.getSql(params);
        PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement(sql2);
        parameterHandler.setParameters(ps, params);
        return ps;
    }

    /**
     * prepare batch insert sql.
     *
     * @param sql {@link String}
     * @param insertData {@link List}
     * @return PreparedStatement
     * @throws SQLException notNull
     */
    private PreparedStatement prepareBatchInsertSql(final String sql, final List<Map<String, Object>> insertData) throws SQLException {

        ParameterHandler parameterHandler = PARAMETER_HANDLER_CACHE.get(sql);
        if (parameterHandler == null) {
            ParameterPlanner parameterPlanner = new ParameterPlanner();
            parameterHandler = parameterPlanner.plan(sql, new LinkedHashMap<>());
            PARAMETER_HANDLER_CACHE.put(sql, parameterHandler);
        }

        String sql2 = parameterHandler.getSql(insertData);
        String valueStr = SqlUtil.trimValues(sql2);
        StringBuilder sb = new StringBuilder(sql2);
        for (int i = 1; i < insertData.size(); i++) {
            sb.append(valueStr);
        }
        PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement(sql2);
        parameterHandler.setBatchParameters(ps, (List) insertData);

        return ps;
    }
}
