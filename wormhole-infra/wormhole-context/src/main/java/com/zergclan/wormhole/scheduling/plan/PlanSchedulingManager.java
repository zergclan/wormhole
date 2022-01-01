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

package com.zergclan.wormhole.scheduling.plan;

import com.zergclan.wormhole.definition.PlanDefinition;
import com.zergclan.wormhole.scheduling.SchedulingExecutorFactory;
import com.zergclan.wormhole.scheduling.SchedulingManager;
import com.zergclan.wormhole.scheduling.SchedulingTrigger;

import java.util.Queue;
import java.util.concurrent.DelayQueue;

/**
 * Plan implemented of {@link SchedulingManager}.
 */
public final class PlanSchedulingManager implements SchedulingManager<PlanDefinition> {

    private static final Queue<SchedulingTrigger> DELAY_QUEUE = new DelayQueue<>();

    @Override
    public boolean register(final PlanDefinition definition) {
        return DELAY_QUEUE.offer(createSchedulingTrigger(definition));
    }

    @Override
    public boolean execute(final PlanDefinition definition) {
        SchedulingExecutorFactory.createSchedulingExecutor(createSchedulingTrigger(definition)).execute();
        return true;
    }

    @Override
    public void onTrigger() {
        for (;;) {
            SchedulingTrigger trigger = DELAY_QUEUE.peek();
            if (null != trigger) {
                SchedulingExecutorFactory.createSchedulingExecutor(trigger).execute();
            }
        }
    }

    private SchedulingTrigger createSchedulingTrigger(final PlanDefinition definition) {
        // TODO create trigger by definition
        return null;
    }
}
