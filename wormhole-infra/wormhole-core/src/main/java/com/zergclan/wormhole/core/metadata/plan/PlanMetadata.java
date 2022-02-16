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

package com.zergclan.wormhole.core.metadata.plan;

import com.zergclan.wormhole.core.metadata.Metadata;
import com.zergclan.wormhole.core.metadata.Refreshable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Plan metadata.
 */
@RequiredArgsConstructor
@Getter
public final class PlanMetadata implements Metadata, Refreshable<TaskMetadata> {
    
    private final String code;
    
    private final ExecutionMode executionMode;
    
    private final String executionCorn;
    
    private final LocalDateTime effectiveDate;
    
    private final AtomicBoolean execute;
    
    private final AtomicBoolean enable;
    
    private final AtomicLong executionCount;

    private final Map<String, TaskMetadata> tasks = new LinkedHashMap<>();
    
    @Override
    public String getIdentifier() {
        return code;
    }
    
    /**
     * Register {@link TaskMetadata}.
     *
     * @param taskMetadata {@link TaskMetadata}
     * @return is registered or not
     */
    public Metadata register(final TaskMetadata taskMetadata) {
        return tasks.put(taskMetadata.getIdentifier(), taskMetadata);
    }
    
    @Override
    public boolean refresh(final TaskMetadata taskMetadata) {
        tasks.put(taskMetadata.getIdentifier(), taskMetadata);
        return true;
    }
}
