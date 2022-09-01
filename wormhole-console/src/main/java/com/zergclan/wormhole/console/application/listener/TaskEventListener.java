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

package com.zergclan.wormhole.console.application.listener;

import com.google.common.eventbus.Subscribe;
import com.zergclan.wormhole.bootstrap.scheduling.ExecutionStep;
import com.zergclan.wormhole.bootstrap.scheduling.event.TaskExecutionEvent;
import com.zergclan.wormhole.common.WormholeEventListener;
import com.zergclan.wormhole.console.application.domain.log.TaskExecutionLog;
import com.zergclan.wormhole.console.application.service.log.LogMetricsService;
import com.zergclan.wormhole.console.infra.util.BeanMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Task event listener.
 */
@Component
public final class TaskEventListener implements WormholeEventListener<TaskExecutionEvent> {
    
    @Resource
    private LogMetricsService logMetricsService;
    
    @Subscribe
    @Override
    public void onEvent(final TaskExecutionEvent event) {
        TaskExecutionLog taskLog = new TaskExecutionLog();
        BeanMapper.shallowCopy(event, taskLog);
        ExecutionStep executionStep = event.getExecutionStep();
        taskLog.setExecutionStep(executionStep.name());
        taskLog.setExecutionState(event.getExecutionState().name());
        if (ExecutionStep.NEW == executionStep) {
            logMetricsService.add(taskLog);
        } else {
            logMetricsService.syncExecutionLog(taskLog);
        }
    }
}
