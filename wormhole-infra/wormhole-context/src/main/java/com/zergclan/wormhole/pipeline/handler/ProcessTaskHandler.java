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

package com.zergclan.wormhole.pipeline.handler;

import com.zergclan.wormhole.api.Filter;
import com.zergclan.wormhole.api.Handler;
import com.zergclan.wormhole.core.concurrent.ProcessTask;
import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.pipeline.data.BatchedDataGroup;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;

/**
 * Implemented {@link ProcessTask} to handle {@link BatchedDataGroup}.
 */
@RequiredArgsConstructor
public final class ProcessTaskHandler implements ProcessTask {
    
    private final Collection<Filter<DataGroup>> filters;
    
    private final Handler<BatchedDataGroup> nextHandler;
    
    private final BatchedDataGroup batchedDataGroup;
    
    @Override
    public void run() {
        Iterator<DataGroup> iterator = batchedDataGroup.getDataGroups().iterator();
        DataGroup each;
        while (iterator.hasNext()) {
            each = iterator.next();
            if (handleDataGroup(each)) {
                batchedDataGroup.clearError(each);
            }
        }
        nextHandler.handle(batchedDataGroup);
    }
    
    private boolean handleDataGroup(final DataGroup dataGroup) {
        Iterator<Filter<DataGroup>> iterator = filters.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().doFilter(dataGroup)) {
                return false;
            }
        }
        return true;
    }
}
