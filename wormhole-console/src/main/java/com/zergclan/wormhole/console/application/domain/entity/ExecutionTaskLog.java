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

package com.zergclan.wormhole.console.application.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * {@link ExecutionTaskLog}.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class ExecutionTaskLog extends AbstractPO {

    private static final long serialVersionUID = 1588145013457477060L;

    private Integer id;

    private Long planBatch;

    private Integer planId;

    private Integer status;

    private String description;

    private Long taskBatch;

    private Integer taskId;

    private Integer transDataNum;

    private Integer errorDataNum;

}
