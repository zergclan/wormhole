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
import com.zergclan.wormhole.common.configuration.DataSourceConfiguration;
import com.zergclan.wormhole.common.configuration.DataSourcePoolConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlDataSourceConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlDataSourcePoolConfiguration;

/**
 * Initializer of {@link DataSourceConfiguration}.
 */
public final class DataSourceConfigurationInitializer implements WormholeInitializer<YamlDataSourceConfiguration, DataSourceConfiguration> {
    
    private final DataSourcePoolConfigurationInitializer poolConfigurationInitializer = new DataSourcePoolConfigurationInitializer();
    
    @Override
    public DataSourceConfiguration init(final YamlDataSourceConfiguration yamlConfiguration) {
        String dataSourceName = yamlConfiguration.getName();
        String dataSourceType = yamlConfiguration.getType();
        String url = yamlConfiguration.getUrl();
        String username = yamlConfiguration.getUsername();
        String password = yamlConfiguration.getPassword();
        YamlDataSourcePoolConfiguration yamlDataSourcePool = yamlConfiguration.getPool();
        String poolName = null == yamlDataSourcePool.getPoolName() ? "wormhole-ds-" + dataSourceName : yamlDataSourcePool.getPoolName();
        yamlDataSourcePool.setPoolName(poolName);
        DataSourcePoolConfiguration dataSourcePoolConfiguration = poolConfigurationInitializer.init(yamlDataSourcePool);
        return new DataSourceConfiguration(dataSourceName, dataSourceType, url, username, password, dataSourcePoolConfiguration);
    }
}
