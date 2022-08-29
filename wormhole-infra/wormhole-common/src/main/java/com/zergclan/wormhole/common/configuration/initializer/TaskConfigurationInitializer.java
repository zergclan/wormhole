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
import com.zergclan.wormhole.common.configuration.DataNodeMappingConfiguration;
import com.zergclan.wormhole.common.configuration.SourceConfiguration;
import com.zergclan.wormhole.common.configuration.TargetConfiguration;
import com.zergclan.wormhole.common.configuration.TaskConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlTaskConfiguration;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Initializer of {@link TaskConfiguration}.
 */
public final class TaskConfigurationInitializer implements WormholeInitializer<YamlTaskConfiguration, TaskConfiguration> {
    
    private final SourceConfigurationInitializer sourceConfigurationInitializer = new SourceConfigurationInitializer();
    
    private final TargetConfigurationInitializer targetConfigurationInitializer = new TargetConfigurationInitializer();
    
    private final DataNodeMappingConfigurationInitializer dataNodeMappingConfigurationInitializer = new DataNodeMappingConfigurationInitializer();
    
    @Override
    public TaskConfiguration init(final YamlTaskConfiguration yamlConfiguration) {
        SourceConfiguration sourceConfiguration = sourceConfigurationInitializer.init(yamlConfiguration.getSource());
        TargetConfiguration targetConfiguration = targetConfigurationInitializer.init(yamlConfiguration.getTarget());
        Collection<DataNodeMappingConfiguration> dataNodeMappings = new LinkedList<>();
        yamlConfiguration.getDataNodeMappings().forEach(each -> dataNodeMappings.add(dataNodeMappingConfigurationInitializer.init(each)));
        return new TaskConfiguration(yamlConfiguration.getOrder(), yamlConfiguration.getBatchSize(), sourceConfiguration, targetConfiguration, dataNodeMappings);
    }
}
