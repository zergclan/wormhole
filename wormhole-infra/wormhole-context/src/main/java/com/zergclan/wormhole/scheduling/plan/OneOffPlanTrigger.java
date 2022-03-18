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

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.common.util.DateUtil;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * One off {@link PlanMetadata.ExecutionMode} plan trigger.
 */
public final class OneOffPlanTrigger implements PlanTrigger {
    
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String PREFIX_IDENTIFIER = PlanMetadata.ExecutionMode.ONE_OFF.name();
    
    @Getter
    private final String planIdentifier;

    private final long nextExecutionTimestamp;

    public OneOffPlanTrigger(final String planIdentifier, final String expression) {
        this.planIdentifier = planIdentifier;
        this.nextExecutionTimestamp = DateUtil.parse(expression, DEFAULT_PATTERN).getTime();
    }

    @Override
    public String getIdentifier() {
        return PREFIX_IDENTIFIER + MarkConstant.SPACE + planIdentifier;
    }
    
    @Override
    public boolean hasNextExecution() {
        return delayedTime() > 0;
    }
    
    @Override
    public long getDelay(final TimeUnit timeUnit) {
        Validator.notNull(timeUnit, "error: OneOffPlanTrigger getDelay arg timeUnit can not be null");
        return timeUnit.convert(delayedTime(), TimeUnit.MILLISECONDS);
    }

    private long delayedTime() {
        return nextExecutionTimestamp - DateUtil.currentTimeMillis();
    }
}
