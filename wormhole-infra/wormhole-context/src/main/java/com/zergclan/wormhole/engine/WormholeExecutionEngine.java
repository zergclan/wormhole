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

import com.zergclan.wormhole.common.exception.WormholeException;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.context.PlanContext;
import com.zergclan.wormhole.core.config.WormholeConfiguration;
import com.zergclan.wormhole.creator.WormholeMetadataCreator;
import com.zergclan.wormhole.core.api.metadata.Metadata;
import com.zergclan.wormhole.core.metadata.WormholeMetadata;
import com.zergclan.wormhole.core.metadata.catched.CachedPlanMetadata;
import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import com.zergclan.wormhole.scheduling.SchedulingTrigger;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.DelayQueue;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class WormholeExecutionEngine implements Runnable {

    private final WormholeMetadata wormholeMetadata;

    private final Queue<SchedulingTrigger> planSchedulingTriggerQueue = new DelayQueue<>();
    
    private final PlanContext planContext = new PlanContext();

    private WormholeExecutionEngine(final WormholeConfiguration configuration) throws SQLException {
        WormholeMetadata wormholeMetadata = WormholeMetadataCreator.create(configuration);
        Optional<String> unregistered = registerPlans(wormholeMetadata.getPlans());
        if (unregistered.isPresent()) {
            throw new WormholeException("error : wormhole execution engine register plan failed named by [%s]", unregistered.get());
        }
        this.wormholeMetadata = wormholeMetadata;
    }

    /**
     * New instance.
     *
     * @param configuration {@link WormholeConfiguration}
     * @return {@link WormholeExecutionEngine}
     * @throws SQLException exception
     */
    public static WormholeExecutionEngine newInstance(final WormholeConfiguration configuration) throws SQLException {
        Validator.notNull(configuration, "error : wormhole execution engine new instance configuration can not be null");
        return new WormholeExecutionEngine(configuration);
    }

    private Optional<String> registerPlans(final Map<String, PlanMetadata> plans) {
        // TODO register plan to scheduling
        return Optional.empty();
    }

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
        // TODO register plan
        return false;
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
            // fixme create scheduling executor
            return true;
        }
        return false;
    }
    
    private boolean isExecuting(final String planIdentifier) {
        return planContext.isExecuting(planIdentifier);
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
            }
        }
    }

    private void sendEvent(final SchedulingTrigger trigger) {
        // TODO send plan repeated event
        System.out.printf("error : repeated event for plan [%s]", trigger);
    }
}
