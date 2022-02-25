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

import com.zergclan.wormhole.core.metadata.resource.DatabaseType;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * MySQLMetaDataHandler test.
 */
public class MySQLMetaDataHandlerTest {

    @Test
    public void assertGetTables() throws SQLException {
//        HikariConfig config = new HikariConfig();
//        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        config.setJdbcUrl("jdbc:mysql://localhost:3306/slzx?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai");
//        config.setUsername("slzx");
//        config.setPassword("slzx");
//        try (Connection connection = (new HikariDataSource(config)).getConnection()) {
//            System.out.println(MySQLMetaDataHandler.getTables(connection, "slzx"));
//        }
        try (Connection connection = getDataSource().getConnection()) {
            System.out.println(MySQLMetaDataHandler.getTables(connection, "PUBLIC").size());
        }
    }

    @Test
    public void assertGetIndexInfo() throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
            System.out.println(MySQLMetaDataHandler.getIndexInfo(connection, "PUBLIC", "target_table").size());
        }
    }

    /**
     * get a data source by mode to be the target of sync data.
     * @return DataSource
     */
    private DataSource getDataSource() {
        DataSourceModeFactory dataSourceModeFactory = new DataSourceModeFactory();
        DataSource dataSource = dataSourceModeFactory.getTestDataSource(DatabaseType.MYSQL);
        return dataSource;
    }
}
