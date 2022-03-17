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

package com.zergclan.wormhole.scheduling.plan;

import com.zergclan.wormhole.scheduling.Trigger;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public final class PlanTriggerManager {
    
    private static final int DEFAULT_INTERVAL_MILLISECONDS = 3000;
    
    private final DelayQueue<PlanTrigger> triggers;
    
    /**
     * Get executable {@link Trigger}.
     *
     * @return {@link Trigger}
     */
    public Optional<PlanTrigger> getExecutableTrigger() {
        try {
            return Optional.ofNullable(triggers.poll(DEFAULT_INTERVAL_MILLISECONDS, TimeUnit.MILLISECONDS));
        } catch (final InterruptedException ignore) {
            return Optional.empty();
        }
    }
    
    /**
     * Register trigger.
     *
     * @param planTrigger {@link PlanTrigger}
     * @return is registered or not
     */
    public boolean register(final PlanTrigger planTrigger) {
        if (planTrigger instanceof OneOffPlanTrigger) {
            return handleOneOffPlanTrigger((OneOffPlanTrigger) planTrigger);
        } else if (planTrigger instanceof ScheduledPlanTrigger) {
            return handleScheduledPlanTrigger((ScheduledPlanTrigger) planTrigger);
        } else {
            throw new UnsupportedOperationException("error: unsupported plan trigger: " + planTrigger.getIdentifier());
        }
    }
    
    private boolean handleScheduledPlanTrigger(final ScheduledPlanTrigger trigger) {
        return triggers.add(new ScheduledPlanTrigger(trigger.getPlanIdentifier(), trigger.getExpression()));
    }
    
    private boolean handleOneOffPlanTrigger(final OneOffPlanTrigger trigger) {
        if (trigger.hasNextExecution()) {
            return triggers.add(trigger);
        }
        return false;
    }
}
