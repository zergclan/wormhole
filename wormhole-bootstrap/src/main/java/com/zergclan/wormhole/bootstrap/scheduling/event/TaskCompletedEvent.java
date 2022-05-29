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

package com.zergclan.wormhole.bootstrap.scheduling.event;

import com.zergclan.wormhole.bus.api.Event;
import com.zergclan.wormhole.common.constant.MarkConstant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Task completed event.
 */
@RequiredArgsConstructor
@Getter
public final class TaskCompletedEvent implements Event {
    
    private static final long serialVersionUID = -5984266540485867999L;
    
    private final String taskIdentifier;
    
    /**
     * Get plan identifier.
     *
     * @return plan identifier
     */
    public String getPlanIdentifier() {
        String[] split = taskIdentifier.split(MarkConstant.SPACE);
        return split[0];
    }
}
