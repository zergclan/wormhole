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

package com.zergclan.wormhole.binder.spi.typed;

import com.zergclan.wormhole.binder.spi.WormholeServiceLoader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TypedSPIRegistry {
    
    /**
     * Find registered service.
     *
     * @param typedSPIClass typed SPI class
     * @param type type
     * @param <T> type
     * @return registered service
     */
    public static <T extends TypedSPI> Optional<T> findRegisteredService(final Class<T> typedSPIClass, final String type) {
        for (T each : WormholeServiceLoader.newServiceInstances(typedSPIClass)) {
            if (each.getType().equalsIgnoreCase(type)) {
                return Optional.of(each);
            }
        }
        return Optional.empty();
    }
}
