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

package com.zergclan.wormhole.pipeline;

import com.zergclan.wormhole.api.Filter;
import com.zergclan.wormhole.api.Handler;
import com.zergclan.wormhole.api.Pipeline;
import com.zergclan.wormhole.core.concurrent.ExecutorService;
import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.loader.Loader;
import com.zergclan.wormhole.pipeline.data.BatchedDataGroup;
import com.zergclan.wormhole.pipeline.handler.LoadedHandler;
import com.zergclan.wormhole.pipeline.handler.ProcessTaskHandler;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Batched {@link com.zergclan.wormhole.core.data.DataGroup} implemented of {@link Pipeline}.
 */
@RequiredArgsConstructor
public final class BatchedDataGroupPipeline implements Pipeline<Collection<Map<String, Object>>> {
    
    private final ExecutorService schedulingExecutor;
    
    private final Long planBatchId;
    
    private final Long taskBatchId;
    
    private final int batchSize;

    private final Collection<Filter<DataGroup>> filters;

    private final Loader loader;
    
    @Override
    public void handle(final Collection<Map<String, Object>> data) {
        int collectionSize = data.size();
        if (batchSize >= collectionSize) {
            schedulingExecutor.submit(createProcessTaskHandler(data));
            return;
        }
        handleBatchedData(data, collectionSize);
    }
    
    private ProcessTaskHandler createProcessTaskHandler(final Collection<Map<String, Object>> data) {
        // TODO init loadedHandler
        Handler<BatchedDataGroup> loadedHandler = new LoadedHandler(loader);
        return new ProcessTaskHandler(filters, loadedHandler, createBatchedDataGroup(data));
    }
    
    private BatchedDataGroup createBatchedDataGroup(final Collection<Map<String, Object>> data) {
        return new BatchedDataGroup(planBatchId, taskBatchId, data);
    }
    
    private void handleBatchedData(final Collection<Map<String, Object>> data, final int totalSize) {
        Iterator<Map<String, Object>> iterator = data.iterator();
        Collection<Map<String, Object>> batchedData = new LinkedList<>();
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            batchedData.add(iterator.next());
            if (count == batchSize || count == totalSize) {
                schedulingExecutor.submit(createProcessTaskHandler(batchedData));
                batchedData = new LinkedList<>();
            }
        }
    }
}
