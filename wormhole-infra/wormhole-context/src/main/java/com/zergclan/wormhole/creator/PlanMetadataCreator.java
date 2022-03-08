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

package com.zergclan.wormhole.creator;

import com.zergclan.wormhole.core.config.PlanConfiguration;
import com.zergclan.wormhole.core.config.TaskConfiguration;
import com.zergclan.wormhole.core.api.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.plan.ExecutionMode;
import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import com.zergclan.wormhole.core.metadata.task.TaskMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Metadata creator of {@link PlanMetadata}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlanMetadataCreator {
    
    /**
     * Create {@link PlanMetadata}.
     *
     * @param configuration {@link PlanConfiguration}
     * @param dataSources data sources metadata
     * @return {@link PlanMetadata}
     */
    public static PlanMetadata create(final PlanConfiguration configuration, final Map<String, DataSourceMetadata> dataSources) {
        String planName = configuration.getName();
        ExecutionMode executionMode = ExecutionMode.valueOf(configuration.getMode().trim().toUpperCase());
        String corn = configuration.getCorn();
        Map<String, TaskMetadata> tasks = createTasks(configuration.getTasks(), dataSources);
        return new PlanMetadata(planName, executionMode, corn, tasks);
    }

    private static Map<String, TaskMetadata> createTasks(final Map<String, TaskConfiguration> configurations, final Map<String, DataSourceMetadata> dataSources) {
        Iterator<Map.Entry<String, TaskConfiguration>> iterator = configurations.entrySet().iterator();
        Map<String, TaskMetadata> result = new LinkedHashMap<>();
        while (iterator.hasNext()) {
            Map.Entry<String, TaskConfiguration> entry = iterator.next();
            result.put(entry.getKey(), TaskMetadataCreator.create(entry.getValue(), dataSources));
        }
        return result;
    }
}
