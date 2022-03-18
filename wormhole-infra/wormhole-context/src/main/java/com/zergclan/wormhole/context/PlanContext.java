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

package com.zergclan.wormhole.context;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zergclan.wormhole.core.metadata.WormholeMetadata;
import com.zergclan.wormhole.core.metadata.catched.CachedPlanMetadata;
import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import com.zergclan.wormhole.scheduling.plan.PlanTrigger;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Plan context.
 */
public final class PlanContext {
    
    private final Cache<String, CachedPlanMetadata> cachedMetadata = Caffeine.newBuilder().initialCapacity(1).maximumSize(100).expireAfterWrite(1, TimeUnit.DAYS).build();
    
    /**
     * Is executing plan.
     *
     * @param planIdentifier plan identifier
     * @return is executing or not
     */
    public boolean isExecuting(final String planIdentifier) {
        return false;
    }
    
    /**
     * Cached plan metadata.
     *
     * @param wormholeMetadata {@link WormholeMetadata}
     * @param planTrigger {@link PlanTrigger}
     * @return {@link CachedPlanMetadata}
     */
    public Optional<CachedPlanMetadata> cachedMetadata(final WormholeMetadata wormholeMetadata, final PlanTrigger planTrigger) {
        String planIdentifier = planTrigger.getPlanIdentifier();
        if (isExecuting(planIdentifier)) {
            // TODO send event
            return Optional.empty();
        }
        Optional<PlanMetadata> plan = wormholeMetadata.getPlan(planIdentifier);
        if (plan.isPresent()) {
            CachedPlanMetadata planMetadata = CachedPlanMetadata.builder(plan.get(), wormholeMetadata.getDataSources());
            cachedMetadata.put(planMetadata.getIdentifier(), planMetadata);
            return Optional.of(planMetadata);
        }
        // TODO send event
        return Optional.empty();
    }
}
