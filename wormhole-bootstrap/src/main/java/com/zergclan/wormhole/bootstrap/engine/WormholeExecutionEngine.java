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

package com.zergclan.wormhole.bootstrap.engine;

import com.zergclan.wormhole.bootstrap.scheduling.plan.PlanTrigger;
import com.zergclan.wormhole.bootstrap.scheduling.plan.PlanTriggerManager;
import com.zergclan.wormhole.bootstrap.scheduling.plan.ScheduledPlanTrigger;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.config.core.WormholeConfiguration;
import com.zergclan.wormhole.metadata.api.MetaData;
import com.zergclan.wormhole.metadata.core.WormholeMetaData;
import com.zergclan.wormhole.metadata.core.initializer.WormholeMetadataInitializer;
import com.zergclan.wormhole.metadata.core.plan.PlanMetaData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.Map;

/**
 * Wormhole execution engine.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WormholeExecutionEngine {
    
    private static final WormholeExecutionEngine INSTANCE = new WormholeExecutionEngine();
    
    private final WormholeMetadataInitializer initializer = new WormholeMetadataInitializer();
    
    private final PlanTriggerManager planTriggerManager = new PlanTriggerManager();
    
    private volatile PlanExecutionEngine planExecutionEngine;
    
    private volatile EngineState engineState = EngineState.UNINITIALIZED;
    
    /**
     * Get instance.
     *
     * @param configuration {@link WormholeConfiguration}
     * @return {@link WormholeExecutionEngine}
     * @throws SQLException exception
     */
    public static synchronized WormholeExecutionEngine getInstance(final WormholeConfiguration configuration) throws SQLException {
        Validator.notNull(configuration, "error : wormhole execution engine new instance configuration can not be null");
        if (INSTANCE.isUninitialized()) {
            INSTANCE.init(configuration);
        }
        return INSTANCE;
    }
    
    private boolean isUninitialized() {
        return EngineState.UNINITIALIZED == engineState;
    }
    
    private void init(final WormholeConfiguration configuration) throws SQLException {
        WormholeMetaData wormholeMetadata = initializer.init(configuration);
        planExecutionEngine = createPlanExecutionEngine(wormholeMetadata);
        initPlanTriggerManager(wormholeMetadata.getPlans());
        engineState = EngineState.INITIALIZATION;
    }
    
    private PlanExecutionEngine createPlanExecutionEngine(final WormholeMetaData wormholeMetadata) {
        return new PlanExecutionEngine(wormholeMetadata);
    }
    
    private void initPlanTriggerManager(final Map<String, PlanMetaData> planMetaData) {
        planMetaData.values().forEach(planTriggerManager::register);
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
     * Register {@link MetaData}.
     *
     * @param metadata {@link MetaData}
     * @return is registered or not
     */
    public boolean register(final MetaData metadata) {
        return planExecutionEngine.register(metadata);
    }
    
    private enum EngineState {
    
        UNINITIALIZED, INITIALIZATION
    }
}
