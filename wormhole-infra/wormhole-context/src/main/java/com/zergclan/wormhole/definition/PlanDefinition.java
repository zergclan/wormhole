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

package com.zergclan.wormhole.definition;

import com.zergclan.wormhole.common.WormholeException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Definition of plan.
 */
@NoArgsConstructor
@Getter
public final class PlanDefinition implements Serializable {

    private static final long serialVersionUID = -341262307232903018L;

    private String code;

    private ExecutionMode executionMode;

    private String executionCorn;

    private Integer operator;

    private Collection<TaskDefinition> taskDefinitions = new LinkedList<>();

    public PlanDefinition(final String code, final Integer executionModeCode, final String executionCorn, final Integer operator) {
        this.code = code;
        this.executionMode = createExecutionMode(executionModeCode);
        this.executionCorn = executionCorn;
        this.operator = operator;
    }

    private ExecutionMode createExecutionMode(final Integer executionModeCode) {
        Optional<ExecutionMode> executionMode = ExecutionMode.getExecutionMode(executionModeCode);
        if (executionMode.isPresent()) {
            return executionMode.get();
        }
        throw new WormholeException("error : execution mode not find by [%s]", executionMode);
    }

    /**
     * Register {@link TaskDefinition}.
     *
     * @param taskDefinition {@link TaskDefinition}
     * @return is registered or not
     */
    public boolean registerTask(final TaskDefinition taskDefinition) {
        return taskDefinitions.add(taskDefinition);
    }
}
