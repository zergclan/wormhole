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

import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Root implemented {@link Metadata} in wormhole project.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WormholeMetadata implements Metadata {
    
    private final Map<String, DataSourceMetadata> dataSources = new LinkedHashMap<>();
    
    private final Map<String, PlanMetadata> plans = new LinkedHashMap<>();
    
    /**
     * Register {@link DataSourceMetadata}.
     *
     * @param dataSourceMetaData {@link DataSourceMetadata}
     * @return is registered or not
     */
    public Metadata register(final DataSourceMetadata dataSourceMetaData) {
        return dataSources.put(dataSourceMetaData.getIdentifier(), dataSourceMetaData);
    }
    
    /**
     * Register {@link PlanMetadata}.
     *
     * @param planMetadata {@link PlanMetadata}
     * @return is registered or not
     */
    public Metadata register(final PlanMetadata planMetadata) {
        return plans.put(planMetadata.getIdentifier(), planMetadata);
    }
    
    @Override
    public String getIdentifier() {
        return "wormhole";
    }
}
