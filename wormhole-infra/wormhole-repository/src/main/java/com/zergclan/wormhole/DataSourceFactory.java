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

package com.zergclan.wormhole;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zergclan.wormhole.common.util.StringUtil;
import com.zergclan.wormhole.core.metadata.DataSourceMetadata;

import javax.sql.DataSource;

/**
 * Create factory for data source.
 */
public final class DataSourceFactory {

    static DataSource createDataSource(final DataSourceMetadata dataSourceMetadata) {
        HikariConfig config = new HikariConfig();
        config.setUsername(dataSourceMetadata.getUsername());
        config.setJdbcUrl(dataSourceMetadata.getJdbcUrl());
        config.setPoolName(dataSourceMetadata.getIdentifier());
        config.setDriverClassName(dataSourceMetadata.getDriverClassName());
        String password = dataSourceMetadata.getPassword();
        setPassword(config, password);
        return new HikariDataSource(config);
    }

    private static void setPassword(final HikariConfig config, final String password) {
        if (!StringUtil.isBlank(password)) {
            config.setPassword(password);
        }
    }

}
