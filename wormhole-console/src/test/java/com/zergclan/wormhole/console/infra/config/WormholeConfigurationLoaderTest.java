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

package com.zergclan.wormhole.console.infra.config;

import com.zergclan.wormhole.config.core.DataSourceConfiguration;
import com.zergclan.wormhole.config.core.PlanConfiguration;
import com.zergclan.wormhole.config.core.TaskConfiguration;
import com.zergclan.wormhole.config.core.WormholeConfiguration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class WormholeConfigurationLoaderTest {
    
    @Test
    @SneakyThrows(IOException.class)
    public void assertLoad() {
        WormholeConfiguration wormholeConfiguration = WormholeConfigurationLoader.load();
        Map<String, DataSourceConfiguration> dataSources = wormholeConfiguration.getDataSources();
        assertEquals(2, dataSources.size());
        DataSourceConfiguration sourceDataSourceConfiguration = dataSources.get("ds-source");
        assertSourceDataSource(sourceDataSourceConfiguration);
        DataSourceConfiguration targetDataSourceConfiguration = dataSources.get("ds-target");
        assertTargetDataSource(targetDataSourceConfiguration);
        Map<String, PlanConfiguration> plans = wormholeConfiguration.getPlans();
        assertEquals(1, plans.size());
        assertPlan(plans.get("test-plan-one"));
    }
    
    private void assertSourceDataSource(final DataSourceConfiguration dataSource) {
        assertNotNull(dataSource);
        assertEquals("ds-source", dataSource.getName());
        assertEquals("MySQL", dataSource.getType());
        assertEquals("127.0.0.1", dataSource.getHost());
        assertEquals(3306, dataSource.getPort());
        assertEquals("root", dataSource.getUsername());
        assertEquals("root", dataSource.getPassword());
        assertEquals("ds_source", dataSource.getCatalog());
    }
    
    private void assertTargetDataSource(final DataSourceConfiguration dataSource) {
        assertNotNull(dataSource);
        assertEquals("ds-target", dataSource.getName());
        assertEquals("MySQL", dataSource.getType());
        assertEquals("127.0.0.1", dataSource.getHost());
        assertEquals(3307, dataSource.getPort());
        assertEquals("root", dataSource.getUsername());
        assertEquals("root", dataSource.getPassword());
        assertEquals("ds_target", dataSource.getCatalog());
    }
    
    private void assertPlan(final PlanConfiguration plan) {
        assertNotNull(plan);
        assertEquals("SCHEDULED", plan.getMode());
        assertEquals("/ 0 0 2 * * ?", plan.getExpression());
        assertTrue(plan.isAtomic());
        Map<String, TaskConfiguration> tasks = plan.getTasks();
        assertEquals(2, tasks.size());
        assertTaskOne(tasks.get("task_one"));
        assertTaskTwo(tasks.get("task_two"));
    }
    
    private void assertTaskOne(final TaskConfiguration taskOne) {
        assertEquals(0, taskOne.getOrder());
        assertEquals(1000, taskOne.getBatchSize());
    }
    
    private void assertTaskTwo(final TaskConfiguration taskTwo) {
        assertEquals(1, taskTwo.getOrder());
        assertEquals(1000, taskTwo.getBatchSize());
    }
}
