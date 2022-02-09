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

package com.zergclan.wormhole.swapper;

import com.zergclan.wormhole.core.config.SchemaConfiguration;
import com.zergclan.wormhole.core.config.TableConfiguration;
import com.zergclan.wormhole.core.metadata.resource.SchemaMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Schema metadata configuration swapper.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SchemaMetadataConfigurationSwapper {

    /**
     * Swap to {@link SchemaMetadata}.
     *
     * @param schemaConfiguration {@link SchemaConfiguration}
     * @param dataSourceIdentifier data source identifier
     * @return {@link SchemaMetadata}
     */
    public static SchemaMetadata swapToMetadata(final SchemaConfiguration schemaConfiguration, final String dataSourceIdentifier) {
        SchemaMetadata result = new SchemaMetadata(dataSourceIdentifier, schemaConfiguration.getName());
        registerTables(result, schemaConfiguration, dataSourceIdentifier);
        return result;
    }

    private static void registerTables(final SchemaMetadata schemaMetadata, final SchemaConfiguration schemaConfiguration, final String dataSourceIdentifier) {
        String schemaName = schemaConfiguration.getName();
        for (Map.Entry<String, TableConfiguration> entry : schemaConfiguration.getTables().entrySet()) {
            TableMetadata tableMetadata = TableMetadataConfigurationSwapper.swapToMetadata(entry.getValue(), dataSourceIdentifier, schemaName);
            schemaMetadata.registerTable(tableMetadata);
        }
    }
}
