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

import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.core.data.DataNode;
import com.zergclan.wormhole.pipeline.DataGroupHandler;
import com.zergclan.wormhole.pipeline.DataNodeFilter;
import com.zergclan.wormhole.pipeline.data.BatchedDataGroup;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public final class CheckedDataGroupHandler implements DataGroupHandler<BatchedDataGroup> {
    
    private final Integer order;
    
    private final DataGroupHandler<BatchedDataGroup> dataGroupHandler;
    
    private final Map<String, Collection<DataNodeFilter<?>>> filters = new LinkedHashMap<>();
    
    @Override
    public void handle(final BatchedDataGroup batchedDataGroup) {
        Collection<DataGroup> sourceData = batchedDataGroup.getSourceData();
        for (DataGroup each : sourceData) {
            if (!checkDataGroup(each)) {
                batchedDataGroup.clearError(each);
            }
        }
        dataGroupHandler.handle(batchedDataGroup);
    }
    
    private boolean checkDataGroup(final DataGroup dataGroup) {
        for (Map.Entry<String, Collection<DataNodeFilter<?>>> entry : filters.entrySet()) {
            if (!checkDataNode(dataGroup.getDataNode(entry.getKey()), entry.getValue())) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkDataNode(final DataNode dataNode, final Collection<DataNodeFilter<?>> filters) {
        boolean result = false;
        for (DataNodeFilter<?> each : filters) {
            result = each.doFilter(dataNode);
        }
    }
    
    @Override
    public int getOrder() {
        return 0;
    }
}
