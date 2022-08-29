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
import com.zergclan.wormhole.common.exception.WormholeException;
import com.zergclan.wormhole.common.metadata.catched.CachedPlanMetaData;
import com.zergclan.wormhole.common.metadata.datasource.DataSourceMetaData;
import com.zergclan.wormhole.common.metadata.WormholeMetaData;
import com.zergclan.wormhole.common.metadata.plan.PlanMetaData;
import com.zergclan.wormhole.bootstrap.scheduling.ExecutionState;
import com.zergclan.wormhole.bootstrap.scheduling.event.PlanCompletedEvent;
import com.zergclan.wormhole.bootstrap.scheduling.event.PlanExecutionEvent;
import com.zergclan.wormhole.bootstrap.scheduling.event.TaskCompletedEvent;
import com.zergclan.wormhole.bootstrap.scheduling.plan.PlanTrigger;
import com.zergclan.wormhole.bus.memory.WormholeEventBus;

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
     * @param planBatch plan batch
     * @return {@link CachedPlanMetaData}
     * @exception SQLException SQL exception
     */
    public synchronized Optional<CachedPlanMetaData> cachedMetadata(final long planBatch, final WormholeMetaData wormholeMetaData, final PlanTrigger planTrigger) throws SQLException {
        String planIdentifier = planTrigger.getPlanIdentifier();
        if (isExecuting(planIdentifier)) {
            return Optional.empty();
        }
        Optional<PlanMetaData> plan = wormholeMetaData.getPlan(planIdentifier);
        if (plan.isPresent()) {
            return Optional.of(cachedMetaData(planBatch, plan.get(), wormholeMetaData.getDataSources()));
        }
        throw new WormholeException("error: can not find plan meta data named: [%s]", planIdentifier);
    }
    
    private CachedPlanMetaData cachedMetaData(final long planBatch, final PlanMetaData planMetaData, final Map<String, DataSourceMetaData> dataSources) throws SQLException {
        CachedPlanMetaData planMetadata = CachedPlanMetaData.builder(planBatch, planMetaData, dataSources);
        cachedMetadata.put(planMetadata.getPlanIdentifier(), planMetadata);
        return planMetadata;
    }
    
    /**
     * Handle trigger.
     *
     * @param planBatch plan batch
     * @param planTrigger {@link PlanTrigger}
     */
    public void handleTrigger(final long planBatch, final PlanTrigger planTrigger) {
        WormholeEventBus.post(PlanExecutionEvent.buildNewEvent(planTrigger.getPlanIdentifier(), planTrigger.getIdentifier(), planBatch));
    }
    
    /**
     * Handle cached failed.
     *
     * @param planBatch plan batch
     * @param executionState {@link ExecutionState}
     */
    public void handleCachedEvent(final long planBatch, final ExecutionState executionState) {
        WormholeEventBus.post(PlanExecutionEvent.buildReadyEvent(planBatch, executionState));
    }
    
    /**
     * Handle completed event.
     *
     * @param event {@link TaskCompletedEvent}
     */
    public void handleCompletedEvent(final TaskCompletedEvent event) {
        String planIdentifier = event.getPlanIdentifier();
        CachedPlanMetaData cachedPlanMetaData = cachedMetadata.asMap().get(planIdentifier);
        int row = cachedPlanMetaData.taskCompleted(event.getTaskIdentifier());
        if (0 == row) {
            cachedMetadata.asMap().remove(planIdentifier, cachedPlanMetaData);
            WormholeEventBus.post(PlanExecutionEvent.buildCompleteEvent(cachedPlanMetaData.getPlanBatch(), ExecutionState.SUCCESS));
        }
    }
    
    /**
     * Handle completed event.
     *
     * @param event {@link PlanCompletedEvent}
     */
    public void handleCompletedEvent(final PlanCompletedEvent event) {
        cachedMetadata.asMap().remove(event.getPlanIdentifier());
    }
    
    /**
     * Handle plan execute error.
     *
     * @param planBatch plan batch
     */
    public void handlePlanExecuteError(final long planBatch) {
        WormholeEventBus.post(PlanExecutionEvent.buildCompleteEvent(planBatch, ExecutionState.ERROR));
    }
}
