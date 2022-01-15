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

package com.zergclan.wormhole.repository;

/**
 * Database enum.
 */
public enum DbTypeEnum {

    MYSQL("mysql", "com.mysql.cj.jdbc.Driver", "select 1", "mysql数据库"),
    ORACLE("oracle", "oracle.jdbc.OracleDriver", "select 1", "mysql数据库");

    private final String db;
    private final String driverClassName;
    private final String testSql;
    private final String desc;

    DbTypeEnum(final String db, final String driverClassName, final String testSql, final String desc) {
        this.db = db;
        this.driverClassName = driverClassName;
        this.testSql = testSql;
        this.desc = desc;
    }

    /**
     * Get dbEnum by dbType.
     * @param dbType Database type.
     * @return DbEnum.
     */
    public static DbTypeEnum getDbType(final String dbType) {
        DbTypeEnum[] dbTypeEnums = values();
        int count = dbTypeEnums.length;
        for (int i = 0; i < count; ++i) {
            DbTypeEnum type = dbTypeEnums[i];
            if (type.db.equalsIgnoreCase(dbType)) {
                return type;
            }
        }
        return MYSQL;
    }

    /**
     * Get dbType.
     * @return DbType.
     */
    public String getDb() {
        return db;
    }

    /**
     * Get database driver className.
     * @return Driver className.
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * Get database desc.
     * @return Database desc.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Get database testSql.
     * @return TestSql.
     */
    public String getTestSql() {
        return testSql;
    }
}
