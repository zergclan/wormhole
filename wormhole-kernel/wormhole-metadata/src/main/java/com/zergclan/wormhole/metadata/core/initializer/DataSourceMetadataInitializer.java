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

package com.zergclan.wormhole.metadata.core.initializer;

import com.zergclan.wormhole.common.exception.WormholeException;
import com.zergclan.wormhole.config.core.DataSourceConfiguration;
import com.zergclan.wormhole.metadata.core.loader.DataSourceManger;
import com.zergclan.wormhole.metadata.core.loader.MetaDataLoader;
import com.zergclan.wormhole.metadata.core.loader.MetaDataLoaderFactory;
import com.zergclan.wormhole.metadata.core.resource.ColumnMetaData;
import com.zergclan.wormhole.metadata.core.resource.DatabaseType;
import com.zergclan.wormhole.metadata.core.resource.IndexMetaData;
import com.zergclan.wormhole.metadata.core.resource.SchemaMetaData;
import com.zergclan.wormhole.metadata.core.resource.TableMetaData;
import com.zergclan.wormhole.metadata.core.resource.dialect.H2DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.resource.dialect.MySQLDataSourceMetaData;
import com.zergclan.wormhole.metadata.core.resource.dialect.OracleDataSourceMetaData;
import com.zergclan.wormhole.metadata.core.resource.dialect.PostgreSQLDataSourceMetaData;
import com.zergclan.wormhole.metadata.core.resource.dialect.SQLServerDataSourceMetaData;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

/**
 * Data source metadata initializer.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataSourceMetadataInitializer {
    
    /**
     * Create {@link DataSourceMetaData} by actual type.
     *
     * @param configuration {@link DataSourceConfiguration}
     * @return {@link DataSourceMetaData}
     * @throws SQLException SQL Exception
     */
    public static DataSourceMetaData createActualTypeDataSourceMetadata(final DataSourceConfiguration configuration) throws SQLException {
        Optional<DatabaseType> databaseType = DatabaseType.getDatabaseType(configuration.getType());
        if (databaseType.isPresent()) {
            String host = configuration.getHost();
            int port = configuration.getPort();
            String catalog = configuration.getCatalog();
            String username = configuration.getUsername();
            String password = configuration.getPassword();
            Properties parameters = configuration.getProps();
            DatabaseType type = databaseType.get();
            if (DatabaseType.MYSQL == type) {
                return new MySQLDataSourceMetaData(host, port, username, password, catalog, parameters);
            }
            if (DatabaseType.ORACLE == type) {
                return new OracleDataSourceMetaData(host, port, username, password, catalog, parameters);
            }
            if (DatabaseType.SQL_SERVER == type) {
                return new SQLServerDataSourceMetaData(host, port, username, password, catalog, parameters);
            }
            if (DatabaseType.POSTGRESQL == type) {
                return new PostgreSQLDataSourceMetaData(host, port, username, password, catalog, parameters);
            }
            if (DatabaseType.H2 == type) {
                return new H2DataSourceMetaData(host, port, username, password, catalog, parameters);
            }
        }
        throw new WormholeException("error : create data source metadata failed databaseType [%s] not find", configuration.getType());
    }
    
    /**
     * Init {@link DataSourceMetaData}.
     *
     * @param dataSourceMetaData {@link DataSourceMetaData}
     * @throws SQLException SQL Exception
     */
    public static void init(final DataSourceMetaData dataSourceMetaData) throws SQLException {
        initDataSource(dataSourceMetaData, MetaDataLoaderFactory.getInstance(DataSourceManger.get(dataSourceMetaData).getConnection()));
    }
    
    private static void initDataSource(final DataSourceMetaData dataSource, final MetaDataLoader metadataLoader) throws SQLException {
        for (SchemaMetaData each : metadataLoader.loadSchemas(dataSource.getIdentifier())) {
            initSchema(each, metadataLoader);
            dataSource.registerSchema(each);
        }
    }
    
    private static void initSchema(final SchemaMetaData schema, final MetaDataLoader metadataLoader) throws SQLException {
        for (TableMetaData each : metadataLoader.loadTables(schema.getDataSourceIdentifier(), schema.getName())) {
            initTable(each, metadataLoader);
            schema.registerTable(each);
        }
    }
    
    private static void initTable(final TableMetaData table, final MetaDataLoader metadataLoader) throws SQLException {
        Collection<ColumnMetaData> columnMetadata = metadataLoader.loadColumns(table.getDataSourceIdentifier(), table.getSchema(), table.getName());
        for (ColumnMetaData each : columnMetadata) {
            table.registerColumn(each);
        }
        Collection<IndexMetaData> indexMetadata = metadataLoader.loadIndexes(table.getDataSourceIdentifier(), table.getSchema(), table.getName());
        for (IndexMetaData each : indexMetadata) {
            table.registerIndex(each);
        }
    }
}
