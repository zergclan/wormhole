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

package com.zergclan.wormhole.jdbc.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Data source creator.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataSourceCreator {
    
    /**
     * Create.
     *
     * @param configProperties config properties
     * @return {@link DataSource}
     */
    public static DataSource create(final Properties configProperties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(String.valueOf(configProperties.get("driverClassName")));
        hikariConfig.setJdbcUrl(String.valueOf(configProperties.get("jdbcUrl")));
        hikariConfig.setUsername(String.valueOf(configProperties.get("username")));
        hikariConfig.setPassword(String.valueOf(configProperties.get("password")));
        return new HikariDataSource(hikariConfig);
    }
}
