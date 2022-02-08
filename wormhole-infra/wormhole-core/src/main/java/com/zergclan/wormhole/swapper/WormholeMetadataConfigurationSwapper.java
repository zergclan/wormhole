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

package com.zergclan.wormhole.swapper;

import com.zergclan.wormhole.api.Pipeline;
import com.zergclan.wormhole.core.config.DataSourceConfiguration;
import com.zergclan.wormhole.core.config.PipelineConfiguration;
import com.zergclan.wormhole.core.config.PlanConfiguration;
import com.zergclan.wormhole.core.config.WormholeConfiguration;
import com.zergclan.wormhole.core.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.WormholeMetadata;
import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Wormhole metadata configuration swapper.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WormholeMetadataConfigurationSwapper {

    /**
     * Swap to {@link WormholeMetadata}.
     *
     * @param wormholeConfiguration {@link WormholeConfiguration}
     * @return {@link WormholeMetadata}
     */
    public static WormholeMetadata swapToMetadata(final WormholeConfiguration wormholeConfiguration) {
        Map<String, DataSourceMetadata> dataSources = createDataSourceMetadata(wormholeConfiguration.getDataSourcesConfigurations());
        Map<String, PlanMetadata> plans = createPlanMetadata(wormholeConfiguration.getPlanConfigurations());
        return new WormholeMetadata(dataSources, plans);
    }

    private static Map<String, DataSourceMetadata> createDataSourceMetadata(final Collection<DataSourceConfiguration> dataSourcesConfigurations) {
        Map<String, DataSourceMetadata> result = new LinkedHashMap<>();
        for (DataSourceConfiguration each : dataSourcesConfigurations) {
            DataSourceMetadata dataSourceMetadata = DatasourceMetadataConfigurationSwapper.swapToMetadata(each);
            result.put(dataSourceMetadata.getIdentifier(), dataSourceMetadata);
        }
        return result;
    }

    private static Map<String, PlanMetadata> createPlanMetadata(final Collection<PlanConfiguration> planConfigurations) {
        Map<String, PlanMetadata> result = new LinkedHashMap<>();
        for (PlanConfiguration each : planConfigurations) {
            PlanMetadata planMetadata = PlanMetadataConfigurationSwapper.swapToMetadata(each);
            result.put(planMetadata.getIdentifier(), planMetadata);
        }
        return result;
    }

    private static Map<String, Pipeline<?>> createDataNodePipeline(final Collection<PipelineConfiguration> pipelineConfigurations) {
        Map<String, Pipeline<?>> result = new LinkedHashMap<>();
        for (PipelineConfiguration each : pipelineConfigurations) {
            PipelineMetadataConfigurationSwapper.swapToMetadata(each);
        }
        return result;
    }
}
