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
import com.zergclan.wormhole.common.configuration.FilterConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlDataNodeMappingConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlFilterConfiguration;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.tool.util.StringUtil;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Initializer of {@link DataNodeMappingConfiguration}.
 */
public final class DataNodeMappingConfigurationInitializer implements WormholeInitializer<YamlDataNodeMappingConfiguration, DataNodeMappingConfiguration> {
    
    @Override
    public DataNodeMappingConfiguration init(final YamlDataNodeMappingConfiguration yamlConfiguration) {
        Collection<String> targetNames = StringUtil.deduplicateSplit(yamlConfiguration.getTargetNames(), MarkConstant.COMMA);
        Collection<String> sourceNames = StringUtil.deduplicateSplit(yamlConfiguration.getSourceNames(), MarkConstant.COMMA);
        Collection<FilterConfiguration> filters = initFilters(yamlConfiguration.getFilters());
        return new DataNodeMappingConfiguration(targetNames, sourceNames, filters);
    }
    
    private Collection<FilterConfiguration> initFilters(final Collection<YamlFilterConfiguration> filterConfigurations) {
        Collection<FilterConfiguration> result = new LinkedList<>();
        int order = 0;
        for (YamlFilterConfiguration each : filterConfigurations) {
            result.add(new FilterConfiguration(each.getType(), order, each.getProp()));
            order++;
        }
        return result;
    }
}
