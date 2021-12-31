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

import com.zergclan.wormhole.common.SystemConstant;

import java.util.Properties;

/**
 * Meta data for PostgreSQL data base.
 */
public final class PostgreSQLDatabaseMetaData extends DatabaseMetaData {

    private static final DatabaseType TYPE = DatabaseType.POSTGRESQL;

    private final Properties parameters;

    public PostgreSQLDatabaseMetaData(final String hostName, final int port, final String catalog, final Properties parameters) {
        super(TYPE, hostName, port, catalog);
        this.parameters = parameters;
    }

    @Override
    protected String getJdbcUrl() {
        return getDatabaseType().getProtocol() + getHost() + SystemConstant.COLON + getPort() + SystemConstant.FORWARD_SLASH + getCatalog();
    }
}
