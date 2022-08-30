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

import com.zergclan.wormhole.bootstrap.scheduling.ExecutionState;
import com.zergclan.wormhole.bootstrap.scheduling.event.PlanExecutionEvent;
import com.zergclan.wormhole.bootstrap.scheduling.event.TaskExecutionEvent;
import com.zergclan.wormhole.bootstrap.scheduling.task.PromiseTaskExecutor;
import com.zergclan.wormhole.bootstrap.scheduling.task.PromiseTaskResult;
import com.zergclan.wormhole.bootstrap.scheduling.task.TaskResult;
import com.zergclan.wormhole.bus.api.Event;
import com.zergclan.wormhole.bus.memory.WormholeEventBus;
import com.zergclan.wormhole.common.metadata.catched.CachedPlanMetaData;
import com.zergclan.wormhole.common.metadata.catched.CachedTaskMetaData;
import com.zergclan.wormhole.tool.concurrent.ExecutorServiceManager;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;

/**
 * Standard plan implemented of {@link PlanExecutor}.
 */
@RequiredArgsConstructor
public final class StandardPlanExecutor implements PlanExecutor {

    private final CachedPlanMetaData cachedPlanMetadata;

    @Override
    public void execute() {
        String planIdentifier = cachedPlanMetadata.getIdentifier();
        long planBatch = cachedPlanMetadata.getPlanBatch();
        handleEvent(PlanExecutionEvent.buildExecutionEvent(planBatch, ExecutionState.RUN));
        try {
            cachedPlanMetadata.getCachedOrderedTasks().forEach(each -> parallelExecute(each, planIdentifier, planBatch));
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            handleEvent(PlanExecutionEvent.buildCompleteEvent(planBatch, ExecutionState.ERROR));
        }
    }

    private void parallelExecute(final Map<String, CachedTaskMetaData> cachedTaskMetadata, final String planIdentifier, final long planBatch) {
        CompletionService<PromiseTaskResult> completionService = new ExecutorCompletionService<>(ExecutorServiceManager.getSchedulingExecutor());
        for (Map.Entry<String, CachedTaskMetaData> entry : cachedTaskMetadata.entrySet()) {
            CachedTaskMetaData cachedTaskMetaData = entry.getValue();
            String taskIdentifier = cachedTaskMetaData.getTaskIdentifier();
            long taskBatch = cachedTaskMetaData.getTaskBatch();
            handleEvent(TaskExecutionEvent.buildNewEvent(cachedPlanMetadata.getPlanIdentifier(), planBatch, taskIdentifier, taskBatch));
            PromiseTaskExecutor promiseTaskExecutor = new PromiseTaskExecutor(planIdentifier, planBatch, entry.getValue());
            completionService.submit(promiseTaskExecutor);
        }
        int size = cachedTaskMetadata.size();
        PromiseTaskResult promiseTaskResult;
        for (int i = 0; i < size; i++) {
            try {
                promiseTaskResult = completionService.take().get();
                if (!promiseTaskResult.isSuccess()) {
                    handleEvent(TaskExecutionEvent.buildCompleteEvent(promiseTaskResult.getResultData().getTaskBatch(), ExecutionState.FAILED));
                    continue;
                }
                TaskResult result = promiseTaskResult.getResultData();
                if (0 == result.getTotalRow()) {
                    handleEvent(TaskExecutionEvent.buildCompleteEvent(promiseTaskResult.getResultData().getTaskBatch(), ExecutionState.SUCCESS));
                } else {
                    handleEvent(TaskExecutionEvent.buildExecutionEvent(promiseTaskResult.getResultData().getTaskBatch(), promiseTaskResult.getResultData().getTotalRow()));
                }
            } catch (final InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void handleEvent(final Event event) {
        WormholeEventBus.post(event);
    }
}
