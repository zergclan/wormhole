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
import com.zergclan.wormhole.console.application.domain.log.DataGroupExecutionLog;
import com.zergclan.wormhole.console.application.service.log.LogMetricsService;
import com.zergclan.wormhole.pipeline.event.DataGroupExecutionEvent;
import com.zergclan.wormhole.common.WormholeEventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Data group event listener.
 */
@Component
public final class DataGroupEventListener implements WormholeEventListener<DataGroupExecutionEvent> {

    @Resource
    private LogMetricsService logMetricsService;

    @Subscribe
    @Override
    public synchronized void onEvent(final DataGroupExecutionEvent event) {
        DataGroupExecutionLog dataGroupExecutionLog = new DataGroupExecutionLog(event);
        logMetricsService.add(dataGroupExecutionLog);
    }
}
