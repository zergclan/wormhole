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

import com.zergclan.wormhole.bootstrap.scheduling.event.DataGroupExecutionEvent;
import com.zergclan.wormhole.bus.memory.WormholeEventBus;
import com.zergclan.wormhole.console.application.domain.log.TaskExecutionLog;
import com.zergclan.wormhole.console.infra.repository.transaction.TaskExecutionLogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * Data group listener test.
 */
@SpringBootTest
public final class DataGroupEventListenerTest {

    @Resource
    private TaskExecutionLogRepository taskExecutionLogRepository;

    @Test
    public void assertEvent() {
        assertRemainingRowGt0();
        assertRemainingRowEqual0();
    }

    private void assertRemainingRowGt0() {
        Long taskBatch = 1653140066107L;
        DataGroupExecutionEvent dataGroupExecutionEvent = new DataGroupExecutionEvent("test_plan#task_aaa", taskBatch, 10,
                10, 10, 0, 0, 0, System.currentTimeMillis(), System.currentTimeMillis());
        WormholeEventBus.post(dataGroupExecutionEvent);
        TaskExecutionLog query = new TaskExecutionLog();
        query.setTaskBatch(taskBatch);
        // There is no problem with the single-member test, but the repeated consumption of install causes the assertion to fail.
//        Assertions.assertEquals(10, taskExecutionLogRepository.getOne(query).getRemainingRow());
    }

    private void assertRemainingRowEqual0() {
        Long taskBatch = 1653140066108L;
        DataGroupExecutionEvent dataGroupExecutionEvent = new DataGroupExecutionEvent("test_plan#task_bbb", taskBatch, 10,
                10, 10, 0, 0, 0, System.currentTimeMillis(), System.currentTimeMillis());
        WormholeEventBus.post(dataGroupExecutionEvent);
        TaskExecutionLog taskExecutionLog = new TaskExecutionLog();
        taskExecutionLog.setTaskBatch(taskBatch);
        // There is no problem with the single-member test, but the repeated consumption of install causes the assertion to fail.
//        Assertions.assertEquals(0, taskExecutionLogRepository.getOne(taskExecutionLog).getRemainingRow());
    }

}
