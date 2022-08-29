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

package com.zergclan.wormhole.bootstrap.scheduling.plan;

import com.zergclan.wormhole.common.metadata.plan.PlanMetaData;

import java.util.Optional;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * Plan trigger manager.
 */
public final class PlanTriggerManager {
    
    private static final int DEFAULT_INTERVAL_MILLISECONDS = 3000;
    
    private final DelayQueue<PlanTrigger> triggers = new DelayQueue<>();

    /**
     * Get executable {@link PlanTrigger}.
     *
     * @return {@link PlanTrigger}
     */
    public Optional<PlanTrigger> getExecutableTrigger() {
        try {
            return Optional.ofNullable(triggers.poll(DEFAULT_INTERVAL_MILLISECONDS, TimeUnit.MILLISECONDS));
        } catch (final InterruptedException ignore) {
            return Optional.empty();
        }
    }

    /**
     * Register {@link PlanTrigger} by {@link PlanMetaData}.
     *
     * @param planMetadata {@link PlanMetaData}
     * @return is registered or not
     */
    public boolean register(final PlanMetaData planMetadata) {
        switch (planMetadata.getMode()) {
            case ONE_OFF:
                return registerOneOffPlanTrigger(planMetadata);
            case SCHEDULED:
                return registerScheduledPlanTrigger(planMetadata);
            default:
                throw new UnsupportedOperationException("error: unsupported plan execution mode of planMetadata identifier: " + planMetadata.getIdentifier());
        }
    }

    private boolean registerOneOffPlanTrigger(final PlanMetaData planMetadata) {
        return triggers.offer(new OneOffPlanTrigger(planMetadata.getIdentifier(), planMetadata.getExpression()));
    }

    private boolean registerScheduledPlanTrigger(final PlanMetaData planMetadata) {
        return triggers.offer(new ScheduledPlanTrigger(planMetadata.getIdentifier(), planMetadata.getExpression()));
    }

    /**
     * Re-register {@link PlanTrigger}.
     *
     * @param planTrigger {@link PlanTrigger}
     * @return is re-register or not
     */
    public boolean reRegister(final ScheduledPlanTrigger planTrigger) {
        return handleScheduledPlanTrigger(planTrigger);
    }

    private boolean handleScheduledPlanTrigger(final ScheduledPlanTrigger trigger) {
        return triggers.offer(new ScheduledPlanTrigger(trigger.getPlanIdentifier(), trigger.getExpression()));
    }
}
