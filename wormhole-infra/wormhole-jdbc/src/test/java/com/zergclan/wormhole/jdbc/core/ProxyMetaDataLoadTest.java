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

package com.zergclan.wormhole.jdbc.core;
import com.zergclan.wormhole.jdbc.api.MetaDataLoader;
import org.junit.Test;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ProxyMetaDataLoadTest {

    @Test
    public void assertLoadSchemas() throws SQLException {
        try (Connection connection = getConnection()) {
            MetaDataLoader metaDataLoader = new ProxyMetaDataLoad(connection);
            metaDataLoader.loadSchemas();
        }
    }

    @Test
    public void assertLoadTables() throws SQLException {
        try (Connection connection = getConnection()) {
            MetaDataLoader metaDataLoader = new ProxyMetaDataLoad(connection);
            metaDataLoader.loadTables("test");
        }
    }

    private Connection getConnection() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai");
        config.setUsername("root");
        config.setPassword("123456");
        return (new HikariDataSource(config)).getConnection();
    }
}
