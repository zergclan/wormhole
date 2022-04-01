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
import com.zergclan.wormhole.config.core.DataNodeConfiguration;
import com.zergclan.wormhole.config.core.SourceConfiguration;
import com.zergclan.wormhole.console.infra.config.yaml.YamlSourceConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * YAML source configuration swapper.
 */
public final class YamlSourceConfigurationSwapper implements Swapper<YamlSourceConfiguration, SourceConfiguration> {
    
    @Override
    public SourceConfiguration swapToTarget(final YamlSourceConfiguration yamlConfiguration) {
        String dataSource = yamlConfiguration.getDataSource();
        String actualSql = yamlConfiguration.getActualSql();
        String table = yamlConfiguration.getTable();
        String conditionSql = yamlConfiguration.getConditionSql();
        Map<String, DataNodeConfiguration> dataNodes = new LinkedHashMap<>();
        yamlConfiguration.getDataNodes().forEach((key, value) -> dataNodes.put(key, new DataNodeConfiguration(key, value.getNodeType(), value.getDataType(), value.getDefaultValue())));
        return new SourceConfiguration(dataSource, actualSql, table, conditionSql, dataNodes);
    }
    
    @Override
    public YamlSourceConfiguration swapToSource(final SourceConfiguration configuration) {
        // TODO init yamlSourceConfiguration
        return null;
    }
}
