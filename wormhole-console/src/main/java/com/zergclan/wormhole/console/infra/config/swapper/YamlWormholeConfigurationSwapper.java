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

package com.zergclan.wormhole.console.infra.config.swapper;

import com.zergclan.wormhole.config.api.Swapper;
import com.zergclan.wormhole.config.core.DataSourceConfiguration;
import com.zergclan.wormhole.config.core.PlanConfiguration;
import com.zergclan.wormhole.config.core.WormholeConfiguration;
import com.zergclan.wormhole.console.infra.config.yaml.YamlDataSourceConfiguration;
import com.zergclan.wormhole.console.infra.config.yaml.YamlPlanConfiguration;
import com.zergclan.wormhole.console.infra.config.yaml.YamlTaskConfiguration;
import com.zergclan.wormhole.console.infra.config.yaml.YamlWormholeConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * YAML wormhole configuration swapper.
 */
public final class YamlWormholeConfigurationSwapper implements Swapper<YamlWormholeConfiguration, WormholeConfiguration> {
    
    private final YamlDataSourceConfigurationSwapper dataSourceSwapper = new YamlDataSourceConfigurationSwapper();
    
    private final YamlPlanConfigurationSwapper planSwapper = new YamlPlanConfigurationSwapper();
    
    @Override
    public WormholeConfiguration swapToTarget(final YamlWormholeConfiguration yamlConfiguration) {
        Map<String, DataSourceConfiguration> dataSourceConfigurations = createDataSourceConfigurations(yamlConfiguration.getDataSources());
        Map<String, PlanConfiguration> planConfigurations = createPlanConfigurations(yamlConfiguration.getPlans(), yamlConfiguration.getTasks());
        return new WormholeConfiguration(dataSourceConfigurations, planConfigurations);
    }
    
    private Map<String, DataSourceConfiguration> createDataSourceConfigurations(final Map<String, YamlDataSourceConfiguration> dataSourceConfigurations) {
        Map<String, DataSourceConfiguration> result = new LinkedHashMap<>();
        for (Map.Entry<String, YamlDataSourceConfiguration> entry : dataSourceConfigurations.entrySet()) {
            String dataSource = entry.getKey();
            YamlDataSourceConfiguration yamlDataSourceConfiguration = entry.getValue();
            yamlDataSourceConfiguration.setDataSourceName(dataSource);
            result.put(dataSource, dataSourceSwapper.swapToTarget(yamlDataSourceConfiguration));
        }
        return result;
    }
    
    private Map<String, PlanConfiguration> createPlanConfigurations(final Map<String, YamlPlanConfiguration> planConfigurations, final Map<String, YamlTaskConfiguration> taskConfigurations) {
        Map<String, PlanConfiguration> result = new LinkedHashMap<>();
        PlanConfiguration planConfiguration;
        for (Map.Entry<String, YamlPlanConfiguration> entry : planConfigurations.entrySet()) {
            planConfiguration = planSwapper.swapToTarget(entry.getValue());
            initTasks(planConfiguration, taskConfigurations);
            result.put(entry.getKey(), planSwapper.swapToTarget(entry.getValue()));
        }
        return result;
    }
    
    private void initTasks(final PlanConfiguration planConfiguration, final Map<String, YamlTaskConfiguration> taskConfigurations) {
        //TODO init tasks
    }
    
    @Override
    public YamlWormholeConfiguration swapToSource(final WormholeConfiguration target) {
        // TODO
        return null;
    }
}
