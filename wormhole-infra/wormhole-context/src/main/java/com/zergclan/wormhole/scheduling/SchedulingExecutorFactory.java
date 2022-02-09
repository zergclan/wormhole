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

package com.zergclan.wormhole.scheduling;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zergclan.wormhole.JdbcTemplateManager;
import com.zergclan.wormhole.common.WormholeException;
import com.zergclan.wormhole.core.concurrent.ExecutorService;
import com.zergclan.wormhole.core.concurrent.ExecutorServiceFactory;
import com.zergclan.wormhole.core.metadata.Metadata;
import com.zergclan.wormhole.core.metadata.catched.CachedPlanMetadata;
import com.zergclan.wormhole.core.metadata.resource.dialect.MySQLDataSourceMetadata;
import com.zergclan.wormhole.extracter.Extractor;
import com.zergclan.wormhole.loader.Loader;
import com.zergclan.wormhole.reader.mysql.MySQLExtractor;
import com.zergclan.wormhole.writer.mysql.MySQLLoader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

/**
 * {@link SchedulingExecutor} factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SchedulingExecutorFactory {

    /**
     * The newly created {@link SchedulingExecutor} by {@link Metadata}.
     *
     * @param metadata {@link Metadata}
     * @return {@link SchedulingExecutor}
     */
    public static SchedulingExecutor createSchedulingExecutor(final Metadata metadata) {
        if (metadata instanceof CachedPlanMetadata) {
            return createPlanSchedulingExecutor((CachedPlanMetadata) metadata);
        }
        throw new WormholeException("error : create plan scheduling executor fail, identifier [%s]", metadata.getIdentifier());
    }

    /**
     * The newly created {@link PlanSchedulingExecutor} by {@link CachedPlanMetadata}.
     *
     * @param cachedPlanMetadata {@link CachedPlanMetadata}
     * @return {@link PlanSchedulingExecutor}
     */
    private static PlanSchedulingExecutor createPlanSchedulingExecutor(final CachedPlanMetadata cachedPlanMetadata) {
        Collection<TaskSchedulingExecutor> taskSchedulingExecutors = new LinkedList<>();
        taskSchedulingExecutors.add(createTaskSchedulingExecutor());
        return new PlanSchedulingExecutor(taskSchedulingExecutors);
    }

    /**
     * The newly created {@link TaskSchedulingExecutor}.
     *
     * @return {@link TaskSchedulingExecutor}
     */
    private static TaskSchedulingExecutor createTaskSchedulingExecutor() {
        Extractor extractor = createExtractor();
        Loader loader = createLoader();
        ExecutorService executorService = ExecutorServiceFactory.newFixedThreadExecutor(4, 8, "task", 256);
        return new TaskSchedulingExecutor(1L, 2L, extractor, loader, executorService);
    }

    private static Extractor createExtractor() {
        int port = 3306;
        String host = "127.0.0.1";
        String username = "root";
        String password = "123456";
        String catalog = "source_db";
        Properties properties = new Properties();
        properties.setProperty("serverTimezone", "UTC");
        properties.setProperty("useSSL", "false");
        MySQLDataSourceMetadata dataSourceMetadata = new MySQLDataSourceMetadata(host, port, username, password, catalog, properties);
        return new MySQLExtractor(JdbcTemplateManager.register(dataSourceMetadata));
    }

    private static Loader createLoader() {
        int port = 3307;
        String host = "127.0.0.1";
        String username = "root";
        String password = "123456";
        String catalog = "target_db";
        Properties properties = new Properties();
        properties.setProperty("serverTimezone", "UTC");
        properties.setProperty("useSSL", "false");
        MySQLDataSourceMetadata dataSourceMetadata = new MySQLDataSourceMetadata(host, port, username, password, catalog, properties);
        return new MySQLLoader(JdbcTemplateManager.register(dataSourceMetadata));
    }

    private static DataSource createDataSource(final String driverClassName, final String jdbcUrl, final String username, final String password) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }
}
