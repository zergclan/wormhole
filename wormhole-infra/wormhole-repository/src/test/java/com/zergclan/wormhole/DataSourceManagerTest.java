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

package com.zergclan.wormhole;

import com.zergclan.wormhole.core.metadata.resource.dialect.MySQLDataSourceMetadata;
import org.junit.jupiter.api.Assertions;

import java.util.Properties;

/**
 * Test datasource manager.
 */
public final class DataSourceManagerTest {

    /**
     * Test datasource manager.
     */
//    @Test
    public void test() {
        MySQLDataSourceMetadata dataSourceMetadata = getDataSourceMetadata();
        String identifier = dataSourceMetadata.getIdentifier();
        Assertions.assertNotNull(DataSourceManager.register(dataSourceMetadata));
        Assertions.assertNotNull(DataSourceManager.get(identifier));
        Assertions.assertTrue(DataSourceManager.unRegister(identifier));
        Assertions.assertNull(DataSourceManager.get(identifier));
    }

    /**
     * Create datasource metadata.
     *
     * @return
     */
    private MySQLDataSourceMetadata getDataSourceMetadata() {
        int port = 3306;
        String host = "192.168.112.132";
        String username = "root";
        String password = "koma1993";
        String catalog = "cboard";
        Properties properties = new Properties();
        properties.setProperty("serverTimezone", "UTC");
        properties.setProperty("useSSL", "false");
        return new MySQLDataSourceMetadata(host, port, username, password, catalog, properties);
    }

}
