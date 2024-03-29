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

package com.zergclan.wormhole.pipeline.event;

import com.zergclan.wormhole.common.data.result.LoadResultData;
import com.zergclan.wormhole.common.WormholeEvent;
import com.zergclan.wormhole.tool.util.DateUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Data group execution event.
 */
@RequiredArgsConstructor
@Getter
public final class DataGroupExecutionEvent implements WormholeEvent {

    private static final long serialVersionUID = 8476838490487796654L;
    
    private final String planIdentifier;
    
    private final Long planBatch;
    
    private final String taskIdentifier;
    
    private final Long taskBatch;
    
    private final Integer batchIndex;
    
    private final Integer totalRow;
    
    private final Integer insertRow;
    
    private final Integer updateRow;
    
    private final Integer errorRow;
    
    private final Integer sameRow;
    
    private final Long createTimestamp;
    
    private final Long endTimestamp;
    
    /**
     * Build new {@link DataGroupExecutionEvent}.
     *
     * @param planIdentifier plan identifier
     * @param planBatch plan batch
     * @param taskIdentifier task identifier
     * @param taskBatch task batch
     * @param batchIndex batch index
     * @param totalRow total row
     * @return {@link DataGroupExecutionEvent}
     */
    public static DataGroupExecutionEvent buildNewEvent(final String planIdentifier, final long planBatch, final String taskIdentifier, final long taskBatch,
                                                             final int batchIndex, final int totalRow) {
        return new DataGroupExecutionEvent(planIdentifier, planBatch, taskIdentifier, taskBatch, batchIndex, totalRow, -1, -1, -1, -1, DateUtil.currentTimeMillis(), 0L);
    }
    
    /**
     * Build complete {@link DataGroupExecutionEvent}.
     *
     * @param taskBatch task batch
     * @param batchIndex batch index
     * @param loadResult {@link LoadResultData}
     * @return {@link DataGroupExecutionEvent}
     */
    public static DataGroupExecutionEvent buildCompleteEvent(final long taskBatch, final int batchIndex, final LoadResultData loadResult) {
        return new DataGroupExecutionEvent(null, null, null, taskBatch, batchIndex, loadResult.getTotalRow(), loadResult.getInsertRow(), loadResult.getUpdateRow(), loadResult.getErrorRow(),
                loadResult.getSameRow(), null, DateUtil.currentTimeMillis());
    }
    
    /**
     * Build error {@link DataGroupExecutionEvent}.
     *
     * @param taskBatch task batch
     * @param batchIndex batch index
     * @param errorRow error row
     * @return {@link DataGroupExecutionEvent}
     */
    public static WormholeEvent buildErrorEvent(final long taskBatch, final int batchIndex, final int errorRow) {
        return new DataGroupExecutionEvent(null, null, null, taskBatch, batchIndex, null, null, null, errorRow, null, null, DateUtil.currentTimeMillis());
    }
}
