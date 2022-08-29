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

package com.zergclan.wormhole.common.metadata.loader;

import com.zergclan.wormhole.common.metadata.datasource.DataSourceMetaData;
import com.zergclan.wormhole.tool.spi.WormholeServiceLoader;
import com.zergclan.wormhole.tool.spi.scene.typed.TypedSPIRegistry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builder of {@link MetaDataLoader}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MetaDataLoaderBuilder {
    
    private static final Map<String, MetaDataLoader> CONTAINER = new LinkedHashMap<>();
    
    static {
        WormholeServiceLoader.register(MetaDataLoader.class);
    }
    
    /**
     * Build {@link MetaDataLoader}.
     *
     * @param metadata {@link DataSourceMetaData}
     * @return {@link MetaDataLoader}
     * @throws SQLException {@link SQLException}
     */
    public static synchronized MetaDataLoader build(final DataSourceMetaData metadata) throws SQLException {
        String identifier = metadata.getIdentifier();
        MetaDataLoader metaDataLoader = CONTAINER.get(identifier);
        if (null == metaDataLoader) {
            metaDataLoader = createMetaDataLoader(metadata);
            CONTAINER.put(identifier, metaDataLoader);
        }
        return metaDataLoader;
    }
    
    private static MetaDataLoader createMetaDataLoader(final DataSourceMetaData metadata) throws SQLException {
        MetaDataLoader result = TypedSPIRegistry.getRegisteredService(MetaDataLoader.class, metadata.getDataSourceType().getType());
        result.init(metadata);
        return result;
    }
}
