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
import com.zergclan.wormhole.common.util.CollectionUtil;
import com.zergclan.wormhole.core.concurrent.ExecutorService;
import com.zergclan.wormhole.core.concurrent.ExecutorServiceManager;
import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.loader.Loader;
import com.zergclan.wormhole.pipeline.data.BatchedDataGroup;
import com.zergclan.wormhole.pipeline.handler.LoadedHandler;
import com.zergclan.wormhole.pipeline.handler.ProcessTaskHandler;
import com.zergclan.wormhole.writer.mysql.MySQLLoader;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Batched {@link com.zergclan.wormhole.core.data.DataGroup} implemented of {@link Pipeline}.
 */
@RequiredArgsConstructor
public final class BatchedDataGroupPipeline implements Pipeline<Collection<Map<String, Object>>> {
    
    private final Long planBatchId;
    
    private final Long taskBatchId;
    
    private final int batchSize;
    
    private final Loader loader;
    
    @Override
    public void handle(final Collection<Map<String, Object>> data) {
        // TODO protocol convert
        // TODO batch
        // TODO create handle
        handleData(data);
        ProcessTaskHandler task = createProcessTaskHandler();
        ExecutorService schedulingExecutor = ExecutorServiceManager.getSchedulingExecutor();
        schedulingExecutor.submit(task);
    }
    
    private void handleData(final Collection<Map<String, Object>> data) {
        handleBatchedData(data);
    }
    
    private Collection<BatchedDataGroup> handleBatchedData(final Collection<Map<String, Object>> data) {
        Collection<Collection<Map<String, Object>>> batchedData = CollectionUtil.partition(data, batchSize);
        Iterator<Collection<Map<String, Object>>> iterator = batchedData.iterator();
        while (iterator.hasNext()) {
            result.add(new BatchedDataGroup(planBatchId, taskBatchId, iterator.next()));
        }
        return result;
    }
    
    private ProcessTaskHandler createProcessTaskHandler() {
        Collection<Filter<DataGroup>> filters = new ArrayList<>();
        return new ProcessTaskHandler(filters, loader, batchedDataGroup);
    }
}
