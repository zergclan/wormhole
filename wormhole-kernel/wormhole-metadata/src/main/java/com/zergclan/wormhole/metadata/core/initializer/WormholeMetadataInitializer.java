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

package com.zergclan.wormhole.metadata.core.initializer;

import com.zergclan.wormhole.config.core.DataSourceConfiguration;
import com.zergclan.wormhole.config.core.PlanConfiguration;
import com.zergclan.wormhole.config.core.WormholeConfiguration;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.WormholeMetaData;
import com.zergclan.wormhole.metadata.core.plan.PlanMetaData;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * wormhole metadata initializer.
 */
public final class WormholeMetadataInitializer {
    
    private final PlanMetadataInitializer planMetadataInitializer = new PlanMetadataInitializer();
    
    /**
     * Init {@link WormholeMetaData}.
     *
     * @param configuration {@link WormholeConfiguration}
     * @return {@link WormholeMetaData}
     * @throws SQLException SQL Exception
     */
    public WormholeMetaData init(final WormholeConfiguration configuration) throws SQLException {
        Map<String, DataSourceMetaData> dataSourceMetaData = createDataSources(configuration.getDataSources());
        Map<String, PlanMetaData> plans = createPlans(configuration.getPlans(), dataSourceMetaData);
        Map<String, DataSourceMetaData> dataSources = new LinkedHashMap<>();
        dataSourceMetaData.forEach((key, value) -> dataSources.put(value.getIdentifier(), value));
        return new WormholeMetaData(dataSources, plans);
    }
    
    private Map<String, DataSourceMetaData> createDataSources(final Map<String, DataSourceConfiguration> configurations) throws SQLException {
        Map<String, DataSourceMetaData> result = new LinkedHashMap<>();
        Iterator<Map.Entry<String, DataSourceConfiguration>> iterator = configurations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, DataSourceConfiguration> entry = iterator.next();
            result.put(entry.getKey(), DataSourceMetadataInitializer.createActualTypeDataSourceMetadata(entry.getValue()));
        }
        return result;
    }
    
    private Map<String, PlanMetaData> createPlans(final Map<String, PlanConfiguration> planConfigurations, final Map<String, DataSourceMetaData> dataSources) {
        Iterator<Map.Entry<String, PlanConfiguration>> iterator = planConfigurations.entrySet().iterator();
        Map<String, PlanMetaData> result = new LinkedHashMap<>();
        while (iterator.hasNext()) {
            Map.Entry<String, PlanConfiguration> entry = iterator.next();
            result.put(entry.getKey(), planMetadataInitializer.init(entry.getKey(), entry.getValue(), dataSources));
        }
        return result;
    }
}
