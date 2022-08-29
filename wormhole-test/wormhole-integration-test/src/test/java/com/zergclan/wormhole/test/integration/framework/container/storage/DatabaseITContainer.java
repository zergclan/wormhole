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

package com.zergclan.wormhole.test.integration.framework.container.storage;

import com.zaxxer.hikari.HikariDataSource;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.test.integration.framework.container.wait.ConnectionWaitStrategy;
import com.zergclan.wormhole.test.integration.framework.util.PathGenerator;
import com.zergclan.wormhole.test.integration.framework.container.DockerITContainer;
import com.zergclan.wormhole.test.integration.framework.util.URLGenerator;
import org.testcontainers.containers.BindMode;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Abstract database IT container.
 */
public abstract class DatabaseITContainer extends DockerITContainer {
    
    private static final String CONTAINER_PATH = "/docker-entrypoint-initdb.d/";
    
    private final Map<String, DataSource> dataSources = new LinkedHashMap<>();
    
    public DatabaseITContainer(final String identifier, final String scenario, final String dockerImageName, final int port) {
        super(identifier, scenario, dockerImageName, port);
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
    
    @Override
    protected void configure() {
        withClasspathResourceMapping(PathGenerator.generateInitSqlPath(getScenario(), getDatabaseType()), CONTAINER_PATH, BindMode.READ_ONLY);
        withExposedPorts(getPort());
        setWaitStrategy(new ConnectionWaitStrategy(() -> DriverManager.getConnection(URLGenerator.generateJDBCUrl(getDatabaseType(), getFirstMappedPort()), getUsername(), getPassword())));
    }
    
    private DataSource createDataSource(final String dataSourceName) {
        HikariDataSource result = new HikariDataSource();
        result.setDriverClassName(getDriverClassName());
        String jdbcUrl = getJdbcUrl(getHost(), getMappedPort(getPort()), dataSourceName);
        result.setJdbcUrl(jdbcUrl);
        result.setUsername(getUsername());
        result.setPassword(getPassword());
        result.setTransactionIsolation(getTransactionIsolation());
        result.setPoolName(getPoolName(dataSourceName));
        return result;
    }
    
    private String getPoolName(final String dataSourceName) {
        return getDatabaseType() + MarkConstant.HYPHEN + getScenario() + MarkConstant.HYPHEN + dataSourceName;
    }
    
    protected abstract String getDatabaseType();
    
    protected abstract String getDriverClassName();
    
    protected abstract String getJdbcUrl(String host, int port, String dataSourceName);
    
    protected abstract String getUsername();
    
    protected abstract String getPassword();
    
    protected abstract String getTransactionIsolation();
}
