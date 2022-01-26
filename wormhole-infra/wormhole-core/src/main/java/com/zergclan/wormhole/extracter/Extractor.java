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

package com.zergclan.wormhole.extracter;

import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.core.metadata.resource.IndexMetadata;
import com.zergclan.wormhole.core.metadata.resource.SchemaMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;

import java.util.Collection;
import java.util.Map;

/**
 * The root interface from which all extractor shall be derived in Wormhole.
 */
public interface Extractor {

    /**
     * Extract {@link TableMetadata}.
     *
     * @param schemaMetaData {@link SchemaMetadata}
     * @return {@link TableMetadata}
     */
    Collection<TableMetadata> extractTables(SchemaMetadata schemaMetaData);

    /**
     * Extract {@link ColumnMetadata} of {@link TableMetadata}.
     *
     * @param tableMetaData {@link TableMetadata}
     * @return {@link ColumnMetadata}
     */
    Collection<ColumnMetadata> extractColumns(TableMetadata tableMetaData);

    /**
     * Extract {@link IndexMetadata} of {@link TableMetadata}.
     *
     * @param table {@link TableMetadata}
     * @return {@link IndexMetadata}
     */
    Collection<IndexMetadata> extractIndexes(TableMetadata table);

    /**
     * Extract datum.
     *
     * @param columns {@link ColumnMetadata}
     * @return datum
     */
    Collection<Map<String, Object>> extractData(Map<String, ColumnMetadata> columns);
}
