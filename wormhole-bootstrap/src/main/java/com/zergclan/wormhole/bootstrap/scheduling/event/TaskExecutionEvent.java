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
import com.zergclan.wormhole.tool.util.DateUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Task execution event.
 */
@RequiredArgsConstructor
@Getter
public final class TaskExecutionEvent implements Event {
    
    private static final long serialVersionUID = -3200589002588431821L;
    
    private final String planIdentifier;
    
    private final Long planBatch;
    
    private final String taskIdentifier;
    
    private final Long taskBatch;
    
    private final ExecutionStep executionStep;
    
    private final ExecutionState executionState;

    private final Integer remainingRow;

    private final Long createTimestamp;
    
    private final Long endTimestamp;
    
    /**
     * Build new {@link TaskExecutionEvent}.
     * @param planIdentifier plan identifier
     * @param planBatch plan batch
     * @param taskIdentifier task identifier
     * @param taskBatch task batch
     * @return {@link TaskExecutionEvent}
     */
    public static TaskExecutionEvent buildNewEvent(final String planIdentifier, final Long planBatch, final String taskIdentifier, final long taskBatch) {
        return new TaskExecutionEvent(planIdentifier, planBatch, taskIdentifier, taskBatch, ExecutionStep.NEW, ExecutionState.SUCCESS, null, DateUtil.currentTimeMillis(), 0L);
    }
    
    /**
     * Build ready {@link TaskExecutionEvent}.
     *
     * @param taskBatch task batch
     * @param remainingRow remaining row
     * @return {@link TaskExecutionEvent}
     */
    public static TaskExecutionEvent buildReadyEvent(final long taskBatch, final int remainingRow) {
        return new TaskExecutionEvent(null, null, null, taskBatch, ExecutionStep.READY, ExecutionState.SUCCESS, remainingRow, null, null);
    }
    
    /**
     * Build execution {@link TaskExecutionEvent}.
     *
     * @param taskBatch task batch
     * @param remainingRow remaining row
     * @return {@link TaskExecutionEvent}
     */
    public static TaskExecutionEvent buildExecutionEvent(final long taskBatch, final int remainingRow) {
        return new TaskExecutionEvent(null, null, null, taskBatch, ExecutionStep.READY, ExecutionState.SUCCESS, remainingRow, null, null);
    }
    
    /**
     * Build complete {@link TaskExecutionEvent}.
     *
     * @param taskBatch plan batch
     * @param executionState execution state
     * @return {@link TaskExecutionEvent}
     */
    public static TaskExecutionEvent buildCompleteEvent(final long taskBatch, final ExecutionState executionState) {
        return new TaskExecutionEvent(null, null, null, taskBatch, ExecutionStep.COMPLETE, executionState, null, DateUtil.currentTimeMillis(), 0L);
    }
}
