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

package com.zergclan.wormhole.context.scheduling;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implemented of {@link SchedulingManager}.
 */
public final class DefaultSchedulingManager implements SchedulingManager {
    
    private static final Map<String, SchedulingTrigger> TRIGGER_CONTAINER = new ConcurrentHashMap<>(16);
    
    /**
     * Register {@link SchedulingTrigger} for plan.
     *
     * @param trigger {@link SchedulingTrigger}
     * @return is registered or not
     */
    public boolean register(final SchedulingTrigger trigger) {
        TRIGGER_CONTAINER.put(trigger.getPlanCode(), trigger);
        return true;
    }
    
    @Override
    public boolean execute(final SchedulingTrigger trigger) {
        Collection<String> taskCodes = trigger.getTaskCodes();
        if (trigger.isExecutable()) {
            executeTasks(taskCodes);
        }
        return true;
    }
    
    private void executeTasks(final Collection<String> taskCodes) {
        SchedulingExecutor schedulingExecutor;
        for (String each : taskCodes) {
            schedulingExecutor = SchedulingExecutorFactory.createSchedulingExecutor(each);
            schedulingExecutor.execute();
        }
    }
}
