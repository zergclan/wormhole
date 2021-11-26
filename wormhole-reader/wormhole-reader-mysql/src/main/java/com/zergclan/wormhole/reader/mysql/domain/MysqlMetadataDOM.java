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

package com.zergclan.wormhole.reader.mysql.domain;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Get mysql metadata interface.
 */
public interface MysqlMetadataDOM {

    /**
     * Query all tables metadata.
     * @param jt JdbcTemplate.
     * @param dbName The database to be queried.
     * @return Meta information for all tables.
     */
    List<Map<String, Object>> queryAllTables(JdbcTemplate jt, String dbName);

    /**
     * Query all columns metadata.
     * @param jt JdbcTemplate.
     * @param dbName The database to be queried.
     * @param tableName The tableName to be queried.
     * @return Meta information for all columns.
     */
    List<Map<String, Object>> queryAllColumns(JdbcTemplate jt, String dbName, String tableName);

}
