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
import com.zergclan.wormhole.common.configuration.SourceConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlSourceConfiguration;
import com.zergclan.wormhole.tool.util.Validator;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Initializer of {@link SourceConfiguration}.
 */
public final class SourceConfigurationInitializer implements WormholeInitializer<YamlSourceConfiguration, SourceConfiguration> {
    
    @Override
    public SourceConfiguration init(final YamlSourceConfiguration yamlConfiguration) {
        String dataSource = yamlConfiguration.getDataSource();
        String table = yamlConfiguration.getTable();
        Validator.notBlank(dataSource, "error: source configuration initialization failed arg data source can not be blank");
        Validator.notBlank(table, "error: source configuration initialization failed arg table can not be blank");
        String actualSql = yamlConfiguration.getActualSql();
        String conditionSql = yamlConfiguration.getConditionSql();
        Map<String, DataNodeConfiguration> dataNodes = new LinkedHashMap<>();
        yamlConfiguration.getDataNodes().forEach((key, value) -> dataNodes.put(key, new DataNodeConfiguration(key, value.getNodeType(), value.getDataType(), value.getDefaultValue())));
        return new SourceConfiguration(dataSource, actualSql, table, conditionSql, dataNodes);
    }
}
