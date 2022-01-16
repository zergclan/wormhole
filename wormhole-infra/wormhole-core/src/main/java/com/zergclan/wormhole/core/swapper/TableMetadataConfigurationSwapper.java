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

package com.zergclan.wormhole.core.swapper;

import com.zergclan.wormhole.core.config.ColumnConfiguration;
import com.zergclan.wormhole.core.config.TableConfiguration;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Table metadata configuration swapper.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TableMetadataConfigurationSwapper {

    /**
     * swap to {@link TableMetadata}.
     *
     * @param tableConfiguration {@link TableConfiguration}
     * @param dataSourceIdentifier data source identifier
     * @param schemaName schema name
     * @return {@link TableMetadata}
     */
    public static TableMetadata swapToMetadata(final TableConfiguration tableConfiguration, final String dataSourceIdentifier, final String schemaName) {
        TableMetadata result = new TableMetadata(dataSourceIdentifier, schemaName, tableConfiguration.getName(), tableConfiguration.getComment());
        registerColumns(result, tableConfiguration, dataSourceIdentifier, schemaName);
        registerIndexes(result, tableConfiguration, dataSourceIdentifier, schemaName);
        return result;
    }

    private static void registerColumns(final TableMetadata tableMetadata, final TableConfiguration tableConfiguration, final String dataSourceIdentifier, final String schemaName) {
        Map<String, ColumnConfiguration> columns = tableConfiguration.getColumns();
        String tableName = tableMetadata.getName();
        for (Map.Entry<String, ColumnConfiguration> entry : columns.entrySet()) {
            tableMetadata.registerColumn(ColumnMetadataConfigurationSwapper.swapToMetadata(entry.getValue(), dataSourceIdentifier, schemaName, tableName));
        }

    }

    private static void registerIndexes(final TableMetadata tableMetadata, final TableConfiguration tableConfiguration, final String dataSourceIdentifier, final String schemaName) {
    }
}
