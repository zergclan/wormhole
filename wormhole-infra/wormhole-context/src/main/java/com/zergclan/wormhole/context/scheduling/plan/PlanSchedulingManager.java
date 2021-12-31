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

package com.zergclan.wormhole.context.scheduling.plan;

import com.zergclan.wormhole.context.scheduling.SchedulingExecutor;
import com.zergclan.wormhole.context.scheduling.SchedulingExecutorFactory;
import com.zergclan.wormhole.context.scheduling.SchedulingManager;
import com.zergclan.wormhole.context.scheduling.SchedulingTrigger;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Plan implemented of {@link SchedulingManager}.
 */
public final class PlanSchedulingManager implements SchedulingManager<PlanDefinition> {

    private static final Queue<SchedulingTrigger> SCHEDULING_QUEUE = new PriorityQueue<>((trigger1, trigger2) -> (int) (trigger1.getExpire() - trigger2.getExpire()));

    private static final Map<String, SchedulingTrigger> EXECUTION_CONTAINER = new ConcurrentHashMap<>(16);

    @Override
    public boolean register(final PlanDefinition definition) {
        return SCHEDULING_QUEUE.offer(createSchedulingTrigger(definition));
    }

    @Override
    public void onTrigger() {
        for (;;) {
            SchedulingTrigger trigger = SCHEDULING_QUEUE.peek();
            if (null != trigger) {
                String planCode = trigger.getCode();
                if (null == EXECUTION_CONTAINER.get(planCode)) {
                    EXECUTION_CONTAINER.put(planCode, SCHEDULING_QUEUE.poll());
                    SchedulingExecutor schedulingExecutor = SchedulingExecutorFactory.createSchedulingExecutor(trigger);
                    schedulingExecutor.execute();
                }
            }
        }
    }

    private SchedulingTrigger createSchedulingTrigger(final PlanDefinition definition) {
        // TODO create trigger by definition
        return null;
    }
}
