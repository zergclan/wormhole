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

package com.zergclan.wormhole.common.configuration.initializer;

import com.zergclan.wormhole.common.WormholeInitializer;
import com.zergclan.wormhole.common.configuration.AuthorizationConfiguration;
import com.zergclan.wormhole.common.configuration.DataSourceConfiguration;
import com.zergclan.wormhole.common.configuration.PlanConfiguration;
import com.zergclan.wormhole.common.configuration.WormholeConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlDataSourceConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlPlanConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlTaskConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlWormholeConfiguration;
import com.zergclan.wormhole.tool.util.Validator;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Initializer of {@link WormholeConfiguration}.
 */
public final class WormholeConfigurationInitializer implements WormholeInitializer<YamlWormholeConfiguration, WormholeConfiguration> {
    
    private final AuthorizationConfigurationInitializer authorizationConfigurationInitializer = new AuthorizationConfigurationInitializer();
    
    private final DataSourceConfigurationInitializer dataSourceConfigurationInitializer = new DataSourceConfigurationInitializer();
    
    private final PlanConfigurationInitializer planConfigurationInitializer = new PlanConfigurationInitializer();
    
    private final TaskConfigurationInitializer taskConfigurationInitializer = new TaskConfigurationInitializer();
    
    @Override
    public WormholeConfiguration init(final YamlWormholeConfiguration yamlConfiguration) {
        AuthorizationConfiguration authorization = authorizationConfigurationInitializer.init(yamlConfiguration.getAuthorization());
        Map<String, DataSourceConfiguration> dataSourceConfigurations = createDataSourceConfigurations(yamlConfiguration.getDataSources());
        Map<String, PlanConfiguration> planConfigurations = createPlanConfigurations(yamlConfiguration.getPlans(), yamlConfiguration.getTasks());
        return new WormholeConfiguration(authorization, dataSourceConfigurations, planConfigurations);
    }
    
    private Map<String, DataSourceConfiguration> createDataSourceConfigurations(final Map<String, YamlDataSourceConfiguration> dataSourceConfigurations) {
        Map<String, DataSourceConfiguration> result = new LinkedHashMap<>();
        for (Map.Entry<String, YamlDataSourceConfiguration> entry : dataSourceConfigurations.entrySet()) {
            String dataSourceName = entry.getKey();
            YamlDataSourceConfiguration yamlDataSourceConfiguration = entry.getValue();
            yamlDataSourceConfiguration.setName(dataSourceName);
            result.put(dataSourceName, dataSourceConfigurationInitializer.init(yamlDataSourceConfiguration));
        }
        return result;
    }
    
    private Map<String, PlanConfiguration> createPlanConfigurations(final Map<String, YamlPlanConfiguration> planConfigurations, final Map<String, YamlTaskConfiguration> taskConfigurations) {
        Map<String, PlanConfiguration> result = new LinkedHashMap<>();
        PlanConfiguration planConfiguration;
        for (Map.Entry<String, YamlPlanConfiguration> entry : planConfigurations.entrySet()) {
            String planName = entry.getKey();
            YamlPlanConfiguration yamlPlanConfiguration = entry.getValue();
            planConfiguration = planConfigurationInitializer.init(yamlPlanConfiguration);
            Map<String, YamlTaskConfiguration> relatedTaskConfigurations = getRelatedTaskConfigurations(taskConfigurations, yamlPlanConfiguration, planName);
            initTasks(planConfiguration, relatedTaskConfigurations);
            result.put(planName, planConfiguration);
        }
        return result;
    }
    
    private Map<String, YamlTaskConfiguration> getRelatedTaskConfigurations(final Map<String, YamlTaskConfiguration> tasks, final YamlPlanConfiguration yamlPlan, final String planName) {
        Collection<String> relatedTaskNames = yamlPlan.getTasks();
        Map<String, YamlTaskConfiguration> result = new LinkedHashMap<>();
        relatedTaskNames.forEach(each -> {
            YamlTaskConfiguration taskConfiguration = tasks.get(each);
            Validator.notNull(taskConfiguration, "error: related task [%s] can not find in plan [%s]", each, planName);
            result.put(each, taskConfiguration);
        });
        return result;
    }
    
    private void initTasks(final PlanConfiguration planConfiguration, final Map<String, YamlTaskConfiguration> relatedTaskConfigurations) {
        relatedTaskConfigurations.forEach((key, each) -> planConfiguration.registerTask(key, taskConfigurationInitializer.init(each)));
    }
}
