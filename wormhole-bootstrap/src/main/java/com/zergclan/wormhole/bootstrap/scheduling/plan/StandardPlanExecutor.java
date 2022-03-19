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

import com.zergclan.wormhole.bootstrap.context.catched.CachedPlanMetaData;
import com.zergclan.wormhole.bootstrap.context.catched.CachedTaskMetaData;
import com.zergclan.wormhole.bootstrap.scheduling.task.PromiseTaskExecutor;
import com.zergclan.wormhole.bootstrap.scheduling.task.PromiseTaskResult;
import com.zergclan.wormhole.common.SequenceGenerator;
import com.zergclan.wormhole.common.concurrent.ExecutorServiceManager;
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
        final long planBatch = SequenceGenerator.generateId();
        cachedPlanMetadata.getCachedTasks().forEach(each -> parallelExecute(each, planIdentifier, planBatch));
        // TODO send plan execute success event
    }

    private void parallelExecute(final Map<String, CachedTaskMetaData> cachedTaskMetadata, final String planIdentifier, final long planBatch) {
        CompletionService<PromiseTaskResult> completionService = new ExecutorCompletionService<>(ExecutorServiceManager.getSchedulingExecutor());
        for (Map.Entry<String, CachedTaskMetaData> entry : cachedTaskMetadata.entrySet()) {
            PromiseTaskExecutor promiseTaskExecutor = new PromiseTaskExecutor(planIdentifier, planBatch, SequenceGenerator.generateId(), entry.getValue());
            completionService.submit(promiseTaskExecutor);
        }
        int size = cachedTaskMetadata.size();
        PromiseTaskResult promiseTaskResult;
        for (int i = 0; i < size; i++) {
            try {
                promiseTaskResult = completionService.take().get();
                // TODO send task execute success event
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                // TODO send task execute failed event
            }
        }
    }
}
