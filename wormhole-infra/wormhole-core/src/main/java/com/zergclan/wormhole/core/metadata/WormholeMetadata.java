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

package com.zergclan.wormhole.core.metadata;

import com.zergclan.wormhole.common.WormholeException;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.core.metadata.catched.CachedPlanMetadata;
import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import com.zergclan.wormhole.core.metadata.resource.SchemaMetadata;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Root implemented {@link Metadata} in wormhole project.
 */
public final class WormholeMetadata implements Metadata {

    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
    
    private final Map<String, DataSourceMetadata> dataSources;

    @Getter
    private final Map<String, PlanMetadata> plans;

    public WormholeMetadata(final Map<String, DataSourceMetadata> dataSources, final Map<String, PlanMetadata> plans) {
        this.dataSources = dataSources;
        this.plans = plans;
    }
    
    /**
     * Get {@link PlanMetadata} by plan identifier.
     *
     * @param planIdentifier plan identifier
     * @return {@link PlanMetadata}
     */
    public Optional<PlanMetadata> get(final String planIdentifier) {
        Validator.notNull(planIdentifier, "error : get plan metadata plan identifier can not be null");
        LOCK.readLock().lock();
        try {
            PlanMetadata planMetadata = plans.get(planIdentifier);
            return null == planMetadata ? Optional.empty() : Optional.of(planMetadata);
        } finally {
            LOCK.readLock().unlock();
        }
    }
    
    /**
     * Get {@link CachedPlanMetadata} by plan identifier.
     *
     * @param planIdentifier plan identifier
     * @return {@link CachedPlanMetadata}
     */
    public Optional<CachedPlanMetadata> cachedMetadata(final String planIdentifier) {
        Validator.notNull(planIdentifier, "error : cached plan metadata plan identifier can not be null");
        LOCK.writeLock().lock();
        try {
            PlanMetadata planMetadata = plans.get(planIdentifier);
            return null == planMetadata ? Optional.empty() : Optional.of(CachedPlanMetadata.builder(planMetadata, dataSources));
        } finally {
            LOCK.writeLock().unlock();
        }
    }
    
    /**
     * Register {@link Metadata}.
     *
     * @param metadata {@link Metadata}
     * @return is registered or not
     */
    public boolean register(final Metadata metadata) {
        Validator.notNull(metadata, "error : metadata can not be null");
        final ReentrantReadWriteLock.WriteLock writeLock = LOCK.writeLock();
        writeLock.lock();
        try {
            if (metadata instanceof DataSourceMetadata) {
                return register((DataSourceMetadata) metadata);
            }
            if (metadata instanceof SchemaMetadata) {
                return register((SchemaMetadata) metadata);
            }
            if (metadata instanceof PlanMetadata) {
                return register((PlanMetadata) metadata);
            }
            throw new WormholeException("error : register metadata failed [%s] not find", metadata.getIdentifier());
        } finally {
            writeLock.unlock();
        }
    }
    
    private boolean register(final DataSourceMetadata dataSourceMetaData) {
        dataSources.put(dataSourceMetaData.getIdentifier(), dataSourceMetaData);
        return true;
    }
    
    private boolean register(final SchemaMetadata schemaMetadata) {
        dataSources.get(schemaMetadata.getDataSourceIdentifier()).registerSchema(schemaMetadata);
        return true;
    }
    
    private boolean register(final PlanMetadata planMetadata) {
        plans.put(planMetadata.getIdentifier(), planMetadata);
        return true;
    }
    
    @Override
    public String getIdentifier() {
        return "wormhole";
    }
}
