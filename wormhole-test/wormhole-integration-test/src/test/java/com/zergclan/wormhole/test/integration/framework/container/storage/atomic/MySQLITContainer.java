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

package com.zergclan.wormhole.test.integration.framework.container.storage.atomic;

import com.zergclan.wormhole.test.integration.framework.container.DockerContainerDefinition;
import com.zergclan.wormhole.test.integration.framework.container.storage.DatabaseITContainer;
import com.zergclan.wormhole.test.integration.framework.container.wait.ConnectionWaitStrategy;

import java.util.Collections;
import java.util.List;

/**
 * Database IT container of MySQL.
 */
public final class MySQLITContainer extends DatabaseITContainer {
    
    private static final int DEFAULT_PORT = 3306;
    
    private static final String DEFAULT_USER = "root";
    
    private static final String DEFAULT_PASSWORD = "root";
    
    private static final String DEFAULT_JDBC_URL_SUFFIX = "?useSSL=false&characterEncoding=utf-8&serverTimezone=UTC&useServerPrepStmts=true&useLocalSessionState=true";
    
    private static final String[] DEFAULT_COMMANDS = new String[] {"--default-authentication-plugin=mysql_native_password", "explicit_defaults_for_timestamp=true"};
    
    private static final String DEFAULT_TRANSACTION_ISOLATION = "TRANSACTION_REPEATABLE_READ";
    
    private static final List<String> DEFAULT_ENV = Collections.singletonList("LANG=C.UTF-8");
    
    private final String scenario;
    
    private final String[] commands;
    
    private final List<String> env;
    
    private final String url;
    
    public MySQLITContainer(final DockerContainerDefinition dockerDefinition) {
        super(dockerDefinition.getIdentifier(), dockerDefinition.getImageName(), dockerDefinition.getPortOrDefault(DEFAULT_PORT));
        scenario = dockerDefinition.getScenario();
        commands = null == dockerDefinition.getCommands() ? DEFAULT_COMMANDS : dockerDefinition.getCommands();
        env = dockerDefinition.getEnv().isEmpty() ? DEFAULT_ENV : dockerDefinition.getEnv();
        url = initUrl();
        setWaitStrategy(ConnectionWaitStrategy.buildJDBCConnectionWaitStrategy(url, DEFAULT_USER, DEFAULT_PASSWORD));
    }
    
    private String initUrl() {
        return "jdbc:mysql://localhost:" + getPort() + DEFAULT_JDBC_URL_SUFFIX;
    }
    
    private String initJdbcUrl(final String dataSourceName) {
        return url + "/" + dataSourceName + DEFAULT_JDBC_URL_SUFFIX;
    }
    
    @Override
    protected void configure() {
        withCommand(commands);
        setEnv(env);
        initDatabase(scenario, "mysql");
        super.configure();
    }
    
    @Override
    protected String getDriverClassName() {
        return null;
    }
    
    @Override
    protected String getJdbcUrl(final String dataSourceName) {
        return initJdbcUrl(dataSourceName);
    }
    
    @Override
    protected String getUsername() {
        return DEFAULT_USER;
    }
    
    @Override
    protected String setPassword() {
        return DEFAULT_PASSWORD;
    }
    
    @Override
    protected String getTransactionIsolation() {
        return DEFAULT_TRANSACTION_ISOLATION;
    }
}
