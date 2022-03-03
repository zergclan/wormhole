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

package com.zergclan.wormhole.creator;

import com.zergclan.wormhole.common.WormholeException;
import com.zergclan.wormhole.core.config.DataSourceConfiguration;
import com.zergclan.wormhole.core.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.resource.DatabaseType;
import com.zergclan.wormhole.core.metadata.resource.dialect.H2DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.resource.dialect.MySQLDataSourceMetadata;
import com.zergclan.wormhole.core.metadata.resource.dialect.OracleDataSourceMetadata;
import com.zergclan.wormhole.core.metadata.resource.dialect.PostgreSQLDataSourceMetadata;
import com.zergclan.wormhole.core.metadata.resource.dialect.SQLServerDataSourceMetadata;
import com.zergclan.wormhole.engine.DataSourceMetadataInitializer;
import com.zergclan.wormhole.jdbc.DataSourceManger;
import com.zergclan.wormhole.jdbc.api.MetaDataLoader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

/**
 * Metadata creator of {@link DataSourceMetadata}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DatasourceMetadataCreator {

    /**
     * Create {@link DataSourceMetadata}.
     *
     * @param configuration {@link DataSourceConfiguration}
     * @return {@link DataSourceMetadata}
     * @throws SQLException exception
     */
    public static DataSourceMetadata create(final DataSourceConfiguration configuration) throws SQLException {
        DataSourceMetadata result = createActualTypeDataSourceMetadata(configuration);
        DataSourceMetadataInitializer dataSourceMetadataInitializer = new DataSourceMetadataInitializer(DataSourceManger.get(result).getConnection(), createMetadataLoader());
        return dataSourceMetadataInitializer.init(result);
    }

    // TODO create metadata loader
    private static MetaDataLoader createMetadataLoader() {
        return null;
    }

    private static DataSourceMetadata createActualTypeDataSourceMetadata(final DataSourceConfiguration configuration) {
        Optional<DatabaseType> databaseType = DatabaseType.getDatabaseType(configuration.getType());
        if (databaseType.isPresent()) {
            String host = configuration.getHost();
            int port = configuration.getPort();
            String catalog = configuration.getCatalog();
            String username = configuration.getUsername();
            String password = configuration.getPassword();
            Properties parameters = configuration.getParameters();
            DatabaseType type = databaseType.get();
            if (DatabaseType.MYSQL == type) {
                return new MySQLDataSourceMetadata(host, port, username, password, catalog, parameters);
            }
            if (DatabaseType.ORACLE == type) {
                return new OracleDataSourceMetadata(host, port, username, password, catalog, parameters);
            }
            if (DatabaseType.SQL_SERVER == type) {
                return new SQLServerDataSourceMetadata(host, port, username, password, catalog, parameters);
            }
            if (DatabaseType.POSTGRESQL == type) {
                return new PostgreSQLDataSourceMetadata(host, port, username, password, catalog, parameters);
            }
            if (DatabaseType.H2 == type) {
                return new H2DataSourceMetadata(host, port, username, password, catalog, parameters);
            }
        }
        throw new WormholeException("error : create data source metadata failed databaseType [%s] not find", configuration.getType());
    }
}
