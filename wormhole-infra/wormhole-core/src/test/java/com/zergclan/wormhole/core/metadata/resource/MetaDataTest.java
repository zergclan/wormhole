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

package com.zergclan.wormhole.core.metadata.resource;

import com.zergclan.wormhole.core.api.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.resource.dialect.MySQLDataSourceMetadata;
import com.zergclan.wormhole.core.metadata.resource.dialect.PostgreSQLDataSourceMetadata;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MetaDataTest {

    @Test
    public void assertMySQLDataSourceMetadata() {
        DataSourceMetadata dataSource = new MySQLDataSourceMetadata("127.0.0.1", 3306, "root", "root", "mysql_db", new Properties());
        assertEquals("jdbc:mysql://127.0.0.1:3306/mysql_db", dataSource.getJdbcUrl());
        assertEquals("com.mysql.cj.jdbc.Driver", dataSource.getDriverClassName());
        assertEquals("root", dataSource.getUsername());
        assertEquals("root", dataSource.getPassword());
        assertEquals("MySQL#127.0.0.1:3306:mysql_db#root@root", dataSource.getIdentifier());
    }

    @Test
    public void assertPostgreSQLDataSourceMetadata() {
        DataSourceMetadata dataSource = new PostgreSQLDataSourceMetadata("127.0.0.1", 5432, "root", "root", "pg_db", new Properties());
        assertEquals("jdbc:postgresql://127.0.0.1:5432/pg_db", dataSource.getJdbcUrl());
        assertEquals("org.postgresql.Driver", dataSource.getDriverClassName());
        assertEquals("root", dataSource.getUsername());
        assertEquals("root", dataSource.getPassword());
        assertEquals("PostgreSQL#127.0.0.1:5432:pg_db#root@root", dataSource.getIdentifier());
    }
}
