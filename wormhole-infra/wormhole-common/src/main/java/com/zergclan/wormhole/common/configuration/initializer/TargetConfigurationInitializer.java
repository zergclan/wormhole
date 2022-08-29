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
import com.zergclan.wormhole.common.configuration.DataNodeConfiguration;
import com.zergclan.wormhole.common.configuration.TargetConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlTargetConfiguration;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.tool.util.StringUtil;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Initializer of {@link TargetConfiguration}.
 */
public final class TargetConfigurationInitializer implements WormholeInitializer<YamlTargetConfiguration, TargetConfiguration> {
    
    @Override
    public TargetConfiguration init(final YamlTargetConfiguration yamlConfiguration) {
        String dataSource = yamlConfiguration.getDataSource();
        String table = yamlConfiguration.getTable();
        Collection<String> uniqueNodes = StringUtil.deduplicateSplit(yamlConfiguration.getUniqueNodes(), MarkConstant.COMMA);
        Collection<String> compareNodes = StringUtil.deduplicateSplit(yamlConfiguration.getCompareNodes(), MarkConstant.COMMA);
        Collection<String> ignoreNodes = StringUtil.deduplicateSplit(yamlConfiguration.getIgnoreNodes(), MarkConstant.COMMA);
        String versionNode = yamlConfiguration.getVersionNode();
        Map<String, DataNodeConfiguration> dataNodes = new LinkedHashMap<>();
        yamlConfiguration.getDataNodes().forEach((key, value) -> dataNodes.put(key, new DataNodeConfiguration(key, value.getNodeType(), value.getDataType(), value.getDefaultValue())));
        return new TargetConfiguration(dataSource, table, uniqueNodes, compareNodes, ignoreNodes, versionNode, dataNodes);
    }
}
