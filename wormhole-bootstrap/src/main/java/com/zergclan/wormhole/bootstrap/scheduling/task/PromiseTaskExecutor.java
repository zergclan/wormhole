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

import com.zergclan.wormhole.common.concurrent.ExecutorServiceManager;
import com.zergclan.wormhole.common.concurrent.PromiseTask;
import com.zergclan.wormhole.data.api.result.Result;
import com.zergclan.wormhole.data.core.BatchedDataGroup;
import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.metadata.core.catched.CachedSourceMetaData;
import com.zergclan.wormhole.metadata.core.catched.CachedTargetMetaData;
import com.zergclan.wormhole.metadata.core.catched.CachedTaskMetaData;
import com.zergclan.wormhole.metadata.core.filter.FilterMetaData;
import com.zergclan.wormhole.metadata.core.filter.FilterType;
import com.zergclan.wormhole.pipeline.api.Filter;
import com.zergclan.wormhole.pipeline.api.Handler;
import com.zergclan.wormhole.pipeline.core.filter.DataGroupFilterFactory;
import com.zergclan.wormhole.pipeline.core.handler.LoadedHandler;
import com.zergclan.wormhole.plugin.api.Extractor;
import com.zergclan.wormhole.plugin.api.Loader;
import com.zergclan.wormhole.plugin.factory.ExtractorFactory;
import com.zergclan.wormhole.plugin.factory.LoaderFactory;
import lombok.RequiredArgsConstructor;

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
    public PromiseTaskResult call() throws Exception {
        CachedSourceMetaData source = cachedTaskMetadata.getSource();
        CachedTargetMetaData target = cachedTaskMetadata.getTarget();
        Optional<Extractor> extractor = ExtractorFactory.getExtractor(source);
        Optional<Loader> loader = LoaderFactory.getLoader(target);
        if (extractor.isPresent() && loader.isPresent()) {
            return handleTask(extractor.get(), loader.get());
        }
        return PromiseTaskResult.newError(new TaskResult(cachedTaskMetadata.getIdentifier()));
    }
    
    private PromiseTaskResult handleTask(final Extractor<DataGroup> extractor, final Loader<BatchedDataGroup, Result<?>> loader) {
        Collection<DataGroup> dataGroups = extractor.extract();
        if (dataGroups.isEmpty()) {
            return PromiseTaskResult.newSuccess(createTaskResult());
        }
        int size = dataGroups.size();
        int batchSize = cachedTaskMetadata.getBatchSize();
        if (size <= batchSize) {
            handleBatchedTask(dataGroups, loader);
            return PromiseTaskResult.newSuccess(createTaskResult());
        }
        int count = 0;
        Iterator<DataGroup> iterator = dataGroups.iterator();
        Collection<DataGroup> batchedEach = new LinkedList<>();
        while (iterator.hasNext()) {
            count++;
            batchedEach.add(iterator.next());
            if (batchSize == batchedEach.size() || size == count) {
                handleBatchedTask(batchedEach, loader);
                batchedEach = new LinkedList<>();
            }
        }
        return PromiseTaskResult.newSuccess(createTaskResult());
    }
    
    private void handleBatchedTask(final Collection<DataGroup> dataGroups, final Loader<BatchedDataGroup, Result<?>> loader) {
        String taskIdentifier = cachedTaskMetadata.getIdentifier();
        long taskBatch = cachedTaskMetadata.getTaskBatch();
        BatchedDataGroup batchedDataGroup = new BatchedDataGroup(cachedTaskMetadata.getBatchSize(), dataGroups);
        Collection<Filter<DataGroup>> filters = createFilters(cachedTaskMetadata);
        Handler<BatchedDataGroup> nextHandler = new LoadedHandler(loader);
        ExecutorServiceManager.getComputingExecutor().execute(new BatchedDataGroupHandler(planIdentifier, planBatch, taskIdentifier, taskBatch, batchedDataGroup, filters, nextHandler));
    }
    
    private Collection<Filter<DataGroup>> createFilters(final CachedTaskMetaData cachedTaskMetadata) {
        Collection<Filter<DataGroup>> result = new LinkedHashSet<>();
        Map<Integer, Map<FilterType, Collection<FilterMetaData>>> filters = cachedTaskMetadata.getFilters();
        Iterator<Map.Entry<Integer, Map<FilterType, Collection<FilterMetaData>>>> iterator = filters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map<FilterType, Collection<FilterMetaData>> value = iterator.next().getValue();
            result.addAll(DataGroupFilterFactory.createDataGroupFilters(iterator.next().getKey(), iterator.next().getValue()));
        }
        return result;
    }
    
    private TaskResult createTaskResult() {
        return new TaskResult(cachedTaskMetadata.getIdentifier());
    }
}
