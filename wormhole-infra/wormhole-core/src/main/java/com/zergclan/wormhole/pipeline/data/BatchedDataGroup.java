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

package com.zergclan.wormhole.pipeline.data;

import com.zergclan.wormhole.core.data.DataGroup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

@RequiredArgsConstructor
public final class BatchedDataGroup {
    
    private Long planBatchId;
    
    private Long taskBatchId;
    
    private final int batchSize;
    
    private final Collection<DataGroup> errorData = new LinkedList<>();
    
    @Getter
    private final Collection<DataGroup> sourceData = new LinkedList<>();
    
    public void init(final Collection<Map<String, Object>> sourceData) {
        for (Map<String, Object> each : sourceData) {
            this.sourceData.add(DefaultDataGroupSwapper.mapToDataGroup(each));
        }
    }
    
    public void clearError(final DataGroup dataGroup) {
        sourceData.remove(dataGroup);
        errorData.add(dataGroup);
    }
}
