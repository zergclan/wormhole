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

import lombok.Getter;

import java.util.Optional;

/**
 * Mode of plan execution.
 */
@Getter
public enum ExecutionMode {
    
    ONE_OFF("oneOff"),
    
    SCHEDULED("scheduled");
    
    private final String type;

    ExecutionMode(final String type) {
        this.type = type;
    }

    /**
     * Get {@link ExecutionMode} by type.
     *
     * @param type type
     * @return {@link ExecutionMode}
     */
    public static Optional<ExecutionMode> getExecutionMode(final String type) {
        if (ONE_OFF.type.equals(type)) {
            return Optional.of(ONE_OFF);
        } else if (SCHEDULED.type.equals(type)) {
            return Optional.of(SCHEDULED);
        }
        return Optional.empty();
    }
}
