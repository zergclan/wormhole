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

import com.zergclan.wormhole.bus.memory.WormholeEventBus;
import com.zergclan.wormhole.common.data.BatchedDataGroup;
import com.zergclan.wormhole.common.data.result.ErrorDataGroup;
import com.zergclan.wormhole.common.data.result.LoadResult;
import com.zergclan.wormhole.common.WormholeResult;
import com.zergclan.wormhole.pipeline.event.ErrorDataEvent;
import com.zergclan.wormhole.plugin.loader.Loader;
import com.zergclan.wormhole.tool.util.DateUtil;

import com.zergclan.wormhole.pipeline.event.DataGroupExecutionEvent;
import lombok.RequiredArgsConstructor;

/**
 * Loaded handler.
 */
@RequiredArgsConstructor
public final class LoadedHandler implements Handler<BatchedDataGroup> {
    
    private final Loader<BatchedDataGroup, WormholeResult<LoadResult>> loader;
    
    @Override
    public void handle(final BatchedDataGroup batchedDataGroup) {
        batchedDataGroup.clearErrors();
        try {
            LoadResult loadResult = loader.load(batchedDataGroup).getResult();
            loadResult.getErrorData().forEach(each -> handleErrorDataEvent(batchedDataGroup, each));
            WormholeEventBus.post(DataGroupExecutionEvent.buildCompleteEvent(batchedDataGroup.getTaskBatch(), batchedDataGroup.getBatchIndex(), loadResult));
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            WormholeEventBus.post(DataGroupExecutionEvent.buildErrorEvent(batchedDataGroup.getTaskBatch(), batchedDataGroup.getBatchIndex(), batchedDataGroup.getBatchSize()));
            ex.printStackTrace();
        }
    }
    
    private void handleErrorDataEvent(final BatchedDataGroup batchedDataGroup, final ErrorDataGroup errorDataGroup) {
        WormholeEventBus.post(new ErrorDataEvent(batchedDataGroup.getPlanIdentifier(), batchedDataGroup.getPlanBatch(), batchedDataGroup.getTaskIdentifier(), batchedDataGroup.getTaskBatch(),
                errorDataGroup.getCode(), errorDataGroup.getMessage(), DateUtil.currentTimeMillis(), batchedDataGroup.getOwnerIdentifier(), errorDataGroup.getDataGroup().toString()));
    }
    
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
