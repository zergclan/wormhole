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

import com.zergclan.wormhole.common.data.BatchedDataGroup;
import com.zergclan.wormhole.common.data.DataGroup;
import com.zergclan.wormhole.common.eventbus.WormholeEventBus;
import com.zergclan.wormhole.pipeline.event.ErrorDataEvent;
import com.zergclan.wormhole.tool.concurrent.ProcessTask;
import com.zergclan.wormhole.tool.util.DateUtil;
import com.zergclan.wormhole.pipeline.filter.Filter;
import com.zergclan.wormhole.pipeline.handler.Handler;
import com.zergclan.wormhole.pipeline.filter.exception.WormholeFilterException;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;

/**
 * Implemented {@link ProcessTask} to handle {@link BatchedDataGroup}.
 */
@RequiredArgsConstructor
public final class BatchedDataGroupHandler implements ProcessTask {
    
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
    }
    
    private boolean handleDataGroup(final DataGroup dataGroup) {
        Iterator<Filter<DataGroup>> iterator = filters.iterator();
        while (iterator.hasNext()) {
            Filter<DataGroup> filter = iterator.next();
            boolean isFiltered;
            try {
                isFiltered = filter.doFilter(dataGroup);
                if (!isFiltered) {
                    handleErrorDataEvent(filter.getType(), filter.getType(), dataGroup.toString());
                    return false;
                }
            } catch (WormholeFilterException wex) {
                handleErrorDataEvent(filter.getType(), wex.getMessage(), dataGroup.toString());
                // CHECKSTYLE:OFF
            } catch (Exception ex) {
                // CHECKSTYLE:ON
                handleErrorDataEvent(filter.getType(), ex.getMessage(), dataGroup.toString());
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }
    
    private void handleErrorDataEvent(final String code, final String message, final String dataJson) {
        WormholeEventBus.post(new ErrorDataEvent(batchedDataGroup.getPlanIdentifier(), batchedDataGroup.getPlanBatch(), batchedDataGroup.getTaskIdentifier(), batchedDataGroup.getTaskBatch(),
                code, message, DateUtil.currentTimeMillis(), batchedDataGroup.getOwnerIdentifier(), dataJson));
    }
}
