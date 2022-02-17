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

import com.zergclan.wormhole.core.config.IndexConfiguration;
import com.zergclan.wormhole.core.metadata.resource.IndexMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Index metadata configuration swapper.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IndexMetadataConfigurationSwapper {
    
    /**
     * Swap to {@link IndexMetadata}.
     *
     * @param indexConfiguration {@link IndexConfiguration}
     * @param dataSourceIdentifier data source identifier
     * @param schemaName schema name
     * @param tableName table name
     * @return {@link IndexMetadata}
     */
    public static IndexMetadata swapToMetadata(final IndexConfiguration indexConfiguration, final String dataSourceIdentifier, final String schemaName, final String tableName) {
        return new IndexMetadata(dataSourceIdentifier, schemaName, tableName, indexConfiguration.getName(), indexConfiguration.isUnique(), indexConfiguration.getColumnNames());
    }
}
