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
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.tool.util.CronUtil;
import com.zergclan.wormhole.tool.util.DateUtil;
import com.zergclan.wormhole.tool.util.Validator;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * Scheduled {@link PlanMetaData.ExecutionMode} plan trigger.
 */
@Getter
public final class ScheduledPlanTrigger implements PlanTrigger {

    private static final String PREFIX_IDENTIFIER = PlanMetaData.ExecutionMode.SCHEDULED.name();
    
    private final String planIdentifier;
    
    private final String expression;
    
    private final long nextExecutionTimestamp;

    public ScheduledPlanTrigger(final String planIdentifier, final String expression) {
        this.planIdentifier = planIdentifier;
        this.expression = expression;
        this.nextExecutionTimestamp = CronUtil.nextTimeMillis(expression).orElse(-1L);
    }
    
    @Override
    public String getIdentifier() {
        return PREFIX_IDENTIFIER + MarkConstant.SPACE + planIdentifier + MarkConstant.COLON + expression;
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
