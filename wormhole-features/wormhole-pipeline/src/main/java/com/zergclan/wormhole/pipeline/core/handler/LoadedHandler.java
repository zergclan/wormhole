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

package com.zergclan.wormhole.pipeline.core.handler;

import com.zergclan.wormhole.bus.memory.WormholeEventBus;
import com.zergclan.wormhole.common.util.DateUtil;
import com.zergclan.wormhole.data.api.result.Result;
import com.zergclan.wormhole.data.core.BatchedDataGroup;
import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.data.core.event.ErrorDataEvent;
import com.zergclan.wormhole.data.core.result.ErrorDataGroup;
import com.zergclan.wormhole.data.core.result.LoadResult;
import com.zergclan.wormhole.pipeline.api.Handler;
import com.zergclan.wormhole.pipeline.core.event.DataGroupExecutionEvent;
import com.zergclan.wormhole.plugin.api.Loader;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * Loaded handler.
 */
@RequiredArgsConstructor
public final class LoadedHandler implements Handler<BatchedDataGroup> {
    
    private final Loader<BatchedDataGroup, Result<LoadResult>> loader;
    
    @Override
    public void handle(final BatchedDataGroup batchedDataGroup) {
        Collection<DataGroup> errors = batchedDataGroup.getErrors();
        batchedDataGroup.getDataGroups().removeAll(errors);
        LoadResult loadResult = loader.load(batchedDataGroup).getResult();
        loadResult.getErrorData().forEach(each -> handleErrorDataEvent(batchedDataGroup, each));
        WormholeEventBus.post(DataGroupExecutionEvent.buildCompleteStateEvent(batchedDataGroup.getTaskBatch(), batchedDataGroup.getBatchIndex(), loadResult));
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
