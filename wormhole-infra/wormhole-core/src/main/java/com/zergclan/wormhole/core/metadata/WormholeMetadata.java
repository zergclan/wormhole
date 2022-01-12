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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Root implemented {@link MetaData} in wormhole project.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class WormholeMetadata implements MetaData, Cloneable, Refreshable<WormholeMetadata> {
    
    private final Map<String, DataSourceMetaData> dataSources = new LinkedHashMap<>();
    
    private final Map<String, PlanMetaData> plans = new LinkedHashMap<>();
    
    private WormholeMetadata(final Map<String, DataSourceMetaData> dataSources, final Map<String, PlanMetaData> plans) {
        this.dataSources.putAll(dataSources);
        this.plans.putAll(plans);
    }
    
    /**
     * Register {@link DataSourceMetaData}.
     *
     * @param dataSourceMetaData {@link DataSourceMetaData}
     * @return is registered or not
     */
    public MetaData register(final DataSourceMetaData dataSourceMetaData) {
        return dataSources.put(dataSourceMetaData.getIdentifier(), dataSourceMetaData);
    }
    
    /**
     * Register {@link PlanMetaData}.
     *
     * @param planMetaData {@link PlanMetaData}
     * @return is registered or not
     */
    public MetaData register(final PlanMetaData planMetaData) {
        return plans.put(planMetaData.getIdentifier(), planMetaData);
    }
    
    @Override
    public String getIdentifier() {
        return "wormhole";
    }
    
    @Override
    protected WormholeMetadata clone() throws CloneNotSupportedException {
        super.clone();
        return new WormholeMetadata(this.dataSources, this.plans);
    }
    
    @Override
    public boolean refresh(final WormholeMetadata wormholeMetadata) {
        return refreshResources(wormholeMetadata.getDataSources()) && refreshPlans(wormholeMetadata.getPlans());
    }
    
    private boolean refreshResources(final Map<String, DataSourceMetaData> dataSources) {
        for (Map.Entry<String, DataSourceMetaData> entry : dataSources.entrySet()) {
            register(entry.getValue());
        }
        return true;
    }
    
    private boolean refreshPlans(final Map<String, PlanMetaData> plans) {
        for (Map.Entry<String, PlanMetaData> entry : plans.entrySet()) {
            register(entry.getValue());
        }
        return true;
    }
}
