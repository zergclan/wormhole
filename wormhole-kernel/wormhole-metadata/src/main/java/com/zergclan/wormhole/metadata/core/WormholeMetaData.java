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

package com.zergclan.wormhole.metadata.core;

import com.zergclan.wormhole.common.exception.WormholeException;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.api.MetaData;
import com.zergclan.wormhole.metadata.core.plan.PlanMetaData;
import lombok.Getter;

import java.util.LinkedHashMap;
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
        Map<String, DataSourceMetaData> dataSourceMetaData = new LinkedHashMap<>();
        dataSources.forEach((key, value) -> {
            dataSourceMetaData.put(value.getIdentifier(), value);
        });
        this.dataSources = dataSourceMetaData;
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
            return Optional.ofNullable(plans.get(planIdentifier));
        } finally {
            LOCK.readLock().unlock();
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
    
    private boolean register(final PlanMetaData planMetadata) {
        plans.put(planMetadata.getIdentifier(), planMetadata);
        return true;
    }
    
    @Override
    public String getIdentifier() {
        return "wormhole";
    }
}
