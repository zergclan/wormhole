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

package com.zergclan.wormhole.reader.mysql.domain.impl;

import com.google.gson.Gson;
import com.zergclan.wormhole.reader.mysql.config.DataSourceManagerTest;
import com.zergclan.wormhole.reader.mysql.domain.MysqlMetadataDOM;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Test to get mysql metadata.
 */
@Slf4j
public final class MysqlMetadataDOMImplTest {

    /**
     * Test query all tables metadata.
     * @throws Exception Get jdbcTemplate Exception.
     */
//    @Test
    public void queryAllTables() throws Exception {
        MysqlMetadataDOM mysqlMetadataDOM = new MysqlMetadataDOMImpl();
        JdbcTemplate jdbcTemplate = DataSourceManagerTest.getTestJdbcTemplate();
        List<Map<String, Object>> allTables = mysqlMetadataDOM.queryAllTables(jdbcTemplate, "mysql");
        System.out.println(new Gson().toJson(allTables));
        Assertions.assertTrue(allTables.size() > 1);
    }

    /**
     * Test query all columns metadata.
     * @throws Exception Get jdbcTemplate Exception.
     */
//    @Test
    public void queryAllColumns() throws Exception {
        MysqlMetadataDOM mysqlMetadataDOM = new MysqlMetadataDOMImpl();
        JdbcTemplate jdbcTemplate = DataSourceManagerTest.getTestJdbcTemplate();
        List<Map<String, Object>> allColumns = mysqlMetadataDOM.queryAllColumns(jdbcTemplate, "mysql", "user");
        System.out.println(new Gson().toJson(allColumns));
        Assertions.assertTrue(allColumns.size() > 1);
    }

}
