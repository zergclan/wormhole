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

import com.zergclan.wormhole.common.exception.WormholeException;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.core.api.metadata.DataSourceMetaData;
import com.zergclan.wormhole.core.api.metadata.MetaData;
import com.zergclan.wormhole.core.metadata.catched.CachedPlanMetaData;
import com.zergclan.wormhole.core.metadata.plan.PlanMetaData;
import com.zergclan.wormhole.core.metadata.resource.SchemaMetaData;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Root implemented {@link MetaData} in wormhole project.
 */
@Getter
public final class WormholeMetaData implements MetaData {

    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
    
    private final Map<String, DataSourceMetaData> dataSources;

    private final Map<String, PlanMetaData> plans;

    public WormholeMetaData(final Map<String, DataSourceMetaData> dataSources, final Map<String, PlanMetaData> plans) {
        this.dataSources = dataSources;
        this.plans = plans;
    }
    
    /**
     * Get {@link PlanMetaData} by plan identifier.
     *
     * @param planIdentifier plan identifier
     * @return {@link PlanMetaData}
     */
    public Optional<PlanMetaData> getPlan(final String planIdentifier) {
        Validator.notNull(planIdentifier, "error : get plan metadata plan identifier can not be null");
        LOCK.readLock().lock();
        try {
            PlanMetaData planMetadata = plans.get(planIdentifier);
            return null == planMetadata ? Optional.empty() : Optional.of(planMetadata);
        } finally {
            LOCK.readLock().unlock();
        }
    }
    
    /**
     * Get {@link CachedPlanMetaData} by plan identifier.
     *
     * @param planIdentifier plan identifier
     * @return {@link CachedPlanMetaData}
     */
    public Optional<CachedPlanMetaData> cachedMetadata(final String planIdentifier) {
        Validator.notNull(planIdentifier, "error : cached plan metadata plan identifier can not be null");
        LOCK.writeLock().lock();
        try {
            PlanMetaData planMetadata = plans.get(planIdentifier);
            return null == planMetadata ? Optional.empty() : Optional.of(CachedPlanMetaData.builder(planMetadata, dataSources));
        } finally {
            LOCK.writeLock().unlock();
        }
    }
    
    /**
     * Register {@link MetaData}.
     *
     * @param metadata {@link MetaData}
     * @return is registered or not
     */
    public boolean register(final MetaData metadata) {
        Validator.notNull(metadata, "error : metadata can not be null");
        final ReentrantReadWriteLock.WriteLock writeLock = LOCK.writeLock();
        writeLock.lock();
        try {
            if (metadata instanceof DataSourceMetaData) {
                return register((DataSourceMetaData) metadata);
            }
            if (metadata instanceof SchemaMetaData) {
                return register((SchemaMetaData) metadata);
            }
            if (metadata instanceof PlanMetaData) {
                return register((PlanMetaData) metadata);
            }
            throw new WormholeException("error : register metadata failed [%s] not find", metadata.getIdentifier());
        } finally {
            writeLock.unlock();
        }
    }
    
    private boolean register(final DataSourceMetaData dataSourceMetaData) {
        dataSources.put(dataSourceMetaData.getIdentifier(), dataSourceMetaData);
        return true;
    }
    
    private boolean register(final SchemaMetaData schemaMetadata) {
        dataSources.get(schemaMetadata.getDataSourceIdentifier()).registerSchema(schemaMetadata);
        return true;
    }
    
    private boolean register(final PlanMetaData planMetadata) {
        plans.put(planMetadata.getIdentifier(), planMetadata);
        return true;
    }
    
    @Override
    public String getIdentifier() {
        return "wormhole";
    }
}
