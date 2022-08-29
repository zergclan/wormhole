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

package com.zergclan.wormhole.bootstrap.scheduling.plan;

import com.zergclan.wormhole.bootstrap.scheduling.Trigger;
import com.zergclan.wormhole.tool.util.Validator;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Plan trigger.
 */
public interface PlanTrigger extends Trigger, Delayed {
    
    /**
     * Get identifier.
     *
     * @return identifier
     */
    String getIdentifier();

    /**
     * Get plan identifier.
     *
     * @return plan identifier
     */
    String getPlanIdentifier();

    @Override
    default int compareTo(final Delayed delayed) {
        Validator.notNull(delayed, "error: OneOffPlanTrigger compareTo arg delayed can not be null");
        if (getDelay(TimeUnit.MILLISECONDS) > delayed.getDelay(TimeUnit.MILLISECONDS)) {
            return 1;
        } else if (getDelay(TimeUnit.MILLISECONDS) < delayed.getDelay(TimeUnit.MILLISECONDS)) {
            return -1;
        } else {
            return 0;
        }
    }
}
