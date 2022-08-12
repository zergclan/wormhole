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

package com.zergclan.wormhole.common.swapper;

import com.zergclan.wormhole.common.configuration.DataSourceConfiguration;
import com.zergclan.wormhole.common.configuration.DataSourcePoolConfiguration;
import com.zergclan.wormhole.common.yaml.YamlDataSourceConfiguration;
import com.zergclan.wormhole.common.yaml.YamlDataSourcePoolConfiguration;

/**
 * YAML data source configuration swapper.
 */
public final class YamlDataSourceConfigurationSwapper implements Swapper<YamlDataSourceConfiguration, DataSourceConfiguration> {
    
    private final YamlDataSourcePoolConfigurationSwapper dataSourcePoolSwapper = new YamlDataSourcePoolConfigurationSwapper();
    
    @Override
    public DataSourceConfiguration swapToTarget(final YamlDataSourceConfiguration yamlConfiguration) {
        String dataSourceName = yamlConfiguration.getName();
        String dataSourceType = yamlConfiguration.getType();
        String url = yamlConfiguration.getUrl();
        String username = yamlConfiguration.getUsername();
        String password = yamlConfiguration.getPassword();
        YamlDataSourcePoolConfiguration yamlDataSourcePool = yamlConfiguration.getPool();
        String poolName = null == yamlDataSourcePool.getPoolName() ? "wormhole-ds-" + dataSourceName : yamlDataSourcePool.getPoolName();
        yamlDataSourcePool.setPoolName(poolName);
        DataSourcePoolConfiguration dataSourcePoolConfiguration = dataSourcePoolSwapper.swapToTarget(yamlDataSourcePool);
        return new DataSourceConfiguration(dataSourceName, dataSourceType, url, username, password, dataSourcePoolConfiguration);
    }
    
    @Override
    public YamlDataSourceConfiguration swapToSource(final DataSourceConfiguration configuration) {
        // TODO init YamlDataSourceConfiguration
        return null;
    }
}
