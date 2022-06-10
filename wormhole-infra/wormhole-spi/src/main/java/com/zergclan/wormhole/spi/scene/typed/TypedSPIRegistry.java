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

package com.zergclan.wormhole.spi.scene.typed;

import com.zergclan.wormhole.spi.SPIPostProcessor;
import com.zergclan.wormhole.spi.WormholeServiceLoader;
import com.zergclan.wormhole.spi.FailureHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.Properties;

/**
 * Typed SPI registry.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TypedSPIRegistry {
    
    /**
     * Get typed registered service.
     *
     * @param spiInterface typed SPI interface
     * @param type type
     * @param <T> SPI class type
     * @return registered service
     */
    public static <T extends TypedSPI> T getRegisteredService(final Class<T> spiInterface, final String type) {
        Optional<T> result = findRegisteredService(spiInterface, type);
        if (!result.isPresent()) {
            FailureHandler.noServiceProvider(spiInterface, type);
        }
        return result.get();
    }
    
    /**
     * Get typed registered service.
     *
     * @param spiInterface typed SPI interface
     * @param type type
     * @param props properties
     * @param <T> SPI class type
     * @return registered service
     */
    public static <T extends TypedSPI> T getRegisteredService(final Class<T> spiInterface, final String type, final Properties props) {
        Optional<T> result = findRegisteredService(spiInterface, type, props);
        if (!result.isPresent()) {
            FailureHandler.noServiceProvider(spiInterface, type);
        }
        return result.get();
    }
    
    /**
     * Find typed registered service.
     *
     * @param spiInterface typed SPI interface
     * @param type type
     * @param <T> SPI class type
     * @return registered service
     */
    public static <T extends TypedSPI> Optional<T> findRegisteredService(final Class<T> spiInterface, final String type) {
        for (T each : WormholeServiceLoader.getServiceInstances(spiInterface)) {
            if (matchesType(type, each)) {
                return Optional.of(each);
            }
        }
        return Optional.empty();
    }
    
    /**
     * Find typed registered service.
     *
     * @param spiInterface typed SPI interface
     * @param type type
     * @param props properties
     * @param <T> SPI class type
     * @return registered service
     */
    public static <T extends TypedSPI> Optional<T> findRegisteredService(final Class<T> spiInterface, final String type, final Properties props) {
        for (T each : WormholeServiceLoader.getServiceInstances(spiInterface)) {
            if (matchesType(type, each)) {
                Properties stringTypeProps = convertToStringTypedProperties(props);
                if (each instanceof SPIPostProcessor) {
                    ((SPIPostProcessor) each).postProcessInitialization(stringTypeProps);
                }
                return Optional.of(each);
            }
        }
        return Optional.empty();
    }
    
    private static boolean matchesType(final String type, final TypedSPI typedSPI) {
        return typedSPI.getType().equalsIgnoreCase(type) || typedSPI.getAliases().contains(type);
    }
    
    private static Properties convertToStringTypedProperties(final Properties props) {
        if (null == props) {
            return new Properties();
        }
        Properties result = new Properties();
        props.forEach((key, value) -> result.setProperty(key.toString(), null == value ? null : value.toString()));
        return result;
    }
}
