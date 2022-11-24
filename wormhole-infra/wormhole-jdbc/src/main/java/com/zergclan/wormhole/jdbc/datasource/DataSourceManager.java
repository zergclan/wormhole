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

import com.zergclan.wormhole.common.metadata.datasource.DataSourcePoolMetadata;
import com.zergclan.wormhole.common.metadata.datasource.WormholeDataSourceMetaData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Data source manager.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataSourceManager {
    
    private static final Map<String, DataSource> SOURCES = new LinkedHashMap<>();
    
    /**
     * Get {@link DataSource}.
     *
     * @param dataSourceMetadata {@link WormholeDataSourceMetaData}
     * @return {@link DataSource}
     */
    public static synchronized DataSource getDataSource(final WormholeDataSourceMetaData dataSourceMetadata) {
        String identifier = dataSourceMetadata.getIdentifier();
        DataSource result = SOURCES.get(identifier);
        if (null == result) {
            result = DataSourceCreator.create(initConfigProperties(dataSourceMetadata));
            SOURCES.put(identifier, result);
        }
        return result;
    }
    
    private static Properties initConfigProperties(final WormholeDataSourceMetaData dataSourceMetadata) {
        Properties result = new Properties();
        result.put("driverClassName", dataSourceMetadata.getDriverClassName());
        result.put("jdbcUrl", dataSourceMetadata.getJdbcUrl());
        result.put("username", dataSourceMetadata.getUsername());
        result.put("password", dataSourceMetadata.getPassword());
        DataSourcePoolMetadata pool = dataSourceMetadata.getPool();
        result.put("poolName", pool.getPoolName());
        result.put("minimumIdle", pool.getMinPoolSize());
        result.put("maxPoolSize", pool.getMaxPoolSize());
        result.put("connectionTimeout", pool.getConnectionTimeout());
        result.put("idleTimeout", pool.getIdleTimeout());
        result.put("maxLifetime", pool.getMaxLifetime());
        return result;
    }
}
