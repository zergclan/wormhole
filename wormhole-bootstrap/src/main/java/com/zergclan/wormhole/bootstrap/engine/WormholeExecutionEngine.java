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
import com.zergclan.wormhole.common.WormholeEventListener;
import com.zergclan.wormhole.common.WormholeMetaData;
import com.zergclan.wormhole.common.data.node.PatternedDataTime;
import com.zergclan.wormhole.common.eventbus.WormholeEventBus;
import com.zergclan.wormhole.common.metadata.WormholeMetaDataContext;
import com.zergclan.wormhole.common.metadata.authorization.AuthenticationResult;
import com.zergclan.wormhole.common.metadata.builder.WormholeMetaDataContextBuilder;
import com.zergclan.wormhole.common.metadata.plan.PlanMetaData;
import com.zergclan.wormhole.common.metadata.plan.TaskMetaData;
import com.zergclan.wormhole.tool.util.DateUtil;
import com.zergclan.wormhole.tool.util.Validator;
import com.zergclan.wormhole.common.configuration.WormholeConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Wormhole execution engine.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WormholeExecutionEngine {
    
    private static final WormholeExecutionEngine INSTANCE = new WormholeExecutionEngine();
    
    private static final AtomicReference<EngineState> STATE = new AtomicReference<>(EngineState.UNINITIALIZED);
    
    private static final WormholeMetaDataContextBuilder INITIALIZER = new WormholeMetaDataContextBuilder();
    
    private static final PlanTriggerManager PLAN_TRIGGER_MANAGER = new PlanTriggerManager();
    
    private AuthorizationExecutionEngine authorizationExecutionEngine;
    
    private PlanExecutionEngine planExecutionEngine;
    
    /**
     * Get instance.
     *
     * @param configuration {@link WormholeConfiguration}
     * @return {@link WormholeExecutionEngine}
     * @throws SQLException exception
     */
    public static synchronized WormholeExecutionEngine getInstance(final WormholeConfiguration configuration) throws SQLException {
        Validator.notNull(configuration, "error : wormhole execution engine new instance configuration can not be null");
        if (!isInitialization()) {
            INSTANCE.init(configuration);
        }
        return INSTANCE;
    }
    
    private static boolean isInitialization() {
        return EngineState.INITIALIZATION == STATE.get();
    }
    
    private void init(final WormholeConfiguration configuration) throws SQLException {
        WormholeMetaDataContext wormholeMetadataContext = INITIALIZER.build(configuration);
        authorizationExecutionEngine = new AuthorizationExecutionEngine(wormholeMetadataContext.getAuthorization());
        planExecutionEngine = new PlanExecutionEngine(wormholeMetadataContext);
        WormholeEventBus.register(planExecutionEngine);
        initPlanTriggerManager(wormholeMetadataContext.getPlans());
    }
    
    private void initPlanTriggerManager(final Map<String, PlanMetaData> planMetaData) {
        planMetaData.values().forEach(PLAN_TRIGGER_MANAGER::register);
    }
    
    /**
     * Execute executable plan.
     */
    public void execute() {
        for (;;) {
            if (isInitialization()) {
                PLAN_TRIGGER_MANAGER.getExecutableTrigger().ifPresent(this::handleExecutableTrigger);
            }
        }
    }

    private void handleExecutableTrigger(final PlanTrigger planTrigger) {
        planExecutionEngine.execute(planTrigger);
        if (planTrigger instanceof ScheduledPlanTrigger) {
            PLAN_TRIGGER_MANAGER.reRegister((ScheduledPlanTrigger) planTrigger);
        }
    }
    
    /**
     * Register {@link WormholeMetaData}.
     *
     * @param metadata {@link WormholeMetaData}
     * @return is registered or not
     */
    public synchronized boolean register(final WormholeMetaData metadata) {
        return planExecutionEngine.register(metadata);
    }
    
    /**
     * Register {@link WormholeEventListener}.
     *
     * @param listeners {@link WormholeEventListener}
     */
    @SuppressWarnings("all")
    public void register(final Map<String, WormholeEventListener> listeners) {
        listeners.values().forEach(WormholeEventBus::register);
    }
    
    /**
     * Started.
     *
     * @return is started or not
     */
    public boolean started() {
        boolean result = STATE.compareAndSet(EngineState.UNINITIALIZED, EngineState.INITIALIZATION);
        if (result) {
            log.info("Wormhole execution engine initialization completed, started successfully !!");
        }
        return result;
    }
    
    /**
     * Register {@link WormholeMetaData}.
     *
     * @param planIdentifier plan identifier
     * @return is registered or not
     */
    public synchronized boolean trigger(final String planIdentifier) {
        if (isInitialization()) {
            Optional<PlanMetaData> plan = planExecutionEngine.getPlan(planIdentifier);
            if (plan.isPresent()) {
                return PLAN_TRIGGER_MANAGER.register(initTrigger(plan.get()));
            }
        }
        return false;
    }
    
    private PlanMetaData initTrigger(final PlanMetaData planMetaData) {
        String identifier = planMetaData.getIdentifier();
        PlanMetaData.ExecutionMode mode = PlanMetaData.ExecutionMode.ONE_OFF;
        String expression = DateUtil.format(DateUtil.getCurrentNextSeconds(10), PatternedDataTime.DatePattern.STANDARD.getPattern());
        boolean atomic = planMetaData.isAtomic();
        Map<String, TaskMetaData> tasks = planMetaData.getTasks();
        return new PlanMetaData(identifier, mode, expression, atomic, tasks);
    }
    
    /**
     * Authenticate.
     *
     * @param username username
     * @param password password
     * @return {@link AuthenticationResult}
     */
    public AuthenticationResult authenticate(final String username, final String password) {
        return authorizationExecutionEngine.authenticate(username, password);
    }
    
    private enum EngineState {
        
        UNINITIALIZED, INITIALIZATION
    }
}
