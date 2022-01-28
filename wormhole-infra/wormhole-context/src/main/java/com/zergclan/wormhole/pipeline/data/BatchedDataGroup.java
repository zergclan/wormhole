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

import com.zergclan.wormhole.api.Swapper;
import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.pipeline.swapper.MySQLDataGroupSwapper;
import lombok.Getter;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Batched {@link DataGroup}.
 */
@Getter
public final class BatchedDataGroup {
    
    private static final Swapper<Map<String, Object>, DataGroup> SWAPPER = new MySQLDataGroupSwapper();
    
    private final Long planBatchId;
    
    private final Long taskBatchId;
    
    private final int totalSize;
    
    private final Collection<DataGroup> dataGroups = new LinkedList<>();

    public BatchedDataGroup(final Long planBatchId, final Long taskBatchId, final Collection<Map<String, Object>> data) {
        this.planBatchId = planBatchId;
        this.taskBatchId = taskBatchId;
        this.totalSize = data.size();
        init(data);
    }

    private void init(final Collection<Map<String, Object>> data) {
        Iterator<Map<String, Object>> iterator = data.iterator();
        while (iterator.hasNext()) {
            dataGroups.add(SWAPPER.swapToTarget(iterator.next()));
        }
    }

    /**
     * Clear error.
     *
     * @param dataGroup data group
     */
    public void clearError(final DataGroup dataGroup) {
        dataGroups.remove(dataGroup);
    }
}
