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
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * {@link ErrorDataLog}.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class ErrorDataLog extends AbstractPO {
    
    private static final long serialVersionUID = -685034037667839493L;
    
    private String taskIdentifier;
    
    private Long planBatch;
    
    private Long taskBatch;
    
    private String code;
    
    private String message;
    
    private Long createTimestamp;
    
    private String ownerIdentifier;
    
    private String dataJson;
}
