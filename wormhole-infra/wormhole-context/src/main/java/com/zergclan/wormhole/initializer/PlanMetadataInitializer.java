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
import com.zergclan.wormhole.core.api.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.config.PlanConfiguration;
import com.zergclan.wormhole.core.config.TaskConfiguration;
import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import com.zergclan.wormhole.core.metadata.task.TaskMetadata;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Plan metadata initializer.
 */
public final class PlanMetadataInitializer {
    
    private final TaskMetadataInitializer taskMetadataInitializer = new TaskMetadataInitializer();
    
    /**
     * Create {@link PlanMetadata}.
     *
     * @param planIdentifier plan identifier
     * @param configuration {@link PlanConfiguration}
     * @param dataSources data sources metadata
     * @return {@link PlanMetadata}
     */
    public PlanMetadata init(final String planIdentifier, final PlanConfiguration configuration, final Map<String, DataSourceMetadata> dataSources) {
        PlanMetadata.ExecutionMode executionMode = PlanMetadata.ExecutionMode.valueOf(configuration.getMode().trim().toUpperCase());
        Map<String, TaskMetadata> tasks = createTasks(planIdentifier, configuration.getTasks(), dataSources);
        return new PlanMetadata(planIdentifier, executionMode, configuration.getCorn(), configuration.isAtomic(), tasks);
    }
    
    private Map<String, TaskMetadata> createTasks(final String planIdentifier, final Map<String, TaskConfiguration> configurations, final Map<String, DataSourceMetadata> dataSources) {
        Iterator<Map.Entry<String, TaskConfiguration>> iterator = configurations.entrySet().iterator();
        Map<String, TaskMetadata> result = new LinkedHashMap<>();
        while (iterator.hasNext()) {
            Map.Entry<String, TaskConfiguration> entry = iterator.next();
            String taskIdentifier = planIdentifier + MarkConstant.FORWARD_SLASH + entry.getKey();
            result.put(taskIdentifier, taskMetadataInitializer.init(taskIdentifier, entry.getValue(), dataSources));
        }
        return result;
    }
}
