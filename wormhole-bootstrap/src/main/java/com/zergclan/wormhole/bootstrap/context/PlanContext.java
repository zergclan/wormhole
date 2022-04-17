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

package com.zergclan.wormhole.bootstrap.context;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zergclan.wormhole.bootstrap.scheduling.plan.PlanTrigger;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.WormholeMetaData;
import com.zergclan.wormhole.metadata.core.catched.CachedPlanMetaData;
import com.zergclan.wormhole.metadata.core.plan.PlanMetaData;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Plan context.
 */
public final class PlanContext {
    
    private final Cache<String, CachedPlanMetaData> cachedMetadata = Caffeine.newBuilder().initialCapacity(1).maximumSize(100).expireAfterWrite(1, TimeUnit.DAYS).build();
    
    /**
     * Is executing plan.
     *
     * @param planIdentifier plan identifier
     * @return is executing or not
     */
    public boolean isExecuting(final String planIdentifier) {
        return cachedMetadata.asMap().containsKey(planIdentifier);
    }
    
    /**
     * Create {@link CachedPlanMetaData}.
     *
     * @param wormholeMetaData {@link WormholeMetaData}
     * @param planTrigger plan identifier
     * @return {@link CachedPlanMetaData}
     */
    public Optional<CachedPlanMetaData> cachedMetadata(final WormholeMetaData wormholeMetaData, final PlanTrigger planTrigger) {
        String planIdentifier = planTrigger.getPlanIdentifier();
        if (isExecuting(planIdentifier)) {
            /**
             * TODO send plan is executing event by @gz
             */
            return Optional.empty();
        }
        Optional<PlanMetaData> plan = wormholeMetaData.getPlan(planIdentifier);
        if (!plan.isPresent()) {
            // TODO fix me with exception
            return Optional.empty();
        }
        return cachedMetaData(plan.get(), wormholeMetaData.getDataSources());
    }
    
    private Optional<CachedPlanMetaData> cachedMetaData(final PlanMetaData planMetaData, final Map<String, DataSourceMetaData> dataSources) {
        try {
            CachedPlanMetaData planMetadata = CachedPlanMetaData.builder(planMetaData, dataSources);
            cachedMetadata.put(planMetadata.getIdentifier(), planMetadata);
            return Optional.of(planMetadata);
        } catch (final SQLException ex) {
            // TODO send plan cached metaData failed
            return Optional.empty();
        }
    }
}
