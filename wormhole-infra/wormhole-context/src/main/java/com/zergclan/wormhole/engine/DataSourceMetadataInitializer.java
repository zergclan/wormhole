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

package com.zergclan.wormhole.engine;

import com.zergclan.wormhole.core.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.core.metadata.resource.IndexMetadata;
import com.zergclan.wormhole.core.metadata.resource.SchemaMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import com.zergclan.wormhole.jdbc.api.MetaDataLoader;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

@RequiredArgsConstructor
public final class DataSourceMetadataInitializer {

    private final Connection connection;

    private final MetaDataLoader metadataLoader;

    /**
     * Init.
     *
     * @param dataSourceMetadata {@link DataSourceMetadata}
     * @return {@link DataSourceMetadata}
     * @throws SQLException exception
     */
    public DataSourceMetadata init(final DataSourceMetadata dataSourceMetadata) throws SQLException {
        Collection<SchemaMetadata> schemaMetadata = metadataLoader.loadSchemas();
        for (SchemaMetadata each : schemaMetadata) {
            dataSourceMetadata.registerSchema(initSchemaMetadata(each));
        }
        return dataSourceMetadata;
    }

    private SchemaMetadata initSchemaMetadata(final SchemaMetadata schemaMetadata) throws SQLException {
        Collection<TableMetadata> tableMetadata = metadataLoader.loadTables(schemaMetadata.getName());
        for (TableMetadata each : tableMetadata) {
            schemaMetadata.registerTable(initTableMetadata(each));
        }
        return schemaMetadata;
    }

    private TableMetadata initTableMetadata(final TableMetadata tableMetadata) throws SQLException {
        String schema = tableMetadata.getSchema();
        String table = tableMetadata.getName();
        Collection<ColumnMetadata> columnMetadata = metadataLoader.loadColumns(schema, table);
        for (ColumnMetadata each : columnMetadata) {
            tableMetadata.registerColumn(each);
        }
        Collection<IndexMetadata> indexMetadata = metadataLoader.loadIndexes(schema, table);
        for (IndexMetadata each : indexMetadata) {
            tableMetadata.registerIndex(each);
        }
        return tableMetadata;
    }
}
