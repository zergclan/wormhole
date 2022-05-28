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

package com.zergclan.wormhole.bootstrap.scheduling.event;

import com.zergclan.wormhole.bootstrap.scheduling.ExecutionState;
import com.zergclan.wormhole.bootstrap.scheduling.ExecutionStep;
import com.zergclan.wormhole.bus.api.Event;
import com.zergclan.wormhole.common.util.DateUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Plan execution event.
 */
@RequiredArgsConstructor
@Getter
public final class PlanExecutionEvent implements Event {
    
    private static final long serialVersionUID = -5925904644826031923L;
    
    private final String planIdentifier;
    
    private final String triggerIdentifier;
    
    private final Long planBatch;
    
    private final ExecutionStep executionStep;
    
    private final ExecutionState executionState;
    
    private final Long createTimestamp;
    
    private final Long endTimestamp;
    
    /**
     * Build new step {@link PlanExecutionEvent}.
     *
     * @param planIdentifier plan identifier
     * @param triggerIdentifier plan trigger identifier
     * @param executionState {@link ExecutionState}
     * @return {@link PlanExecutionEvent}
     */
    public static PlanExecutionEvent buildNewStateEvent(final String planIdentifier, final String triggerIdentifier, final ExecutionState executionState) {
        return new PlanExecutionEvent(planIdentifier, triggerIdentifier, 0L, ExecutionStep.NEW, executionState, DateUtil.currentTimeMillis(), 0L);
    }
    
    /**
     * Build ready step {@link PlanExecutionEvent}.
     *
     * @param planIdentifier plan identifier
     * @param triggerIdentifier plan trigger identifier
     * @param executionState {@link ExecutionState}
     * @return {@link PlanExecutionEvent}
     */
    public static PlanExecutionEvent buildReadyStateEvent(final String planIdentifier, final String triggerIdentifier, final ExecutionState executionState) {
        return new PlanExecutionEvent(planIdentifier, triggerIdentifier, 0L, ExecutionStep.READY, executionState, DateUtil.currentTimeMillis(), 0L);
    }
    
    /**
     * Build complete step {@link PlanExecutionEvent}.
     *
     * @param planBatch plan batch
     * @param executionState execution state
     * @return {@link PlanExecutionEvent}
     */
    public static PlanExecutionEvent buildCompleteStepEvent(final long planBatch, final ExecutionState executionState) {
        return new PlanExecutionEvent(null, null, planBatch, ExecutionStep.COMPLETE, executionState, null, DateUtil.currentTimeMillis());
    }
}
