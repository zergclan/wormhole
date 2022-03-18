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

package com.zergclan.wormhole.initializer;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.core.api.metadata.DataSourceMetaData;
import com.zergclan.wormhole.core.config.PlanConfiguration;
import com.zergclan.wormhole.core.config.TaskConfiguration;
import com.zergclan.wormhole.core.metadata.plan.PlanMetaData;
import com.zergclan.wormhole.core.metadata.task.TaskMetaData;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Plan metadata initializer.
 */
public final class PlanMetadataInitializer {
    
    private final TaskMetadataInitializer taskMetadataInitializer = new TaskMetadataInitializer();
    
    /**
     * Create {@link PlanMetaData}.
     *
     * @param planIdentifier plan identifier
     * @param configuration {@link PlanConfiguration}
     * @param dataSources data sources metadata
     * @return {@link PlanMetaData}
     */
    public PlanMetaData init(final String planIdentifier, final PlanConfiguration configuration, final Map<String, DataSourceMetaData> dataSources) {
        PlanMetaData.ExecutionMode executionMode = PlanMetaData.ExecutionMode.valueOf(configuration.getMode().trim().toUpperCase());
        Map<String, TaskMetaData> tasks = createTasks(planIdentifier, configuration.getTasks(), dataSources);
        return new PlanMetaData(planIdentifier, executionMode, configuration.getCorn(), configuration.isAtomic(), tasks);
    }
    
    private Map<String, TaskMetaData> createTasks(final String planIdentifier, final Map<String, TaskConfiguration> configurations, final Map<String, DataSourceMetaData> dataSources) {
        Iterator<Map.Entry<String, TaskConfiguration>> iterator = configurations.entrySet().iterator();
        Map<String, TaskMetaData> result = new LinkedHashMap<>();
        while (iterator.hasNext()) {
            Map.Entry<String, TaskConfiguration> entry = iterator.next();
            String taskIdentifier = planIdentifier + MarkConstant.FORWARD_SLASH + entry.getKey();
            result.put(taskIdentifier, taskMetadataInitializer.init(taskIdentifier, entry.getValue(), dataSources));
        }
        return result;
    }
}
