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

package com.zergclan.wormhole.spi;

import com.zergclan.wormhole.spi.exception.WormholeSPIException;

/**
 * Failure handler for SPI.
 */
public final class FailureHandler {
    
    /**
     * No service provider.
     *
     * @param spiInterface spi interface
     * @param <T> class type of spi
     */
    public static <T> void noServiceProvider(final Class<T> spiInterface) {
        throw new WormholeSPIException(String.format("No service provider of SPI [%s]", spiInterface.getName()));
    }
    
    /**
     * No service provider.
     *
     * @param spiInterface spi interface
     * @param type spi type
     * @param <T> class type of spi
     */
    public static <T> void noServiceProvider(final Class<T> spiInterface, final String type) {
        throw new WormholeSPIException(String.format("No service provider of SPI [%s], with type [%s]", spiInterface.getName(), type));
    }
}
