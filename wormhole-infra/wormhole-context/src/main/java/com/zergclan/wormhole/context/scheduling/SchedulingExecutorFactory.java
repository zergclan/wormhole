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

package com.zergclan.wormhole.context.scheduling;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zergclan.wormhole.common.WormholeException;
import com.zergclan.wormhole.context.scheduling.plan.PlanSchedulingExecutor;
import com.zergclan.wormhole.context.scheduling.plan.PlanSchedulingTrigger;
import com.zergclan.wormhole.context.scheduling.task.TaskSchedulingExecutor;
import com.zergclan.wormhole.context.scheduling.task.TaskSchedulingTrigger;
import com.zergclan.wormhole.core.concurrent.ExecutorService;
import com.zergclan.wormhole.core.concurrent.ExecutorServiceFactory;
import com.zergclan.wormhole.extracter.Extractor;
import com.zergclan.wormhole.jdbc.JdbcTemplateFactory;
import com.zergclan.wormhole.loader.Loader;
import com.zergclan.wormhole.pipeline.data.DefaultDataGroupSwapper;
import com.zergclan.wormhole.reader.mysql.MySQLExtractor;
import com.zergclan.wormhole.writer.mysql.MySQLLoader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.LinkedList;

/**
 * {@link SchedulingExecutor} Factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SchedulingExecutorFactory {

    /**
     * The newly created {@link SchedulingExecutor} by {@link SchedulingTrigger}.
     *
     * @param trigger {@link SchedulingTrigger}
     * @return {@link SchedulingExecutor}
     */
    public static SchedulingExecutor createSchedulingExecutor(final SchedulingTrigger trigger) {
        if (trigger instanceof PlanSchedulingTrigger) {
            return createPlanSchedulingExecutor((PlanSchedulingTrigger) trigger);
        } else if (trigger instanceof TaskSchedulingTrigger) {
            return createTaskSchedulingExecutor((TaskSchedulingTrigger) trigger);
        }
        throw new WormholeException("error : create plan scheduling executor fail, code [%s]", trigger.getCode());
    }

    /**
     * The newly created {@link PlanSchedulingExecutor} by {@link PlanSchedulingTrigger}.
     *
     * @param trigger {@link PlanSchedulingTrigger}
     * @return {@link PlanSchedulingExecutor}
     */
    private static PlanSchedulingExecutor createPlanSchedulingExecutor(final PlanSchedulingTrigger trigger) {
        return new PlanSchedulingExecutor(new LinkedList<>());
    }

    /**
     * The newly created {@link TaskSchedulingExecutor} by {@link TaskSchedulingTrigger}.
     *
     * @param trigger {@link TaskSchedulingTrigger}
     * @return {@link TaskSchedulingExecutor}
     */
    private static TaskSchedulingExecutor createTaskSchedulingExecutor(final TaskSchedulingTrigger trigger) {
        Extractor extractor = createExtractor(trigger.getCode());
        Loader loader = createLoader(trigger.getCode());
        ExecutorService executorService = ExecutorServiceFactory.newFixedThreadExecutor(4, 8, trigger.getCode(), 256);
        DefaultDataGroupSwapper defaultDataGroupSwapper = new DefaultDataGroupSwapper();
        return new TaskSchedulingExecutor(1L, 2L, extractor, loader, executorService, defaultDataGroupSwapper);
    }

    private static Loader createLoader(final String code) {
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/source_ds?serverTimezone=UTC&useSSL=false";
        String username = "root";
        String password = "root";
        DataSource dataSource = createDataSource(driverClassName, jdbcUrl, username, password);
        JdbcTemplate jdbcTemplate = JdbcTemplateFactory.createJdbcTemplate(dataSource);
        return new MySQLLoader(jdbcTemplate);
    }
    
    private static Extractor createExtractor(final String code) {
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        String jdbcUrl = "jdbc:mysql://127.0.0.1:3307/target_ds?serverTimezone=UTC&useSSL=false";
        String username = "root";
        String password = "root";
        DataSource dataSource = createDataSource(driverClassName, jdbcUrl, username, password);
        JdbcTemplate jdbcTemplate = JdbcTemplateFactory.createJdbcTemplate(dataSource);
        return new MySQLExtractor(jdbcTemplate);
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
