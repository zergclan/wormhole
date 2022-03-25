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

import com.zergclan.wormhole.plugin.mysql.old.writer.xsql.SqlExecutor;
import org.springframework.jdbc.core.JdbcTemplate;

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
     */
    public List<Map<String, String>> executeSelect(final Collection selectData) {
        //TODO
        return null;
    }

    /**
     * execute batch insert sql.
     * @param insertData {@link Collection}
     */
    public void executeBatchInsert(final Collection insertData) {
        //TODO
    }

    /**
     * execute batch insert sql.
     * @param updateData {@link Object}
     */
    public void executeUpdate(final Object updateData) {
        //TODO
    }
}
