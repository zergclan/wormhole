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

import com.zergclan.wormhole.common.WormholeException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.UUID;

/**
 * create factory for data source by h2.
 */
public final class DataSourceFactory {

    /**
     * Compatibility modes for IBM DB2.
     */
    private static final String MODEL_DB2 = "DB2";

    /**
     * Compatibility modes for Apache Derby.
     */
    private static final String MODEL_DERBY = "Derby";

    /**
     * Compatibility modes for HSQLDB.
     */
    private static final String MODEL_HSQLDB = "HSQLDB";

    /**
     * Compatibility modes for MS SQL Server.
     */
    private static final String MODEL_SQL_SERVER = "SQL_Server";

    /**
     * Compatibility modes for MySQL.
     */
    private static final String MODEL_MYSQL = "MYSQL";

    /**
     * Compatibility modes for Oracle.
     */
    private static final String MODEL_ORACLE = "ORACLE";

    /**
     * Compatibility modes for PostgreSQL.
     */
    private static final String MODEL_POSTGRESQL = "POSTGRESQL";

    /**
     * Compatibility modes.
     */
    private static final String[] MODES = {MODEL_DB2, MODEL_DERBY, MODEL_HSQLDB, MODEL_SQL_SERVER, MODEL_MYSQL, MODEL_ORACLE, MODEL_POSTGRESQL};

    /**
     * get data source by mode.
     * @param  mode notnull
     * @return DataSource
     */
    public DataSource getDataSource(final String mode) {

        Boolean flag = Arrays.asList(MODES).contains(mode.toUpperCase());
        if (!flag) {
            throw new WormholeException("no support mode");
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName("org.h2.Driver");
        String url = "jdbc:h2:file:~/.h2/" + uuid + ";AUTO_SERVER=TRUE;MODE=" + mode;
        System.out.println(url);
        datasource.setUrl(url);
        datasource.setUsername("sa");
        datasource.setPassword("");

        return datasource;
    }
}
