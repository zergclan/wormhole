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

package com.zergclan.wormhole.tool.spi.scene.required;

import com.zergclan.wormhole.tool.spi.FailureHandler;
import com.zergclan.wormhole.tool.spi.WormholeServiceLoader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * Required SPI registry.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequiredSPIRegistry {
    
    /**
     * Get required registered service.
     *
     * @param spiInterface required SPI interface
     * @param <T> SPI class type
     * @return registered service
     */
    public static <T extends RequiredSPI> T getRegisteredService(final Class<T> spiInterface) {
        Collection<T> services = WormholeServiceLoader.getServiceInstances(spiInterface);
        if (services.isEmpty()) {
            FailureHandler.noServiceProvider(spiInterface);
        }
        if (1 == services.size()) {
            return services.iterator().next();
        }
        for (T each : services) {
            if (each.isDefault()) {
                return each;
            }
        }
        return services.iterator().next();
    }
}
