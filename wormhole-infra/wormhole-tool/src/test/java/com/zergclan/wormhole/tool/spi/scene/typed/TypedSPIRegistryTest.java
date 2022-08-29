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

package com.zergclan.wormhole.tool.spi.scene.typed;

import com.zergclan.wormhole.tool.spi.WormholeServiceLoader;
import com.zergclan.wormhole.tool.spi.exception.WormholeSPIException;
import com.zergclan.wormhole.tool.spi.scene.typed.fixture.TypedSPIFixture;
import com.zergclan.wormhole.tool.spi.scene.typed.fixture.TypedSPIFixtureProperties;
import com.zergclan.wormhole.tool.spi.scene.typed.fixture.TypedSPIFixturePropertiesImpl;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TypedSPIRegistryTest {
    
    static {
        WormholeServiceLoader.register(TypedSPIFixture.class);
        WormholeServiceLoader.register(TypedSPIFixtureProperties.class);
    }
    
    @Test
    public void assertGetRegisteredService() {
        assertNotNull(TypedSPIRegistry.getRegisteredService(TypedSPIFixture.class, "FIXTURE_TYPE"));
        assertNotNull(TypedSPIRegistry.getRegisteredService(TypedSPIFixture.class, "FIXTURE_TYPE_ALIASES"));
        WormholeSPIException exception = assertThrows(WormholeSPIException.class, () -> TypedSPIRegistry.getRegisteredService(TypedSPIFixture.class, "FIXTURE_TYPE_ERROR"));
        assertNotNull(exception);
        assertEquals("No service provider of SPI [com.zergclan.wormhole.tool.spi.scene.typed.fixture.TypedSPIFixture], with type [FIXTURE_TYPE_ERROR]", exception.getMessage());
    }
    
    @Test
    public void assertGetRegisteredServiceProperties() {
        Properties props = new Properties();
        props.put("key", "value");
        TypedSPIFixtureProperties typedSPIFixtureProperties = TypedSPIRegistry.getRegisteredService(TypedSPIFixtureProperties.class, "FIXTURE_PROPERTIES", props);
        assertInstanceOf(TypedSPIFixturePropertiesImpl.class, typedSPIFixtureProperties);
        assertEquals("value", ((TypedSPIFixturePropertiesImpl) typedSPIFixtureProperties).getValue());
    }
}
