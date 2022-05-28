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

package com.zergclan.wormhole.bootstrap.context;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zergclan.wormhole.bootstrap.scheduling.ExecutionState;
import com.zergclan.wormhole.bootstrap.scheduling.event.PlanExecutionEvent;
import com.zergclan.wormhole.bootstrap.scheduling.event.TaskCompletedEvent;
import com.zergclan.wormhole.bootstrap.scheduling.plan.PlanTrigger;
import com.zergclan.wormhole.bus.disruptor.event.ExecutionEvent;
import com.zergclan.wormhole.bus.memory.WormholeEventBus;
import com.zergclan.wormhole.common.exception.WormholeException;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.WormholeMetaData;
import com.zergclan.wormhole.metadata.core.catched.CachedPlanMetaData;
import com.zergclan.wormhole.metadata.core.plan.PlanMetaData;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Plan context.
 */
public final class PlanContext {
    
    private final Cache<String, CachedPlanMetaData> cachedMetadata = Caffeine.newBuilder().initialCapacity(1).maximumSize(100).expireAfterWrite(1, TimeUnit.DAYS).build();
    
    /**
     * Is executing plan.
     *
     * @param planIdentifier plan identifier
     * @return is executing or not
     */
    public boolean isExecuting(final String planIdentifier) {
        return cachedMetadata.asMap().containsKey(planIdentifier);
    }
    
    /**
     * Create {@link CachedPlanMetaData}.
     *
     * @param wormholeMetaData {@link WormholeMetaData}
     * @param planTrigger plan identifier
     * @return {@link CachedPlanMetaData}
     * @exception SQLException SQL exception
     */
    public synchronized Optional<CachedPlanMetaData> cachedMetadata(final WormholeMetaData wormholeMetaData, final PlanTrigger planTrigger) throws SQLException {
        String planIdentifier = planTrigger.getPlanIdentifier();
        if (isExecuting(planIdentifier)) {
            return Optional.empty();
        }
        Optional<PlanMetaData> plan = wormholeMetaData.getPlan(planIdentifier);
        if (plan.isPresent()) {
            return Optional.of(cachedMetaData(plan.get(), wormholeMetaData.getDataSources()));
        }
        throw new WormholeException("error: can not find plan meta data named: [%s]", planIdentifier);
    }
    
    private CachedPlanMetaData cachedMetaData(final PlanMetaData planMetaData, final Map<String, DataSourceMetaData> dataSources) throws SQLException {
        CachedPlanMetaData planMetadata = CachedPlanMetaData.builder(planMetaData, dataSources);
        cachedMetadata.put(planMetadata.getIdentifier(), planMetadata);
        return planMetadata;
    }
    
    /**
     * Handle trigger.
     *
     * @param planTrigger {@link PlanTrigger}
     */
    public void handleTrigger(final PlanTrigger planTrigger) {
        String planIdentifier = planTrigger.getPlanIdentifier();
        String planTriggerIdentifier = planTrigger.getIdentifier();
        PlanExecutionEvent event = PlanExecutionEvent.buildNewStateEvent(planIdentifier, planTriggerIdentifier, ExecutionState.SUCCESS);
        WormholeEventBus.post(event);
    }
    
    /**
     * Handle cached failed.
     *
     * @param planTrigger {@link PlanTrigger}
     * @param state state
     */
    public void handleCachedEvent(final PlanTrigger planTrigger, final boolean state) {
        ExecutionState executionState = state ? ExecutionState.SUCCESS : ExecutionState.FAILED;
        String planIdentifier = planTrigger.getPlanIdentifier();
        String planTriggerIdentifier = planTrigger.getIdentifier();
        PlanExecutionEvent event = PlanExecutionEvent.buildReadyStateEvent(planIdentifier, planTriggerIdentifier, executionState);
        WormholeEventBus.post(event);
    }
    
    /**
     * Handle task completed event.
     *
     * @param event {@link ExecutionEvent}
     */
    public void handleTaskCompletedEvent(final TaskCompletedEvent event) {
        String planIdentifier = event.getPlanIdentifier();
        CachedPlanMetaData cachedPlanMetaData = cachedMetadata.asMap().get(planIdentifier);
        int row = cachedPlanMetaData.taskCompleted(event.getTaskIdentifier());
        if (0 == row) {
            cachedMetadata.asMap().remove(planIdentifier, cachedPlanMetaData);
        }
        PlanExecutionEvent planEvent = PlanExecutionEvent.buildCompleteStepEvent(cachedPlanMetaData.getPlanBatch(), ExecutionState.SUCCESS);
        WormholeEventBus.post(planEvent);
    }
    
    /**
     * Handle execute SQL exception.
     *
     * @param exception {@link SQLException}
     */
    public void handleExecuteException(final SQLException exception) {
        throw new WormholeException("error: can not cached plan meta data by SQL exception", exception);
    }
}
