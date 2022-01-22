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

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * execute sql statement
 */
public class SqlExecutor {
    private final JdbcTemplate jdbcTemplate;

    public SqlExecutor(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int queryCount(final String querySql, final Map<String, Object> mapData) {
        return jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(selectSql);
                ps.setLong(1, Long.parseLong(String.valueOf(map.get("transBigint"))));
                ps.setString(2, String.valueOf(map.get("transVarchar")));
                return ps;
            }
        }, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(final ResultSet rs) throws SQLException, DataAccessException {
                return rs.getRow();
            }
        });
    }

    public int[] batchInsert(final String insertSql, final List<Map<String, Object>> insertData) {
        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                PreparedStatement ps1 = connection.prepareStatement(insertSql);
                ps1.setInt(1, Integer.parseInt(String.valueOf(map.get("transInt"))));
                ps1.setLong(2, Long.parseLong(String.valueOf(map.get("transBigint"))));
                ps1.setString(3, String.valueOf(map.get("transVarchar")));
                ps1.setBigDecimal(4, new BigDecimal(String.valueOf(map.get("transDecimal"))));
                ps1.setObject(5, map.get("transDatetime"));
                System.out.println(ps1);
                return ps1;
            }
        });
    }

    public int[] batchUpdate(final String updateStr, final List<Map<String, Object>> updateData) {
        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                PreparedStatement ps2 = connection.prepareStatement(updateSql);
                ps2.setInt(1, Integer.parseInt(String.valueOf(map.get("transInt"))));
                ps2.setBigDecimal(2, new BigDecimal(String.valueOf(map.get("transDecimal"))));
                ps2.setObject(3, map.get("transDatetime"));
                ps2.setLong(4, Long.parseLong(String.valueOf(map.get("transBigint"))));
                ps2.setString(5, String.valueOf(map.get("transVarchar")));
                System.out.println(ps2);
                return ps2;
            }
        });
    }
}
