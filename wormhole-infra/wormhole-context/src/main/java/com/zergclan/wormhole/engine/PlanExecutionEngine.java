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

package com.zergclan.wormhole.engine;

import com.zergclan.wormhole.context.PlanContext;
import com.zergclan.wormhole.core.api.metadata.Metadata;
import com.zergclan.wormhole.core.metadata.WormholeMetadata;
import com.zergclan.wormhole.core.metadata.catched.CachedPlanMetadata;
import com.zergclan.wormhole.scheduling.plan.PlanExecutorFactory;
import com.zergclan.wormhole.scheduling.plan.PlanTrigger;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Plan execution engine.
 */
@RequiredArgsConstructor
public final class PlanExecutionEngine {

    private final WormholeMetadata wormholeMetadata;

    private final PlanContext planContext = new PlanContext();
    
    /**
     * Register {@link Metadata}.
     *
     * @param metadata {@link Metadata}
     * @return is register or not
     */
    public boolean register(final Metadata metadata) {
        return wormholeMetadata.register(metadata);
    }
    
    /**
     * Execute by plan trigger.
     *
     * @param planTrigger {@link PlanTrigger}
     */
    public void execute(final PlanTrigger planTrigger) {
        Optional<CachedPlanMetadata> cachedPlanMetadata = planContext.cachedMetadata(wormholeMetadata, planTrigger);
        cachedPlanMetadata.ifPresent(planMetadata -> PlanExecutorFactory.create(planMetadata).execute());
    }
}
