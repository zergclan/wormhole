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

package com.zergclan.wormhole.pipeline.core;

import com.zergclan.wormhole.common.concurrent.ExecutorService;
import com.zergclan.wormhole.data.api.BatchedDataGroup;
import com.zergclan.wormhole.data.api.DataGroup;
import com.zergclan.wormhole.pipeline.api.Filter;
import com.zergclan.wormhole.pipeline.api.Handler;
import com.zergclan.wormhole.pipeline.api.Pipeline;
import com.zergclan.wormhole.pipeline.core.handler.LoadedHandler;
import com.zergclan.wormhole.pipeline.core.handler.ProcessTaskHandler;
import com.zergclan.wormhole.plugin.api.Loader;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * Batched {@link DataGroup} implemented of {@link Pipeline}.
 */
@RequiredArgsConstructor
public final class BatchedDataGroupPipeline implements Pipeline<BatchedDataGroup> {
    
    private final ExecutorService schedulingExecutor;
    
    private final Long planBatchId;
    
    private final Long taskBatchId;
    
    private final int batchSize;

    private final Collection<Filter<DataGroup>> filters;

    private final Loader<BatchedDataGroup> loader;
    
    @Override
    public void handle(final BatchedDataGroup batchedDataGroup) {
        schedulingExecutor.submit(createProcessTaskHandler(batchedDataGroup));
    }
    
    private ProcessTaskHandler createProcessTaskHandler(final BatchedDataGroup batchedDataGroup) {
        // TODO init loadedHandler
        Handler<BatchedDataGroup> loadedHandler = new LoadedHandler(loader);
        return new ProcessTaskHandler(filters, loadedHandler, batchedDataGroup);
    }
}
