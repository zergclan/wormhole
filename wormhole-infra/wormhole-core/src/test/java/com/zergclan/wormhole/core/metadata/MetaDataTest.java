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
        assertEquals("jdbc:mysql://127.0.0.1:3306/mysql_db", jdbcUrlOptional.get());
    }

    @Test
    public void assertOracleDataSourceMetaData() {
        DatabaseMetaData databaseMetaData = new OracleDatabaseMetaData("127.0.0.1", 1521, new Properties());
        assertEquals(DatabaseType.ORACLE, databaseMetaData.getDatabaseType());
        assertEquals("127.0.0.1", databaseMetaData.getHost());
        assertEquals(1521, databaseMetaData.getPort());
        Optional<String> urlOptional = databaseMetaData.getUrl("ORCL");
        assertFalse(urlOptional.isPresent());
        databaseMetaData.addSchema(new SchemaMetaData(databaseMetaData.getIdentifier(), "ORCL"));
        Optional<String> jdbcUrlOptional = databaseMetaData.getUrl("ORCL");
        assertTrue(jdbcUrlOptional.isPresent());
        assertEquals("jdbc:oracle:thin:@127.0.0.1:1521:ORCL", jdbcUrlOptional.get());
    }

    @Test
    public void assertDB2DataSourceMetaData() {
        DatabaseMetaData databaseMetaData = new DB2DatabaseMetaData("127.0.0.1", 6789, new Properties());
        assertEquals(DatabaseType.DB2, databaseMetaData.getDatabaseType());
        assertEquals("127.0.0.1", databaseMetaData.getHost());
        assertEquals(6789, databaseMetaData.getPort());
        Optional<String> urlOptional = databaseMetaData.getUrl("db2_db");
        assertFalse(urlOptional.isPresent());
        databaseMetaData.addSchema(new SchemaMetaData(databaseMetaData.getIdentifier(), "db2_db"));
        Optional<String> jdbcUrlOptional = databaseMetaData.getUrl("db2_db");
        assertTrue(jdbcUrlOptional.isPresent());
        assertEquals("jdbc:db2://127.0.0.1:6789/db2_db", jdbcUrlOptional.get());
    }

    @Test
    public void assertSQLServerDataSourceMetaData() {
        DatabaseMetaData databaseMetaData = new SQLServerDatabaseMetaData("127.0.0.1", 1433, new Properties());
        assertEquals(DatabaseType.SQL_SERVER, databaseMetaData.getDatabaseType());
        assertEquals("127.0.0.1", databaseMetaData.getHost());
        assertEquals(1433, databaseMetaData.getPort());
        Optional<String> urlOptional = databaseMetaData.getUrl("sql_server_db");
        assertFalse(urlOptional.isPresent());
        databaseMetaData.addSchema(new SchemaMetaData(databaseMetaData.getIdentifier(), "sql_server_db"));
        Optional<String> jdbcUrlOptional = databaseMetaData.getUrl("sql_server_db");
        assertTrue(jdbcUrlOptional.isPresent());
        assertEquals("jdbc:microsoft:sqlserver://127.0.0.1:1433/sql_server_db", jdbcUrlOptional.get());
    }

    @Test
    public void assertPostgreSQLDataSourceMetaData() {
        DatabaseMetaData databaseMetaData = new PostgreSQLDatabaseMetaData("127.0.0.1", 5432, new Properties());
        assertEquals(DatabaseType.POSTGRESQL, databaseMetaData.getDatabaseType());
        assertEquals("127.0.0.1", databaseMetaData.getHost());
        assertEquals(5432, databaseMetaData.getPort());
        Optional<String> urlOptional = databaseMetaData.getUrl("pg_db");
        assertFalse(urlOptional.isPresent());
        databaseMetaData.addSchema(new SchemaMetaData(databaseMetaData.getIdentifier(), "pg_db"));
        Optional<String> jdbcUrlOptional = databaseMetaData.getUrl("pg_db");
        assertTrue(jdbcUrlOptional.isPresent());
        assertEquals("jdbc:postgresql://127.0.0.1:5432/pg_db", jdbcUrlOptional.get());
    }
}
