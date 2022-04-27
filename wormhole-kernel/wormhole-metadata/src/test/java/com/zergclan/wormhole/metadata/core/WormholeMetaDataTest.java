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

package com.zergclan.wormhole.metadata.core;

import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.plan.PlanMetaData;
import com.zergclan.wormhole.metadata.core.resource.dialect.MySQLDataSourceMetaData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class WormholeMetaDataTest {
    
    private static WormholeMetaData metaData;
    
    @BeforeAll
    public static void init() {
        metaData = new WormholeMetaData(new LinkedHashMap<>(), new LinkedHashMap<>());
    }
    
    @Test
    public void assertRegister() {
        DataSourceMetaData dataSourceMetaData = new MySQLDataSourceMetaData("127.0.0.1", 3306, "root", "root", "source-ds", new Properties());
        metaData.register(dataSourceMetaData);
        PlanMetaData planMetaData = new PlanMetaData("test-plan", PlanMetaData.ExecutionMode.ONE_OFF, "yyyy-MM-dd HH:mm:ss", false, new LinkedHashMap<>());
        metaData.register(planMetaData);
        assertNotNull(metaData.getDataSources().get(dataSourceMetaData.getIdentifier()));
        assertTrue(metaData.getPlan(planMetaData.getIdentifier()).isPresent());
    }
    
    @Test
    public void assertGetIdentifier() {
        assertEquals("wormhole", metaData.getIdentifier());
    }
}
