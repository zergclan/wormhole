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

import com.zergclan.wormhole.core.metadata.Metadata;
import com.zergclan.wormhole.core.metadata.WormholeMetadata;
import com.zergclan.wormhole.core.metadata.catched.CachedPlanMetadata;
import com.zergclan.wormhole.scheduling.SchedulingExecutorFactory;
import com.zergclan.wormhole.scheduling.SchedulingTrigger;
import com.zergclan.wormhole.scheduling.PlanSchedulingTrigger;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

@RequiredArgsConstructor
public final class WormholeExecutionEngine implements Runnable {
    
    private final WormholeMetadata wormholeMetadata;
    
    private final Queue<SchedulingTrigger> planSchedulingTriggerQueue = new DelayQueue<>();
    
    // FIXME refactoring with cache
    private final Map<String, CachedPlanMetadata> cachedPlanMetadataContainer = new ConcurrentHashMap<>();
    
    /**
     * Register {@link Metadata}.
     *
     * @param metadata {@link Metadata}
     * @return is registered or not
     */
    public boolean registerMetadata(final Metadata metadata) {
        return wormholeMetadata.register(metadata);
    }
    
    /**
     * Register plan by plan identifier.
     *
     * @param planIdentifier plan identifier
     * @return is registered or not
     */
    public boolean registerPlan(final String planIdentifier) {
        return wormholeMetadata.get(planIdentifier).filter(metadata -> planSchedulingTriggerQueue.offer(new PlanSchedulingTrigger(metadata.getIdentifier()))).isPresent();
    }
    
    /**
     * Try to execute by plan identifier.
     *
     * @param planIdentifier plan identifier
     * @return is executing or not
     */
    public boolean tryExecute(final String planIdentifier) {
        return !isExecuting(planIdentifier) && execute(planIdentifier);
    }
    
    /**
     * Execute by plan identifier.
     *
     * @param planIdentifier plan identifier
     * @return is executing or not
     */
    private boolean execute(final String planIdentifier) {
        Optional<CachedPlanMetadata> cachedPlanMetadata = wormholeMetadata.cachedMetadata(planIdentifier);
        if (cachedPlanMetadata.isPresent()) {
            SchedulingExecutorFactory.createSchedulingExecutor(cachedPlanMetadata.get()).execute();
            return true;
        }
        return false;
    }
    
    private boolean isExecuting(final String planIdentifier) {
        return null != cachedPlanMetadataContainer.get(planIdentifier);
    }
    
    @Override
    public void run() {
        start();
    }
    
    private void start() {
        for (;;) {
            SchedulingTrigger trigger = planSchedulingTriggerQueue.poll();
            if (null != trigger) {
                String identifier = trigger.getIdentifier();
                if (!tryExecute(trigger.getIdentifier())) {
                    sendEvent(trigger);
                }
                // FIXME calculate time for next time create new trigger
                planSchedulingTriggerQueue.offer(new PlanSchedulingTrigger(identifier));
            }
        }
    }
    
    private void sendEvent(final SchedulingTrigger trigger) {
        // TODO send plan repeated event
        System.out.printf("error : repeated event for plan [%s]", trigger);
    }
}
