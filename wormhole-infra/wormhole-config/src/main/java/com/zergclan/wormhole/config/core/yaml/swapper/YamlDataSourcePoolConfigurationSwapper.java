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

package com.zergclan.wormhole.config.core.yaml.swapper;

import com.zergclan.wormhole.common.util.ValueExtractor;
import com.zergclan.wormhole.config.api.Swapper;
import com.zergclan.wormhole.config.core.DataSourcePoolConfiguration;
import com.zergclan.wormhole.config.core.yaml.YamlDataSourcePoolConfiguration;

/**
 * YAML data source pool configuration swapper.
 */
public final class YamlDataSourcePoolConfigurationSwapper implements Swapper<YamlDataSourcePoolConfiguration, DataSourcePoolConfiguration> {
    
    @Override
    public DataSourcePoolConfiguration swapToTarget(final YamlDataSourcePoolConfiguration yamlConfiguration) {
        int minPoolSize = ValueExtractor.getOrDefault(yamlConfiguration.getMinPoolSize(), 1);
        int maxPoolSize = ValueExtractor.getOrDefault(yamlConfiguration.getMaxPoolSize(), 2);
        int connectionTimeout = ValueExtractor.getOrDefault(yamlConfiguration.getConnectionTimeoutMilliseconds(), 30000);
        int idleTimeout = ValueExtractor.getOrDefault(yamlConfiguration.getIdleTimeoutMilliseconds(), 60000);
        int maxLifetime = ValueExtractor.getOrDefault(yamlConfiguration.getMaxLifetimeMilliseconds(), 1800000);
        return new DataSourcePoolConfiguration(yamlConfiguration.getPoolName(), minPoolSize, maxPoolSize, connectionTimeout, idleTimeout, maxLifetime);
    }
    
    @Override
    public YamlDataSourcePoolConfiguration swapToSource(final DataSourcePoolConfiguration configuration) {
        // TODO init YamlDataSourcePoolConfiguration
        return null;
    }
}
