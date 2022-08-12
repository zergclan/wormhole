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

package com.zergclan.wormhole.console.application.domain.log;

import com.zergclan.wormhole.console.application.domain.entity.AbstractPO;
import com.zergclan.wormhole.console.infra.util.BeanMapper;
import com.zergclan.wormhole.pipeline.event.DataGroupExecutionEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * {@link DataGroupExecutionLog}.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class DataGroupExecutionLog extends AbstractPO {
    
    private static final long serialVersionUID = 9145337134624590196L;
    
    private Long taskBatch;
    
    private Integer batchIndex;
    
    private Integer totalRow;
    
    private Integer insertRow;
    
    private Integer updateRow;
    
    private Integer errorRow;
    
    private Integer sameRow;
    
    private Long createTimestamp;
    
    private Long endTimestamp;
    
    public DataGroupExecutionLog(final DataGroupExecutionEvent dataGroupExecutionEvent) {
        BeanMapper.shallowCopy(dataGroupExecutionEvent, this);
    }
}
