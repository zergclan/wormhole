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

package com.zergclan.wormhole.core.creator;

import com.zergclan.wormhole.common.WormholeException;
import com.zergclan.wormhole.core.config.PlanConfiguration;
import com.zergclan.wormhole.core.config.TaskConfiguration;
import com.zergclan.wormhole.core.metadata.plan.ExecutionMode;
import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import com.zergclan.wormhole.core.metadata.plan.TaskMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Metadata creator of {@link PlanMetadata}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlanMetadataCreator {
    
    /**
     * Create {@link PlanMetadata}.
     *
     * @param configuration {@link PlanConfiguration}
     * @return {@link PlanMetadata}
     */
    public static PlanMetadata create(final PlanConfiguration configuration) {
        String mode = configuration.getMode();
        Optional<ExecutionMode> executionMode = ExecutionMode.getExecutionMode(mode);
        if (executionMode.isPresent()) {
            return new PlanMetadata(configuration.getName(), executionMode.get(), configuration.getCorn(), createTasks(configuration.getTasks()));
        }
        throw new WormholeException("error : execution mode code [%d] not find", mode);
    }
    
    private static Map<String, TaskMetadata> createTasks(final Map<String, TaskConfiguration> configurations) {
        Iterator<Map.Entry<String, TaskConfiguration>> iterator = configurations.entrySet().iterator();
        Map<String, TaskMetadata> result = new LinkedHashMap<>();
        TaskMetadata taskMetadata;
        while (iterator.hasNext()) {
            taskMetadata = TaskMetadataCreator.create(iterator.next().getValue());
            result.put(taskMetadata.getIdentifier(), taskMetadata);
        }
        return result;
    }
}
