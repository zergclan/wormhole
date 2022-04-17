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

import com.zergclan.wormhole.common.concurrent.ProcessTask;
import com.zergclan.wormhole.data.core.BatchedDataGroup;
import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.pipeline.api.Filter;
import com.zergclan.wormhole.pipeline.api.Handler;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;

/**
 * Implemented {@link ProcessTask} to handle {@link BatchedDataGroup}.
 */
@RequiredArgsConstructor
public final class BatchedDataGroupHandler implements ProcessTask {
    
    private final String planIdentifier;
    
    private final long planBatch;
    
    private final String taskIdentifier;
    
    private final long taskBatchId;
    
    private final BatchedDataGroup batchedDataGroup;
    
    private final Collection<Filter<DataGroup>> filters;
    
    private final Handler<BatchedDataGroup> loadedHandler;
    
    @Override
    public void run() {
        Iterator<DataGroup> iterator = batchedDataGroup.getDataGroups().iterator();
        while (iterator.hasNext()) {
            DataGroup dataGroup = iterator.next();
            if (!handleDataGroup(dataGroup)) {
                batchedDataGroup.remove(dataGroup);
            }
        }
        loadedHandler.handle(batchedDataGroup);
        // TODO send task result event
    }
    
    private boolean handleDataGroup(final DataGroup dataGroup) {
        Iterator<Filter<DataGroup>> iterator = filters.iterator();
        while (iterator.hasNext()) {
            Filter<DataGroup> filter = iterator.next();
            if (!filter.doFilter(dataGroup)) {
                // TODO send error data event
                return false;
            }
        }
        return true;
    }
}
