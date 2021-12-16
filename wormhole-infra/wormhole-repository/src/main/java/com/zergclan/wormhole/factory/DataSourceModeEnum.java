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

package com.zergclan.wormhole.factory;

/**
 * the enum of h2 mode.
 * @author wgz
 */
public enum DataSourceModeEnum {
    /**
     * Compatibility modes for IBM DB2.
     */
    DB2("DB2"),
    /**
     * Compatibility modes for Apache Derby.
     */
    DERBY("Derby"),
    /**
     * Compatibility modes for HSQLDB.
     */
    HSQLDB("HSQLDB"),
    /**
     * Compatibility modes for MS SQL Server.
     */
    SQL_SERVER("SQL_Server"),
    /**
     * Compatibility modes for MySQL.
     */
    MYSQL("MySQL"),
    /**
     * Compatibility modes for Oracle.
     */
    ORACLE("Oracle"),
    /**
     * Compatibility modes for PostgreSQL.
     */
    POSTGRESQL("PostgreSQL");

    /**
     * modes value.
     */
    private String value;

    DataSourceModeEnum(final String value) {
        this.value = value;
    }

    /**
     * set value.
     * @param value notnull
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * get vulue.
     * @return string
     */
    public String getValue() {
        return this.value;
    }

}
