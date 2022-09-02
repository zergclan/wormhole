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

package com.zergclan.wormhole.common.metadata.initializer;

import com.zergclan.wormhole.common.configuration.DataSourceConfiguration;
import com.zergclan.wormhole.common.configuration.PlanConfiguration;
import com.zergclan.wormhole.common.configuration.WormholeConfiguration;
import com.zergclan.wormhole.common.metadata.WormholeMetaDataContext;
import com.zergclan.wormhole.common.metadata.datasource.WormholeDataSourceMetaData;
import com.zergclan.wormhole.common.metadata.plan.PlanMetaData;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * wormhole metadata context initializer.
 */
public final class WormholeMetaDataContextInitializer {
    
    private final PlanMetadataInitializer planMetadataInitializer = new PlanMetadataInitializer();
    
    private final DataSourceMetadataInitializer dataSourceMetadataInitializer = new DataSourceMetadataInitializer();
    
    /**
     * Init {@link WormholeMetaDataContext}.
     *
     * @param configuration {@link WormholeConfiguration}
     * @return {@link WormholeMetaDataContext}
     * @throws SQLException SQL Exception
     */
    public WormholeMetaDataContext init(final WormholeConfiguration configuration) throws SQLException {
        Map<String, WormholeDataSourceMetaData> dataSourceMetaData = createDataSources(configuration.getDataSources());
        Map<String, PlanMetaData> plans = createPlans(configuration.getPlans(), dataSourceMetaData);
        Map<String, WormholeDataSourceMetaData> dataSources = new LinkedHashMap<>();
        dataSourceMetaData.forEach((key, value) -> dataSources.put(value.getIdentifier(), value));
        return new WormholeMetaDataContext(dataSources, plans);
    }
    
    private Map<String, WormholeDataSourceMetaData> createDataSources(final Map<String, DataSourceConfiguration> configurations) {
        Map<String, WormholeDataSourceMetaData> result = new LinkedHashMap<>();
        configurations.forEach((key, value) -> result.put(key, dataSourceMetadataInitializer.createActualTypeDataSourceMetadata(value)));
        return result;
    }
    
    private Map<String, PlanMetaData> createPlans(final Map<String, PlanConfiguration> planConfigurations, final Map<String, WormholeDataSourceMetaData> dataSources) {
        Map<String, PlanMetaData> result = new LinkedHashMap<>();
        planConfigurations.forEach((key, value) -> result.put(key, planMetadataInitializer.init(key, value, dataSources)));
        return result;
    }
}
