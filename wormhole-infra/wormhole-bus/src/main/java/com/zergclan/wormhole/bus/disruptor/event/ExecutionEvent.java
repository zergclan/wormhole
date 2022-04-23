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

package com.zergclan.wormhole.bus.disruptor.event;

import com.zergclan.wormhole.bus.api.Event;
import com.zergclan.wormhole.common.util.DateUtil;
import lombok.Getter;

@Getter
public final class ExecutionEvent implements Event {
    
    private String identifier;
    
    private String type;
    
    private int state;
    
    private int total;
    
    private int success;
    
    private int error;
    
    private long timestamp;
    
    /**
     * Init.
     *
     * @param executionEvent {@link ExecutionEvent}
     */
    public void init(final ExecutionEvent executionEvent) {
        this.identifier = executionEvent.getIdentifier();
        this.type = executionEvent.getType();
        this.state = executionEvent.getState();
        this.total = executionEvent.getTotal();
        this.success = executionEvent.getSuccess();
        this.error = executionEvent.getError();
        this.timestamp = DateUtil.currentTimeMillis();
    }
    
    /**
     * Clean.
     */
    public void clean() {
        this.identifier = null;
        this.type = null;
        this.state = -1;
        this.total = -1;
        this.success = -1;
        this.error = -1;
        this.timestamp = -1L;
    }
}
