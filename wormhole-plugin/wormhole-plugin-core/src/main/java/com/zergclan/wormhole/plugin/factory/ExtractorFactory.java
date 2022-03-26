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

package com.zergclan.wormhole.plugin.factory;

import com.zergclan.wormhole.common.spi.WormholeServiceLoader;
import com.zergclan.wormhole.common.spi.typed.TypedSPIRegistry;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.plugin.api.Extractor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * Extractor factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExtractorFactory {
    
    static {
        WormholeServiceLoader.register(Extractor.class);
    }
    
    /**
     * Get extractor.
     *
     * @param dataSource {@link DataSourceMetaData}
     * @return {@link Extractor}
     *
     */
    public static Optional<Extractor> getExtractor(final DataSourceMetaData dataSource) {
        return TypedSPIRegistry.findRegisteredService(Extractor.class, dataSource.getDataSourceType());
    }
}
