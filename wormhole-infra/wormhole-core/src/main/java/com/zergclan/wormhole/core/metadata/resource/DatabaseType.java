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

/**
 * Type of database.
 */
@Getter
public enum DatabaseType {
    
    /**
     * Compatibility modes for MySQL.
     */
    MYSQL("MySQL", "jdbc:mysql://", 3306),
    
    /**
     * Compatibility modes for Oracle.
     */
    ORACLE("Oracle", "jdbc:oracle:thin:@", 1521),
    
    /**
     * Compatibility modes for MS SQL Server.
     */
    SQL_SERVER("SQL_Server", "jdbc:microsoft:sqlserver://", 1433),
    
    /**
     * Compatibility modes for PostgreSQL.
     */
    POSTGRESQL("PostgreSQL", "jdbc:postgresql://", 5431),
    
    /**
     * Compatibility modes for H2.
     */
    H2("H2", "jdbc:h2:", -1);
    
    private final String name;
    
    private final String protocol;
    
    private final Integer defaultPort;
    
    DatabaseType(final String name, final String protocol, final Integer defaultPort) {
        this.name = name;
        this.protocol = protocol;
        this.defaultPort = defaultPort;
    }
}
