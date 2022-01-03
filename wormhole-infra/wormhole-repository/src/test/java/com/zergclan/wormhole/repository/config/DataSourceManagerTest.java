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

package com.zergclan.wormhole.repository.config;

import com.zergclan.wormhole.repository.entity.DataSourceInformation;
import org.junit.jupiter.api.Assertions;

/**
 * Data source manager test.
 */
public class DataSourceManagerTest {

    /**
     * Test register data source.
     */
//    @Test
    public void registerDataSource() {
        DataSourceInformation dataSourceInformation = new DataSourceInformation();
        dataSourceInformation.setId("test");
        dataSourceInformation.setDbUser("root");
        dataSourceInformation.setDbPassword("123456");
        dataSourceInformation.setDbType("mysql");
        dataSourceInformation.setJdbcUrl("jdbc:mysql://10.168.1.10:3306?characterEncoding=utf-8&useSSL=false");
        Assertions.assertNotNull(DataSourceManager.registerDataSource(dataSourceInformation));
    }

    /**
     * Test unRegister data source.
     */
//    @Test
    public void unRegisterDataSource() {
        registerDataSource();
        Assertions.assertTrue(DataSourceManager.unRegisterDataSource("test"));
    }

    /**
     * Test get data source.
     */
//    @Test
    public void getDataSource() {
        registerDataSource();
        Assertions.assertNotNull(DataSourceManager.getDataSource("test"));
    }

}
