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

package com.zergclan.wormhole.core.metadata;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MetaDataTest {

    @Test
    public void assertMySQLDataSourceMetaData() {
        MySQLDataSourceMetaData mySQLDataSourceMetaData = new MySQLDataSourceMetaData("127.0.0.1", 3306, "mysql_db", new Properties());
        assertEquals("127.0.0.1", mySQLDataSourceMetaData.getHostName());
        assertEquals(3306, mySQLDataSourceMetaData.getPort());
        assertEquals("mysql_db", mySQLDataSourceMetaData.getCatalog());
        assertEquals("127.0.0.1:3306/mysql_db", mySQLDataSourceMetaData.getUrl());
    }
}
