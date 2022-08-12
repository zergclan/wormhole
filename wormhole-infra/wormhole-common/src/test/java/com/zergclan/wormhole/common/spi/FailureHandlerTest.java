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

package com.zergclan.wormhole.common.spi;

import com.zergclan.wormhole.common.spi.exception.WormholeSPIException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FailureHandlerTest {
    
    @Test
    public void assertNoServiceProvider() {
        WormholeSPIException exception = assertThrows(WormholeSPIException.class, () -> FailureHandler.noServiceProvider(Object.class));
        assertEquals("No service provider of SPI [java.lang.Object]", exception.getMessage());
    }
    
    @Test
    public void assertNoTypedServiceProvider() {
        WormholeSPIException exception = assertThrows(WormholeSPIException.class, () -> FailureHandler.noServiceProvider(Object.class, "SPI_TYPE"));
        assertEquals("No service provider of SPI [java.lang.Object], with type [SPI_TYPE]", exception.getMessage());
    }
}
