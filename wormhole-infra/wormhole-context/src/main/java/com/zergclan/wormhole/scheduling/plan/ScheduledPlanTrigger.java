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

import com.zergclan.wormhole.common.util.DateUtil;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import com.zergclan.wormhole.scheduling.Trigger;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled {@link PlanMetadata.ExecutionMode} plan trigger.
 */
public final class ScheduledPlanTrigger implements Trigger {
    
    private final String expression;
    
    private final long nextExecutionTimestamp;
    
    public ScheduledPlanTrigger(final String expression) {
        this.expression = expression;
        this.nextExecutionTimestamp = 1L;
    }
    
    @Override
    public String getIdentifier() {
        return "";
    }
    
    @Override
    public Optional<Long> nextExecutionTimestamp() {
        return Optional.empty();
    }
    
    @Override
    public long getDelay(final TimeUnit timeUnit) {
        Validator.notNull(timeUnit, "error: OneOffPlanTrigger getDelay arg timeUnit can not be null");
        return 0;
    }
    
    @Override
    public int compareTo(final Delayed delayed) {
        Validator.notNull(delayed, "error: OneOffPlanTrigger compareTo arg delayed can not be null");
        if (getDelay(TimeUnit.MILLISECONDS) > delayed.getDelay(TimeUnit.MILLISECONDS)) {
            return 1;
        } else if (getDelay(TimeUnit.MILLISECONDS) < delayed.getDelay(TimeUnit.MILLISECONDS)) {
            return -1;
        } else {
            return 0;
        }
    }
}
