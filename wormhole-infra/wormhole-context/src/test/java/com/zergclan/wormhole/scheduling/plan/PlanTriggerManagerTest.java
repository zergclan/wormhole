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

import com.zergclan.wormhole.core.metadata.plan.PlanMetaData;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class PlanTriggerManagerTest {

    @Test
    public void assertRegisterTrigger() {
        PlanTriggerManager planTriggerManager = new PlanTriggerManager();
        PlanMetaData planMetadata = new PlanMetaData("test_plan", PlanMetaData.ExecutionMode.SCHEDULED, "*/4 * * * * ?", true, new LinkedHashMap<>());
        planTriggerManager.register(planMetadata);
        for (int i = 0; i < 2; i++) {
            Optional<PlanTrigger> executableTrigger = planTriggerManager.getExecutableTrigger();
            if (executableTrigger.isPresent()) {
                PlanTrigger planTrigger = executableTrigger.get();
                assertEquals("SCHEDULED#test_plan:*/4 * * * * ?", planTrigger.getIdentifier());
            }
        }
    }
}
