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

package com.zergclan.wormhole.test.integration.framework.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public final class DataSourceUtil {
    
    private static DataSource createDataSource(final String dbType, final String db, final Integer port, final String password) {
        if ("mysql".equalsIgnoreCase(dbType)) {
            return createMySQLDataSource(db, port, password);
        } else if ("postgresql".equalsIgnoreCase(dbType)){
            return createPostgreSQLDataSource(db, port, password);
        } else {
            throw new UnsupportedOperationException();
        }
    }
    
    private static DataSource createMySQLDataSource(final String db, final Integer port, final String password) {
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        String jdbcUrl = "jdbc:mysql://127.0.0.1:"+port+"/"+db+"?serverTimezone=UTC&useSSL=false";
        String username = "houyang";
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }
    
    private static DataSource createPostgreSQLDataSource(final String db, final Integer port, final String password) {
        String driverClassName = "org.postgresql.Driver";
        String jdbcUrl = "jdbc:postgresql://127.0.0.1:"+port+"/"+db+"";
        String username = "houyang";
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }
}
