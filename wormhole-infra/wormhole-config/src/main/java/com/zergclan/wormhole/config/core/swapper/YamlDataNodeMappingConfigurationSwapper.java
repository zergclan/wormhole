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

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.common.util.StringUtil;
import com.zergclan.wormhole.config.api.Swapper;
import com.zergclan.wormhole.config.core.DataNodeMappingConfiguration;
import com.zergclan.wormhole.config.core.FilterConfiguration;
import com.zergclan.wormhole.config.core.yaml.YamlDataNodeMappingConfiguration;

import java.util.Collection;
import java.util.LinkedList;

/**
 * YAML data node mapping configuration swapper.
 */
public final class YamlDataNodeMappingConfigurationSwapper implements Swapper<YamlDataNodeMappingConfiguration, DataNodeMappingConfiguration> {
    
    @Override
    public DataNodeMappingConfiguration swapToTarget(final YamlDataNodeMappingConfiguration yamlConfiguration) {
        Collection<String> targetNames = parseNodeNames(yamlConfiguration.getTargetNames());
        Collection<String> sourceNames = parseNodeNames(yamlConfiguration.getSourceNames());
        Collection<FilterConfiguration> filters = new LinkedList<>();
        yamlConfiguration.getFilters().forEach(each -> filters.add(new FilterConfiguration(each.getType(), each.getOrder(), each.getProp())));
        return new DataNodeMappingConfiguration(targetNames, sourceNames, filters);
    }
    
    private Collection<String> parseNodeNames(final String nodeNames) {
        return StringUtil.deduplicateSplit(nodeNames, MarkConstant.COMMA);
    }
    
    @Override
    public YamlDataNodeMappingConfiguration swapToSource(final DataNodeMappingConfiguration configuration) {
        // TODO init yamlDataNodeMappingConfiguration
        return null;
    }
}
