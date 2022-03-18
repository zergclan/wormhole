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

package com.zergclan.wormhole.engine;

import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.core.config.WormholeConfiguration;
import com.zergclan.wormhole.core.api.metadata.Metadata;
import com.zergclan.wormhole.core.metadata.WormholeMetadata;
import com.zergclan.wormhole.initializer.WormholeMetadataInitializer;
import com.zergclan.wormhole.scheduling.Trigger;
import com.zergclan.wormhole.scheduling.plan.PlanTrigger;
import com.zergclan.wormhole.scheduling.plan.PlanTriggerManager;
import com.zergclan.wormhole.scheduling.plan.ScheduledPlanTrigger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.SQLException;

/**
 * Wormhole execution engine.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WormholeExecutionEngine {
    
    private static final WormholeExecutionEngine INSTANCE = new WormholeExecutionEngine();
    
    private final WormholeMetadataInitializer initializer = new WormholeMetadataInitializer();
    
    private final PlanTriggerManager planTriggerManager = new PlanTriggerManager();
    
    private volatile PlanExecutionEngine planExecutionEngine;
    
    /**
     * Get instance.
     *
     * @param configuration {@link WormholeConfiguration}
     * @return {@link WormholeExecutionEngine}
     * @throws SQLException exception
     */
    public static WormholeExecutionEngine getInstance(final WormholeConfiguration configuration) throws SQLException {
        Validator.notNull(configuration, "error : wormhole execution engine new instance configuration can not be null");
        INSTANCE.init(configuration);
        return INSTANCE;
    }
    
    private void init(final WormholeConfiguration configuration) throws SQLException {
        WormholeMetadata wormholeMetadata = initializer.init(configuration);
        planExecutionEngine = createPlanExecutionEngine(wormholeMetadata);
    }
    
    private PlanExecutionEngine createPlanExecutionEngine(final WormholeMetadata wormholeMetadata) {
        // TODO create plan execution engine
        return new PlanExecutionEngine(wormholeMetadata);
    }
    
    /**
     * Execute executable plan.
     */
    public void execute() {
        planTriggerManager.getExecutableTrigger().ifPresent(this::handleExecutableTrigger);
    }

    private void handleExecutableTrigger(final PlanTrigger planTrigger) {
        planExecutionEngine.execute(planTrigger);
        if (planTrigger instanceof ScheduledPlanTrigger) {
            planTriggerManager.reRegister((ScheduledPlanTrigger) planTrigger);
        }
    }
    
    /**
     * Register {@link Metadata}.
     *
     * @param metadata {@link Metadata}
     * @return is registered or not
     */
    public boolean register(final Metadata metadata) {
        return planExecutionEngine.register(metadata);
    }
    
    private void sendRepeatedEvent(final Trigger trigger) {
        // TODO send execute plan repeated event
        System.out.printf("error : repeated event for plan [%s]", trigger);
    }
}
