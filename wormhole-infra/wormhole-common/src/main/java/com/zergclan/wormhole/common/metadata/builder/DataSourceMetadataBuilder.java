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

package com.zergclan.wormhole.common.metadata.builder;

import com.zergclan.wormhole.common.configuration.DataSourceConfiguration;
import com.zergclan.wormhole.common.metadata.datasource.DataSourcePoolMetadata;
import com.zergclan.wormhole.common.metadata.datasource.WormholeDataSourceMetaData;
import com.zergclan.wormhole.common.metadata.datasource.ColumnMetaData;
import com.zergclan.wormhole.common.metadata.datasource.IndexMetaData;
import com.zergclan.wormhole.common.metadata.datasource.SchemaMetaData;
import com.zergclan.wormhole.common.metadata.datasource.TableMetaData;
import com.zergclan.wormhole.common.metadata.loader.MetaDataLoader;
import com.zergclan.wormhole.common.metadata.loader.MetaDataLoaderBuilder;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Data source metadata builder.
 */
public final class DataSourceMetadataBuilder {
    
    private final DataSourcePoolMetadataBuilder dataSourcePoolMetadataBuilder = new DataSourcePoolMetadataBuilder();
    
    /**
     * Build {@link WormholeDataSourceMetaData} by actual type.
     *
     * @param configuration {@link DataSourceConfiguration}
     * @return {@link WormholeDataSourceMetaData}
     */
    public WormholeDataSourceMetaData buildActualTypedDataSourceMetadata(final DataSourceConfiguration configuration) {
        DataSourcePoolMetadata pool = dataSourcePoolMetadataBuilder.build(configuration.getPool());
        return new WormholeDataSourceMetaData(configuration.getName(), configuration.getType(), configuration.getUrl(), configuration.getUsername(), configuration.getPassword(), pool);
    }
    
    /**
     * Build {@link WormholeDataSourceMetaData} by metadata loader.
     *
     * @param dataSourceMetaData {@link WormholeDataSourceMetaData}
     * @throws SQLException SQL Exception
     */
    public void buildLoadedDataSourceMetadata(final WormholeDataSourceMetaData dataSourceMetaData) throws SQLException {
        initDataSource(dataSourceMetaData, MetaDataLoaderBuilder.build(dataSourceMetaData));
    }
    
    private void initDataSource(final WormholeDataSourceMetaData dataSource, final MetaDataLoader metadataLoader) throws SQLException {
        Collection<String> relatedSchemaNames = dataSource.getRelatedSchemaNames();
        for (SchemaMetaData each : metadataLoader.loadSchemas(dataSource.getIdentifier())) {
            if (relatedSchemaNames.contains(each.getName())) {
                initSchema(each, metadataLoader);
                dataSource.registerSchema(each);
            }
        }
    }
    
    private void initSchema(final SchemaMetaData schema, final MetaDataLoader metadataLoader) throws SQLException {
        for (TableMetaData each : metadataLoader.loadTables(schema.getDataSourceIdentifier(), schema.getName())) {
            initTable(each, metadataLoader);
            schema.registerTable(each);
        }
    }
    
    private void initTable(final TableMetaData table, final MetaDataLoader metadataLoader) throws SQLException {
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
