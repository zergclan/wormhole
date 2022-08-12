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

package com.zergclan.wormhole.repository;

import com.zergclan.wormhole.common.spi.WormholeServiceLoader;
import com.zergclan.wormhole.common.spi.scene.typed.TypedSPIRegistry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Simple factory for create {@link PersistRepository} based on wormhole SPI.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PersistRepositoryFactory {

    static {
        WormholeServiceLoader.register(PersistRepository.class);
    }
    
    /**
     * Get instance.
     *
     * @param type type
     * @return {@link PersistRepository}
     */
    public static PersistRepository getInstance(final String type) {
        return TypedSPIRegistry.getRegisteredService(PersistRepository.class, type);
    }
}
