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

import com.zergclan.wormhole.test.integration.framework.container.storage.StorageContainerDefinition;
import com.zergclan.wormhole.test.integration.framework.container.storage.StorageITContainer;
import org.testcontainers.containers.BindMode;

import java.util.Locale;

/**
 * Database IT container of MySQL.
 */
public final class MySQLITContainer extends StorageITContainer {
    
    private static final String DEFAULT_USER = "root";
    
    private static final String DEFAULT_PASSWORD = "root";
    
    private static final String DEFAULT_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    
    private static final String DEFAULT_JDBC_URL_SUFFIX = "?useSSL=false&characterEncoding=utf-8&serverTimezone=UTC&useServerPrepStmts=true&useLocalSessionState=true";
    
    private static final String DEFAULT_TRANSACTION_ISOLATION = "TRANSACTION_REPEATABLE_READ";
    
    public MySQLITContainer(final StorageContainerDefinition dockerDefinition) {
        super(dockerDefinition.getContainerIdentifier(), dockerDefinition.getDockerImageName(), dockerDefinition.getScenario(), dockerDefinition.getDatabaseType().getType(),
                dockerDefinition.getAssertPart().name().toLowerCase(Locale.ROOT));
    }
    
    @Override
    protected void configure() {
        addEnv("LANG", "C.UTF-8");
        addEnv("MYSQL_ROOT_PASSWORD", DEFAULT_PASSWORD);
        addEnv("MYSQL_ROOT_HOST", "%");
        withClasspathResourceMapping("/env/common/mysql/my.cnf", "/etc/mysql/my.cnf", BindMode.READ_ONLY);
        super.configure();
    }
    
    @Override
    protected String getDriverClassName() {
        return DEFAULT_DRIVER_CLASS_NAME;
    }
    
    @Override
    protected String getJdbcUrl(final String host, final int port, final String dataSourceName) {
        return "jdbc:mysql://" + host + ":" + port + "/" + dataSourceName + DEFAULT_JDBC_URL_SUFFIX;
    }
    
    @Override
    protected int getDefaultPort() {
        return 3306;
    }
    
    @Override
    protected String getUsername() {
        return DEFAULT_USER;
    }
    
    @Override
    protected String getPassword() {
        return DEFAULT_PASSWORD;
    }
    
    @Override
    protected String getTransactionIsolation() {
        return DEFAULT_TRANSACTION_ISOLATION;
    }
}
