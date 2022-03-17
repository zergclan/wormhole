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

package com.zergclan.wormhole.scheduling;

import com.zergclan.wormhole.common.util.DateUtil;
import com.zergclan.wormhole.scheduling.plan.OneOffPlanTrigger;
import com.zergclan.wormhole.scheduling.plan.ScheduledPlanTrigger;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TriggerManagerTest {
    
    @Test
    public void assertRegisterTrigger() {
        TriggerManager.register(createOneOffPlanTrigger());
        TriggerManager.register(createScheduledPlanTrigger());
        for (int i = 0; i < 5; i++) {
            Optional<Trigger> trigger = TriggerManager.getExecutableTrigger();
            if (trigger.isPresent()) {
                Trigger executableTrigger = trigger.get();
                assertEquals("SCHEDULED#test_plan:*/15 * * * * ?", executableTrigger.getIdentifier());
            }
        }
    }
    
    private ScheduledPlanTrigger createScheduledPlanTrigger() {
        return new ScheduledPlanTrigger("test_plan", "*/15 * * * * ?");
    }
    
    private OneOffPlanTrigger createOneOffPlanTrigger() {
        return new OneOffPlanTrigger("test_plan", "2022-03-17 15:39:02");
    }
}
