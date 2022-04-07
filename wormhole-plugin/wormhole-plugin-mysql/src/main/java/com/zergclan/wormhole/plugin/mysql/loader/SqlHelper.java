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

import com.zergclan.wormhole.plugin.mysql.xsql.SqlExecutor;
import com.zergclan.wormhole.plugin.mysql.xsql.SqlGenerator;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * SqlHelper.
 */
public final class SqlHelper {

    private final SqlExecutor sqlExecutor;

    private final SqlGenerator sqlGenerator;

    public SqlHelper(final JdbcTemplate jdbcTemplate, final SqlGenerator sqlGenerator) {
        this.sqlExecutor = new SqlExecutor(jdbcTemplate);
        this.sqlGenerator = sqlGenerator;
    }

    /**
     * execute select sql.
     * @param selectData {@link Collection}
     * @return List
     * @throws SQLException  Exception
     */
    public List<Map<String, Object>> executeSelect(final Object selectData) throws SQLException {
        String selectSql = sqlGenerator.createSelectSql();
        List<Map<String, Object>> result = sqlExecutor.query(selectSql, selectData);
        return result;
    }

    /**
     * execute insert sql.
     * @param insertData {@link Collection}
     * @throws SQLException  Exception
     */
    public void executeInsert(final Object insertData) throws SQLException {
        String insertSql = sqlGenerator.createInsertSql();
        sqlExecutor.execute(insertSql, insertData);
    }

    /**
     * execute insert sql.
     * @param updateData {@link Object}
     * @throws SQLException  Exception
     */
    public void executeUpdate(final Object updateData) throws SQLException {
        String updateSql = sqlGenerator.createUpdateSql();
        sqlExecutor.execute(updateSql, updateData);
    }
}
