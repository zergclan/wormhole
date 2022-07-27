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

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public final class DatasourceUtil {
    
    /**
     * Create {@link DataSource}.
     *
     * @param databaseType database type
     * @param jdbcUrl jdbc url
     * @param username username
     * @param password password
     * @return {@link DataSource}
     */
    public static DataSource createDatasource(final String databaseType, final String jdbcUrl, final String username, final String password) {
        if ("MySQL".equalsIgnoreCase(databaseType)) {
            return createMySQLDatasource(jdbcUrl, username, password);
        } else {
            throw new UnsupportedOperationException();
        }
    }
    
    private static DataSource createMySQLDatasource(final String jdbcUrl, final String username, final String password) {
        HikariDataSource result = new HikariDataSource();
        result.setDriverClassName("com.mysql.cj.jdbc.Driver");
        result.setJdbcUrl(jdbcUrl);
        result.setUsername(username);
        result.setPassword(password);
        result.setMaximumPoolSize(2);
        result.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
        return result;
    }
}
