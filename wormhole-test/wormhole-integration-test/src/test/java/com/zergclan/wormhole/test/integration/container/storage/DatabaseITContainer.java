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

package com.zergclan.wormhole.test.integration.container.storage;

import com.zaxxer.hikari.HikariDataSource;
import com.zergclan.wormhole.test.integration.container.DockerITContainer;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Abstract database IT container.
 */
public abstract class DatabaseITContainer extends DockerITContainer {
    
    private final Map<String, DataSource> dataSources = new LinkedHashMap<>();
    
    public DatabaseITContainer(final String identifier, final String dockerImageName, final int port) {
        super(identifier, dockerImageName, port);
    }
    
    /**
     * Get {@link DataSource}.
     *
     * @param dataSourceName data source name
     * @return {@link DataSource}
     */
    public synchronized DataSource getDataSource(final String dataSourceName) {
        DataSource result = dataSources.get(dataSourceName);
        if (null == result) {
            result = createDataSource(dataSourceName);
            dataSources.put(dataSourceName, result);
        }
        return result;
    }
    
    private DataSource createDataSource(final String dataSourceName) {
        HikariDataSource result = new HikariDataSource();
        result.setDriverClassName(getDriverClassName());
        result.setJdbcUrl(getJdbcUrl(dataSourceName));
        result.setUsername(getUsername());
        result.setPassword(setPassword());
        result.setTransactionIsolation(getTransactionIsolation());
        return result;
    }
    
    protected abstract String getDriverClassName();
    
    protected abstract String getJdbcUrl(String dataSourceName);
    
    protected abstract String getUsername();
    
    protected abstract String setPassword();
    
    protected abstract String getTransactionIsolation();
}
