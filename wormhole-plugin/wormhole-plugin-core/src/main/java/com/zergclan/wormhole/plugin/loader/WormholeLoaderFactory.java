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

package com.zergclan.wormhole.plugin.loader;

import com.zergclan.wormhole.common.metadata.catched.CachedTargetMetaData;
import com.zergclan.wormhole.tool.spi.WormholeServiceLoader;
import com.zergclan.wormhole.tool.spi.scene.typed.TypedSPIRegistry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * Loader factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WormholeLoaderFactory {
    
    static {
        WormholeServiceLoader.register(WormholeLoader.class);
    }
    
    /**
     * Get loader.
     *
     * @param cachedTarget {@link CachedTargetMetaData}
     * @return {@link WormholeLoader}
     */
    @SuppressWarnings("all")
    public static Optional<WormholeLoader> getLoader(final CachedTargetMetaData cachedTarget) {
        Optional<WormholeLoader> registeredService = TypedSPIRegistry.findRegisteredService(WormholeLoader.class, cachedTarget.getDataSource().getDataSourceType().getType());
        if (registeredService.isPresent()) {
            WormholeLoader loader = registeredService.get();
            loader.init(cachedTarget);
            return Optional.of(loader);
        }
        return Optional.empty();
    }
}
