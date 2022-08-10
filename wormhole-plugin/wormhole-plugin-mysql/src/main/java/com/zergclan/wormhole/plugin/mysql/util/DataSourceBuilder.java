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

package com.zergclan.wormhole.plugin.mysql.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.datasource.DataSourcePoolMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builder of {@link DataSource}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataSourceBuilder {
    
    private static final Map<String, DataSource> DATA_SOURCES = new LinkedHashMap<>();
    
    /**
     * Build.
     *
     * @param dataSourceMetadata {@link DataSourceMetaData}
     * @return {@link DataSource}
     */
    public static synchronized DataSource build(final DataSourceMetaData dataSourceMetadata) {
        String identifier = dataSourceMetadata.getIdentifier();
        DataSource result = DATA_SOURCES.get(identifier);
        if (null == result) {
            result = new HikariDataSource(createConfiguration(dataSourceMetadata));
            DATA_SOURCES.put(identifier, result);
        }
        return result;
    }
    
    private static HikariConfig createConfiguration(final DataSourceMetaData dataSourceMetadata) {
        HikariConfig result = new HikariConfig();
        result.setDriverClassName(dataSourceMetadata.getDriverClassName());
        result.setJdbcUrl(dataSourceMetadata.getJdbcUrl());
        result.setUsername(dataSourceMetadata.getUsername());
        result.setPassword(dataSourceMetadata.getPassword());
        DataSourcePoolMetadata pool = dataSourceMetadata.getPool();
        result.setPoolName(pool.getPoolName());
        result.setMinimumIdle(pool.getIdleTimeout());
        result.setMaximumPoolSize(pool.getMaxPoolSize());
        result.setConnectionTimeout(pool.getConnectionTimeout());
        result.setIdleTimeout(pool.getIdleTimeout());
        result.setMaxLifetime(pool.getMaxLifetime());
        return result;
    }
}
