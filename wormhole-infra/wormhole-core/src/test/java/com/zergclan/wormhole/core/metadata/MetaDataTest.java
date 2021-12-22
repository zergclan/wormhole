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

import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class MetaDataTest {

    @Test
    public void assertMySQLDataSourceMetaData() {
        DatabaseMetaData databaseMetaData = new MySQLDatabaseMetaData("127.0.0.1", 3306, new Properties());
        assertEquals(DatabaseType.MYSQL, databaseMetaData.getDatabaseType());
        assertEquals("127.0.0.1", databaseMetaData.getHost());
        assertEquals(3306, databaseMetaData.getPort());
        Optional<String> urlOptional = databaseMetaData.getUrl("mysql_db");
        assertFalse(urlOptional.isPresent());
        databaseMetaData.addSchema(new SchemaMetaData(databaseMetaData.getIdentifier(), "mysql_db"));
        Optional<String> jdbcUrlOptional = databaseMetaData.getUrl("mysql_db");
        assertTrue(jdbcUrlOptional.isPresent());
        assertEquals("jdbc:mysql//127.0.0.1:3306/mysql_db", jdbcUrlOptional.get());
    }
}
