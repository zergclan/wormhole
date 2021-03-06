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

package com.zergclan.wormhole.metadata.core.loader;

import com.zergclan.wormhole.common.exception.WormholeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * MetaData loader factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MetaDataLoaderFactory {
    
    /**
     * get instance.
     *
     * @param connection {@link Connection}
     * @return {@link MetaDataLoader}
     * @throws SQLException SQL exception
     */
    public static MetaDataLoader getInstance(final Connection connection) throws SQLException {
        String databaseProductName = connection.getMetaData().getDatabaseProductName().toUpperCase();
        switch (databaseProductName) {
            case "H2" :
            case "MYSQL" :
                return new MySQLMetaDataLoader(connection);
            case "ORACLE" :
                return new OracleMetaDataLoader(connection);
            default :
                throw new WormholeException("error: can not find implemented meta data loader named: " + databaseProductName);
        }
    }
}
