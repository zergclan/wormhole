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
        DatabaseMetaData databaseMetaData = new MySQLDatabaseMetaData("127.0.0.1", 3306, "mysql_db", new Properties());
        assertEquals(DatabaseType.MYSQL, databaseMetaData.getDatabaseType());
        assertEquals("127.0.0.1", databaseMetaData.getHost());
        assertEquals(3306, databaseMetaData.getPort());
        assertEquals("jdbc:mysql://127.0.0.1:3306/mysql_db", databaseMetaData.getJdbcUrl());
    }

    @Test
    public void assertOracleDataSourceMetaData() {
        DatabaseMetaData databaseMetaData = new OracleDatabaseMetaData("127.0.0.1", 1521, "ORCL", new Properties());
        assertEquals(DatabaseType.ORACLE, databaseMetaData.getDatabaseType());
        assertEquals("127.0.0.1", databaseMetaData.getHost());
        assertEquals(1521, databaseMetaData.getPort());
        assertEquals("jdbc:oracle:thin:@127.0.0.1:1521:ORCL", databaseMetaData.getJdbcUrl());
    }

    @Test
    public void assertDB2DataSourceMetaData() {
        DatabaseMetaData databaseMetaData = new DB2DatabaseMetaData("127.0.0.1", 6789, "db2_db", new Properties());
        assertEquals(DatabaseType.DB2, databaseMetaData.getDatabaseType());
        assertEquals("127.0.0.1", databaseMetaData.getHost());
        assertEquals(6789, databaseMetaData.getPort());
        assertEquals("jdbc:db2://127.0.0.1:6789/db2_db", databaseMetaData.getJdbcUrl());
    }

    @Test
    public void assertSQLServerDataSourceMetaData() {
        DatabaseMetaData databaseMetaData = new SQLServerDatabaseMetaData("127.0.0.1", 1433, "sql_server_db", new Properties());
        assertEquals(DatabaseType.SQL_SERVER, databaseMetaData.getDatabaseType());
        assertEquals("127.0.0.1", databaseMetaData.getHost());
        assertEquals(1433, databaseMetaData.getPort());
        assertEquals("jdbc:microsoft:sqlserver://127.0.0.1:1433/sql_server_db", databaseMetaData.getJdbcUrl());
    }

    @Test
    public void assertPostgreSQLDataSourceMetaData() {
        DatabaseMetaData databaseMetaData = new PostgreSQLDatabaseMetaData("127.0.0.1", 5432, "pg_db", new Properties());
        assertEquals(DatabaseType.POSTGRESQL, databaseMetaData.getDatabaseType());
        assertEquals("127.0.0.1", databaseMetaData.getHost());
        assertEquals(5432, databaseMetaData.getPort());
        assertEquals("jdbc:postgresql://127.0.0.1:5432/pg_db", databaseMetaData.getJdbcUrl());
    }
}
