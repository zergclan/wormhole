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

package com.zergclan.wormhole.bootstrap.scheduling.task;

import com.zergclan.wormhole.bootstrap.scheduling.event.TaskExecutionEvent;
import com.zergclan.wormhole.common.WormholeEvent;
import com.zergclan.wormhole.common.WormholeResult;
import com.zergclan.wormhole.common.data.BatchedDataGroup;
import com.zergclan.wormhole.common.data.node.DataGroup;
import com.zergclan.wormhole.common.data.result.LoadResultData;
import com.zergclan.wormhole.common.eventbus.WormholeEventBus;
import com.zergclan.wormhole.common.metadata.catched.CachedSourceMetaData;
import com.zergclan.wormhole.common.metadata.catched.CachedTargetMetaData;
import com.zergclan.wormhole.common.metadata.catched.CachedTaskMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.tool.concurrent.ExecutorServiceManager;
import com.zergclan.wormhole.tool.concurrent.PromiseTask;
import com.zergclan.wormhole.pipeline.filter.Filter;
import com.zergclan.wormhole.pipeline.handler.Handler;
import com.zergclan.wormhole.pipeline.event.DataGroupExecutionEvent;
import com.zergclan.wormhole.pipeline.filter.DataGroupFilterFactory;
import com.zergclan.wormhole.pipeline.handler.LoadedHandler;
import com.zergclan.wormhole.extractor.WormholeExtractor;
import com.zergclan.wormhole.loader.WormholeLoader;
import com.zergclan.wormhole.extractor.WormholeExtractorFactory;
import com.zergclan.wormhole.loader.WormholeLoaderFactory;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

/**
 * Promise task executor.
 */
@RequiredArgsConstructor
public final class PromiseTaskExecutor implements PromiseTask<PromiseTaskResult> {

    private final String planIdentifier;

    private final long planBatch;

    private final CachedTaskMetaData cachedTaskMetadata;
    
    @Override
    @SuppressWarnings("all")
    public PromiseTaskResult call() throws Exception {
        CachedSourceMetaData source = cachedTaskMetadata.getSource();
        CachedTargetMetaData target = cachedTaskMetadata.getTarget();
        try {
            Optional<WormholeExtractor> extractor = WormholeExtractorFactory.getExtractor(source);
            Optional<WormholeLoader> loader = WormholeLoaderFactory.getLoader(target);
            if (extractor.isPresent() && loader.isPresent()) {
                handleEvent(TaskExecutionEvent.buildReadyEvent(cachedTaskMetadata.getTaskBatch(), -1));
                return handleTask(extractor.get(), loader.get());
            }
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            ex.printStackTrace();
        }
        return PromiseTaskResult.newError(createTaskResult(-1));
    }
    
    private PromiseTaskResult handleTask(final WormholeExtractor<DataGroup> extractor, final WormholeLoader<BatchedDataGroup, WormholeResult<LoadResultData>> loader) throws SQLException {
        Collection<DataGroup> dataGroups = extractor.extract();
        if (dataGroups.isEmpty()) {
            return PromiseTaskResult.newSuccess(createTaskResult(0));
        }
        int totalRow = dataGroups.size();
        int batchSize = cachedTaskMetadata.getBatchSize();
        Handler<BatchedDataGroup> nextHandler = new LoadedHandler(loader);
        Collection<Filter<DataGroup>> filters = createFilters(cachedTaskMetadata);
        if (totalRow <= batchSize) {
            handleBatchedTask(dataGroups, nextHandler, filters, 0);
            return PromiseTaskResult.newSuccess(createTaskResult(totalRow));
        }
        Iterator<DataGroup> iterator = dataGroups.iterator();
        Collection<DataGroup> batchedEach = new LinkedList<>();
        int count = 0;
        int batchIndex = 0;
        while (iterator.hasNext()) {
            count++;
            batchedEach.add(iterator.next());
            if (batchSize == batchedEach.size() || totalRow == count) {
                handleBatchedTask(batchedEach, nextHandler, filters, batchIndex);
                batchIndex++;
                batchedEach = new LinkedList<>();
            }
        }
        return PromiseTaskResult.newSuccess(createTaskResult(totalRow));
    }
    
    private Collection<Filter<DataGroup>> createFilters(final CachedTaskMetaData cachedTaskMetadata) {
        Collection<Filter<DataGroup>> result = new LinkedHashSet<>();
        Map<Integer, Map<FilterType, Collection<FilterMetaData>>> filters = cachedTaskMetadata.getFilters();
        Iterator<Map.Entry<Integer, Map<FilterType, Collection<FilterMetaData>>>> iterator = filters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Map<FilterType, Collection<FilterMetaData>>> entry = iterator.next();
            Integer order = entry.getKey();
            Map<FilterType, Collection<FilterMetaData>> typedFilters = entry.getValue();
            Collection<Filter<DataGroup>> dataGroupFilters = DataGroupFilterFactory.createDataGroupFilters(order, typedFilters);
            result.addAll(dataGroupFilters);
        }
        return result;
    }
    
    private void handleBatchedTask(final Collection<DataGroup> dataGroups, final Handler<BatchedDataGroup> nextHandler, final Collection<Filter<DataGroup>> filters, final int batchIndex) {
        String taskIdentifier = cachedTaskMetadata.getIdentifier();
        long taskBatch = cachedTaskMetadata.getTaskBatch();
        int batchSize = dataGroups.size();
        String ownerIdentifier = cachedTaskMetadata.getSource().getDataSource().getIdentifier();
        handleEvent(DataGroupExecutionEvent.buildNewEvent(planIdentifier, planBatch, taskIdentifier, taskBatch, batchIndex, batchSize));
        BatchedDataGroup batchedDataGroup = new BatchedDataGroup(planIdentifier, planBatch, taskIdentifier, taskBatch, ownerIdentifier, batchIndex, batchSize, dataGroups);
        BatchedDataGroupHandler batchedDataGroupHandler = new BatchedDataGroupHandler(batchedDataGroup, filters, nextHandler);
        ExecutorServiceManager.getComputingExecutor().submit(batchedDataGroupHandler);
    }
    
    private TaskResult createTaskResult(final int totalRow) {
        String cachedTaskIdentifier = cachedTaskMetadata.getIdentifier();
        String taskIdentifier = cachedTaskMetadata.getTaskIdentifier();
        long taskBatch = cachedTaskMetadata.getTaskBatch();
        return new TaskResult(cachedTaskIdentifier, taskIdentifier, taskBatch, totalRow);
    }
    
    private void handleEvent(final WormholeEvent event) {
        WormholeEventBus.post(event);
    }
}
