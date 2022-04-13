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

package com.zergclan.wormhole.config.core.swapper;

import com.zergclan.wormhole.binder.api.Swapper;
import com.zergclan.wormhole.config.core.DataNodeMappingConfiguration;
import com.zergclan.wormhole.config.core.SourceConfiguration;
import com.zergclan.wormhole.config.core.TargetConfiguration;
import com.zergclan.wormhole.config.core.TaskConfiguration;
import com.zergclan.wormhole.config.core.yaml.YamlTaskConfiguration;

import java.util.Collection;
import java.util.LinkedList;

/**
 * YAML task configuration swapper.
 */
public final class YamlTaskConfigurationSwapper implements Swapper<YamlTaskConfiguration, TaskConfiguration> {
    
    private final YamlSourceConfigurationSwapper sourceSwapper = new YamlSourceConfigurationSwapper();
    
    private final YamlTargetConfigurationSwapper targetSwapper = new YamlTargetConfigurationSwapper();
    
    private final YamlDataNodeMappingConfigurationSwapper dataNodeMappingSwapper = new YamlDataNodeMappingConfigurationSwapper();
    
    @Override
    public TaskConfiguration swapToTarget(final YamlTaskConfiguration yamlConfiguration) {
        SourceConfiguration sourceConfiguration = sourceSwapper.swapToTarget(yamlConfiguration.getSource());
        TargetConfiguration targetConfiguration = targetSwapper.swapToTarget(yamlConfiguration.getTarget());
        Collection<DataNodeMappingConfiguration> dataNodeMappings = new LinkedList<>();
        yamlConfiguration.getDataNodeMappings().forEach(each -> dataNodeMappings.add(dataNodeMappingSwapper.swapToTarget(each)));
        return new TaskConfiguration(yamlConfiguration.getOrder(), yamlConfiguration.getBatchSize(), sourceConfiguration, targetConfiguration, dataNodeMappings);
    }
    
    @Override
    public YamlTaskConfiguration swapToSource(final TaskConfiguration configuration) {
        // TODO init yamlTaskConfiguration
        return null;
    }
}
