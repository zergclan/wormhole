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

package com.zergclan.wormhole.data.event;

import com.zergclan.wormhole.bus.api.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Error data event.
 */
@RequiredArgsConstructor
@Getter
public final class ErrorDataEvent implements Event {
    
    private static final long serialVersionUID = -3777226091757149095L;
    
    private final String planIdentifier;
    
    private final Long planBatch;
    
    private final String taskIdentifier;
    
    private final Long taskBatch;
    
    private final String code;
    
    private final String message;
    
    private final Long createTimestamp;
    
    private final String ownerIdentifier;
    
    private final String dataJson;
}
