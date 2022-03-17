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

package com.zergclan.wormhole.scheduling;

import com.zergclan.wormhole.scheduling.plan.OneOffPlanTrigger;
import com.zergclan.wormhole.scheduling.plan.ScheduledPlanTrigger;

import java.util.Optional;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * Trigger manager.
 */
public final class TriggerManager {
    
    private static final int DEFAULT_INTERVAL_MILLISECONDS = 3000;
    
    private static final DelayQueue<Trigger> triggers = new DelayQueue<>();

    /**
     * Get executable {@link Trigger}.
     *
     * @return {@link Trigger}
     */
    public static Optional<Trigger> getExecutableTrigger() {
        try {
            return Optional.ofNullable(triggers.poll(DEFAULT_INTERVAL_MILLISECONDS, TimeUnit.MILLISECONDS));
        } catch (final InterruptedException ignore) {
            return Optional.empty();
        }
    }

    /**
     * Register trigger.
     *
     * @param trigger {@link Trigger}
     * @return is registered or not
     */
    public static boolean register(final Trigger trigger) {
        if (trigger instanceof OneOffPlanTrigger) {
            return handleOneOffPlanTrigger((OneOffPlanTrigger) trigger);
        } else if (trigger instanceof ScheduledPlanTrigger) {
            return handleScheduledPlanTrigger((ScheduledPlanTrigger) trigger);
        } else {
            throw new UnsupportedOperationException("error: unsupported plan trigger: " + trigger.getIdentifier());
        }
    }
    
    private static boolean handleScheduledPlanTrigger(final ScheduledPlanTrigger trigger) {
        return triggers.add(new ScheduledPlanTrigger(trigger.getPlanIdentifier(), trigger.getExpression()));
    }
    
    private static boolean handleOneOffPlanTrigger(final OneOffPlanTrigger trigger) {
        if (trigger.hasNextExecution()) {
            return triggers.add(trigger);
        }
        return false;
    }
}
