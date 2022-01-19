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

import lombok.Getter;

import java.util.Optional;

/**
 * Type of database.
 */
@Getter
public enum DatabaseType {
    
    /**
     * Compatibility modes for MySQL.
     */
    MYSQL("MySQL", "jdbc:mysql://", 3306, "com.mysql.cj.jdbc.Driver"),
    
    /**
     * Compatibility modes for Oracle.
     */
    ORACLE("Oracle", "jdbc:oracle:thin:@", 1521, "oracle.jdbc.OracleDriver"),
    
    /**
     * Compatibility modes for MS SQL Server.
     */
    SQL_SERVER("SQL_Server", "jdbc:microsoft:sqlserver://", 1433, "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    
    /**
     * Compatibility modes for PostgreSQL.
     */
    POSTGRESQL("PostgreSQL", "jdbc:postgresql://", 5431, "org.postgresql.Driver"),
    
    /**
     * Compatibility modes for H2.
     */
    H2("H2", "jdbc:h2:", -1, "org.h2.Driver");
    
    private final String name;
    
    private final String protocol;
    
    private final Integer defaultPort;

    private final String defaultDriverClassName;
    
    DatabaseType(final String name, final String protocol, final Integer defaultPort, final String defaultDriverClassName) {
        this.name = name;
        this.protocol = protocol;
        this.defaultPort = defaultPort;
        this.defaultDriverClassName = defaultDriverClassName;
    }

    /**
     * Get {@link DatabaseType} by code.
     *
     * @param name name
     * @return {@link DatabaseType}
     */
    public static Optional<DatabaseType> getDatabaseType(final String name) {
        if (MYSQL.name.equalsIgnoreCase(name)) {
            return Optional.of(MYSQL);
        } else if (ORACLE.name.equalsIgnoreCase(name)) {
            return Optional.of(ORACLE);
        } else if (SQL_SERVER.name.equalsIgnoreCase(name)) {
            return Optional.of(SQL_SERVER);
        } else if (POSTGRESQL.name.equalsIgnoreCase(name)) {
            return Optional.of(POSTGRESQL);
        } else if (H2.name.equalsIgnoreCase(name)) {
            return Optional.of(H2);
        }
        return Optional.empty();
    }
}
