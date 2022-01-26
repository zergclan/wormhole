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

import com.zergclan.wormhole.api.FilterChain;
import com.zergclan.wormhole.api.Handler;
import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.pipeline.data.BatchedDataGroup;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;

/**
 * Splitting implemented of {@link Handler}.
 */
@RequiredArgsConstructor
public final class SplittingHandler implements Handler<BatchedDataGroup> {

    private final Integer order;
    
    private final FilterChain<DataGroup> filterChain;

    private final Handler<BatchedDataGroup> nextHandler;
    
    @Override
    public void handle(final BatchedDataGroup batchedDataGroup) {
        Iterator<DataGroup> iterator = batchedDataGroup.getSourceDataGroup().iterator();
        while (iterator.hasNext()) {
            split(iterator.next(), batchedDataGroup);
        }
        nextHandler.handle(batchedDataGroup);
    }
    
    private void split(final DataGroup dataGroup, final BatchedDataGroup batchedDataGroup) {
        if (!filterChain.doFilter(dataGroup)) {
            batchedDataGroup.clearError(dataGroup);
        }
    }
    
    @Override
    public int getOrder() {
        return order;
    }
}
