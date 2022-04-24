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

import com.zergclan.wormhole.config.api.Swapper;
import com.zergclan.wormhole.common.util.StringUtil;
import com.zergclan.wormhole.config.core.DataSourceConfiguration;
import com.zergclan.wormhole.config.core.yaml.YamlDataSourceConfiguration;

import java.util.Properties;

/**
 * YAML data source configuration swapper.
 */
public final class YamlDataSourceConfigurationSwapper implements Swapper<YamlDataSourceConfiguration, DataSourceConfiguration> {
    
    @Override
    public DataSourceConfiguration swapToTarget(final YamlDataSourceConfiguration yamlConfiguration) {
        String dataSourceName = yamlConfiguration.getDataSourceName();
        String type = yamlConfiguration.getType();
        String host = yamlConfiguration.getHost();
        int port = yamlConfiguration.getPort();
        String username = yamlConfiguration.getUsername();
        String password = yamlConfiguration.getPassword();
        String catalog = yamlConfiguration.getCatalog();
        Properties props = initProperties(yamlConfiguration);
        return new DataSourceConfiguration(dataSourceName, type, host, port, username, password, catalog, props);
    }
    
    private Properties initProperties(final YamlDataSourceConfiguration yamlConfiguration) {
        Properties result = new Properties();
        String poolName = yamlConfiguration.getPoolName();
        result.put("poolName", StringUtil.isBlank(poolName) ? yamlConfiguration.getDataSourceName() : poolName);
        result.put("minPoolSize", yamlConfiguration.getMinPoolSize());
        result.put("maxPoolSize", yamlConfiguration.getMaxPoolSize());
        result.put("connectionTimeoutMilliseconds", yamlConfiguration.getConnectionTimeoutMilliseconds());
        result.put("idleTimeoutMilliseconds", yamlConfiguration.getIdleTimeoutMilliseconds());
        result.put("maxLifetimeMilliseconds", yamlConfiguration.getMaxLifetimeMilliseconds());
        return result;
    }
    
    @Override
    public YamlDataSourceConfiguration swapToSource(final DataSourceConfiguration target) {
        return null;
    }
}
