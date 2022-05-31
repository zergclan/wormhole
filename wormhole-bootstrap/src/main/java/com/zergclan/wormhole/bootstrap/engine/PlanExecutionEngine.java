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

import com.google.common.eventbus.Subscribe;
import com.zergclan.wormhole.bootstrap.context.PlanContext;
import com.zergclan.wormhole.bootstrap.scheduling.ExecutionState;
import com.zergclan.wormhole.bootstrap.scheduling.event.PlanCompletedEvent;
import com.zergclan.wormhole.bootstrap.scheduling.event.TaskCompletedEvent;
import com.zergclan.wormhole.bootstrap.scheduling.plan.PlanExecutor;
import com.zergclan.wormhole.bootstrap.scheduling.plan.PlanExecutorFactory;
import com.zergclan.wormhole.bootstrap.scheduling.plan.PlanTrigger;
import com.zergclan.wormhole.bus.api.EventListener;
import com.zergclan.wormhole.common.SequenceGenerator;
import com.zergclan.wormhole.metadata.api.MetaData;
import com.zergclan.wormhole.metadata.core.WormholeMetaData;
import com.zergclan.wormhole.metadata.core.catched.CachedPlanMetaData;
import com.zergclan.wormhole.metadata.core.plan.PlanMetaData;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Plan execution engine.
 */
@RequiredArgsConstructor
public final class PlanExecutionEngine implements EventListener<TaskCompletedEvent> {
    
    private final PlanContext planContext = new PlanContext();
    
    private final WormholeMetaData wormholeMetadata;
    
    /**
     * Register {@link MetaData}.
     *
     * @param metadata {@link MetaData}
     * @return is register or not
     */
    public boolean register(final MetaData metadata) {
        return wormholeMetadata.register(metadata);
    }
    
    /**
     * Get {@link PlanMetaData}.
     *
     * @param planIdentifier plan identifier
     * @return {@link PlanMetaData}
     */
    public Optional<PlanMetaData> getPlan(final String planIdentifier) {
        return wormholeMetadata.getPlan(planIdentifier);
    }
    
    /**
     * Execute by plan trigger.
     *
     * @param planTrigger {@link PlanTrigger}
     */
    public void execute(final PlanTrigger planTrigger) {
        long planBatch = SequenceGenerator.generateId();
        planContext.handleTrigger(planBatch, planTrigger);
        try {
            Optional<CachedPlanMetaData> cachedPlanMetadata = planContext.cachedMetadata(planBatch, wormholeMetadata, planTrigger);
            if (!cachedPlanMetadata.isPresent()) {
                planContext.handleCachedEvent(planBatch, ExecutionState.FAILED);
                return;
            }
            planContext.handleCachedEvent(planBatch, ExecutionState.SUCCESS);
            PlanExecutor planExecutor = PlanExecutorFactory.create(cachedPlanMetadata.get());
            planExecutor.execute();
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            planContext.handlePlanExecuteError(planBatch);
            ex.printStackTrace();
        }
    }
    
    @Subscribe
    @Override
    public void onEvent(final TaskCompletedEvent event) {
        planContext.handleCompletedEvent(event);
    }
    
    /**
     * On event.
     *
     * @param event {@link PlanCompletedEvent}
     */
    @Subscribe
    public void onEvent(final PlanCompletedEvent event) {
        planContext.handleCompletedEvent(event);
    }
}
