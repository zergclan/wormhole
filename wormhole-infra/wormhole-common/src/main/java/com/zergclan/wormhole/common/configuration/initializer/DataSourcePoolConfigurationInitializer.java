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
import com.zergclan.wormhole.common.configuration.DataSourcePoolConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlDataSourcePoolConfiguration;
import com.zergclan.wormhole.tool.util.ValueExtractor;

/**
 * Initializer of {@link DataSourcePoolConfiguration}.
 */
public final class DataSourcePoolConfigurationInitializer implements WormholeInitializer<YamlDataSourcePoolConfiguration, DataSourcePoolConfiguration> {
    
    @Override
    public DataSourcePoolConfiguration init(final YamlDataSourcePoolConfiguration yamlConfiguration) {
        int minPoolSize = ValueExtractor.extractValueOrDefault(yamlConfiguration.getMinPoolSize(), 1);
        int maxPoolSize = ValueExtractor.extractValueOrDefault(yamlConfiguration.getMaxPoolSize(), 2);
        int connectionTimeout = ValueExtractor.extractValueOrDefault(yamlConfiguration.getConnectionTimeoutMilliseconds(), 30000);
        int idleTimeout = ValueExtractor.extractValueOrDefault(yamlConfiguration.getIdleTimeoutMilliseconds(), 60000);
        int maxLifetime = ValueExtractor.extractValueOrDefault(yamlConfiguration.getMaxLifetimeMilliseconds(), 1800000);
        return new DataSourcePoolConfiguration(yamlConfiguration.getPoolName(), minPoolSize, maxPoolSize, connectionTimeout, idleTimeout, maxLifetime);
    }
}
