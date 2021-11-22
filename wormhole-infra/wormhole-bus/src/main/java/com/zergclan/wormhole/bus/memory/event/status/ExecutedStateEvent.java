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

package com.zergclan.wormhole.bus.memory.event.status;

import com.zergclan.wormhole.bus.api.event.WormholeEvent;

import java.io.Serializable;

/**
 * Event for executed state.
 */
public final class ExecutedStateEvent implements WormholeEvent, Serializable {
    
    private static final long serialVersionUID = 2263745384258957956L;
    
    private Long eventId;
    
    private ExecutedType businessType;
    
    private ExecutedStateType executedStateType;
    
    private Long timestamp;
    
    @Override
    public Object getSource() {
        return this;
    }
}
