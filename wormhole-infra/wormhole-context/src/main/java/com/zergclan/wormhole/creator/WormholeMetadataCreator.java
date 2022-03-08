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

package com.zergclan.wormhole.creator;

import com.zergclan.wormhole.core.config.DataSourceConfiguration;
import com.zergclan.wormhole.core.config.PlanConfiguration;
import com.zergclan.wormhole.core.config.WormholeConfiguration;
import com.zergclan.wormhole.core.api.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.WormholeMetadata;
import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Metadata creator of {@link WormholeMetadata}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WormholeMetadataCreator {
    
    /**
     * Create {@link WormholeMetadata}.
     *
     * @param configuration {@link WormholeConfiguration}
     * @return {@link WormholeMetadata}
     * @throws SQLException exception
     */
    public static WormholeMetadata create(final WormholeConfiguration configuration) throws SQLException {
        Map<String, DataSourceMetadata> dataSources = createDataSources(configuration.getDataSourceConfigurations());
        Map<String, PlanMetadata> plans = createPlans(configuration.getPlanConfigurations(), dataSources);
        return new WormholeMetadata(dataSources, plans);
    }

    private static Map<String, DataSourceMetadata> createDataSources(final Map<String, DataSourceConfiguration> configurations) throws SQLException {
        Map<String, DataSourceMetadata> result = new LinkedHashMap<>();
        Iterator<Map.Entry<String, DataSourceConfiguration>> iterator = configurations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, DataSourceConfiguration> next = iterator.next();
            result.put(next.getKey(), DatasourceMetadataCreator.create(next.getValue()));
        }
        return result;
    }

    private static Map<String, PlanMetadata> createPlans(final Map<String, PlanConfiguration> planConfigurations, final Map<String, DataSourceMetadata> dataSources) {
        Iterator<Map.Entry<String, PlanConfiguration>> iterator = planConfigurations.entrySet().iterator();
        Map<String, PlanMetadata> result = new LinkedHashMap<>();
        while (iterator.hasNext()) {
            Map.Entry<String, PlanConfiguration> entry = iterator.next();
            result.put(entry.getKey(), PlanMetadataCreator.create(entry.getValue(), dataSources));
        }
        return result;
    }
}
