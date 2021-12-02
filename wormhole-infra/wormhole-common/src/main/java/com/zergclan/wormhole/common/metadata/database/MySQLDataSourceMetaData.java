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

package com.zergclan.wormhole.common.metadata.database;

/**
 * Data source meta data for MySQL.
 */
public final class MySQLDataSourceMetaData implements DataSourceMetaData {
    
    private static final String DEFAULT_HOST_NAME = "127.0.0.1";
    
    private static final int DEFAULT_PORT = 3306;
    
    private final String hostName;
    
    private final int port;
    
    private final String catalogue;
    
    private final String schema;
    
    public MySQLDataSourceMetaData(final String hostName, final Integer port, final String catalogue, final String schema) {
        this.hostName = null == hostName ? DEFAULT_HOST_NAME : hostName;
        this.port = null == port ? DEFAULT_PORT : port;
        this.catalogue = catalogue;
        this.schema = schema;
    }
    
    @Override
    public String getHostName() {
        return this.hostName;
    }
    
    @Override
    public int getPort() {
        return this.port;
    }
    
    @Override
    public String getCatalogue() {
        return this.catalogue;
    }
    
    @Override
    public String getSchema() {
        return this.schema;
    }
}
