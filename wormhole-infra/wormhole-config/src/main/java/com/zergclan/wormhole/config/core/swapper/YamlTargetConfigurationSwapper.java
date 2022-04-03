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
import com.zergclan.wormhole.config.core.DataNodeConfiguration;
import com.zergclan.wormhole.config.core.TargetConfiguration;
import com.zergclan.wormhole.config.core.yaml.YamlTargetConfiguration;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * YAML target configuration swapper.
 */
public final class YamlTargetConfigurationSwapper implements Swapper<YamlTargetConfiguration, TargetConfiguration> {
    
    @Override
    public TargetConfiguration swapToTarget(final YamlTargetConfiguration yamlConfiguration) {
        String dataSource = yamlConfiguration.getDataSource();
        String table = yamlConfiguration.getTable();
        boolean transaction = yamlConfiguration.isTransaction();
        Collection<String> uniqueNodes = parseNodeNames(yamlConfiguration.getUniqueNodes());
        Collection<String> compareNodes = parseNodeNames(yamlConfiguration.getCompareNodes());
        Collection<String> ignoreNodes = parseNodeNames(yamlConfiguration.getIgnoreNodes());
        Map<String, DataNodeConfiguration> dataNodes = new LinkedHashMap<>();
        yamlConfiguration.getDataNodes().forEach((key, value) -> dataNodes.put(key, new DataNodeConfiguration(key, value.getNodeType(), value.getDataType(), value.getDefaultValue())));
        return new TargetConfiguration(dataSource, table, transaction, uniqueNodes, compareNodes, ignoreNodes, dataNodes);
    }
    
    private Collection<String> parseNodeNames(final String nodeNames) {
        return StringUtil.deduplicateSplit(nodeNames, MarkConstant.COMMA);
    }
    
    @Override
    public YamlTargetConfiguration swapToSource(final TargetConfiguration configuration) {
        // TODO init yamlTargetConfiguration
        return null;
    }
}
