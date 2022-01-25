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

package com.zergclan.wormhole.swapper;

import com.zergclan.wormhole.common.WormholeException;
import com.zergclan.wormhole.core.config.PlanConfiguration;
import com.zergclan.wormhole.core.config.TaskConfiguration;
import com.zergclan.wormhole.core.metadata.plan.ExecutionMode;
import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Plan metadata configuration swapper.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlanMetadataConfigurationSwapper {

    /**
     * Swap to {@link PlanMetadata}.
     *
     * @param configuration {@link PlanConfiguration}
     * @return {@link PlanMetadata}
     */
    public static PlanMetadata swapToMetadata(final PlanConfiguration configuration) {
        Optional<ExecutionMode> executionMode = ExecutionMode.getExecutionMode(configuration.getExecutionMode());
        if (executionMode.isPresent()) {
            PlanMetadata result = createPlanMetadata(configuration, executionMode.get());
            registerTasks(result, configuration);
            return result;
        }
        throw new WormholeException("error : execution mode code [%d] not find", configuration.getExecutionMode());
    }

    private static void registerTasks(final PlanMetadata planMetadata, final PlanConfiguration configuration) {
        for (Map.Entry<String, TaskConfiguration> entry : configuration.getTasks().entrySet()) {
            planMetadata.register(TaskMetadataConfigurationSwapper.swapToMetadata(entry.getValue()));
        }
    }

    private static PlanMetadata createPlanMetadata(final PlanConfiguration configuration, final ExecutionMode executionMode) {
        String code = configuration.getCode();
        String executionCorn = configuration.getExecutionCorn();
        LocalDateTime effectiveDate = configuration.getEffectiveDate();
        AtomicBoolean execute = new AtomicBoolean(configuration.isExecute());
        AtomicBoolean enable = new AtomicBoolean(configuration.isEnable());
        AtomicLong executionCount = new AtomicLong(configuration.getExecutionCount());
        return new PlanMetadata(code, executionMode, executionCorn, effectiveDate, execute, enable, executionCount);
    }
}
