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

package com.zergclan.wormhole.metadata.core.resource;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.metadata.api.MetaData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Table metadata.
 */
@RequiredArgsConstructor
@Getter
public final class TableMetaData implements MetaData {

    private final String dataSourceIdentifier;

    private final String schema;

    private final String name;
    
    private final Map<String, ColumnMetaData> columns = new LinkedHashMap<>();

    private final Map<String, IndexMetaData> indexes = new LinkedHashMap<>();

    /**
     * Register {@link ColumnMetaData}.
     *
     * @param columnMetadata {@link ColumnMetaData}
     * @return is registered or not.
     */
    public boolean registerColumn(final ColumnMetaData columnMetadata) {
        columns.put(columnMetadata.getIdentifier(), columnMetadata);
        return true;
    }

    /**
     * Register.
     * @param key  {@link String}
     * @param columnMetadata {@link ColumnMetaData}
     * @return is registered or not.
     */
    public boolean registerColumn(final String key, final ColumnMetaData columnMetadata) {
        columns.put(key, columnMetadata);
        return true;
    }

    /**
     * Register {@link IndexMetaData}.
     *
     * @param indexMetadata {@link IndexMetaData}
     * @return is registered or not.
     */
    public boolean registerIndex(final IndexMetaData indexMetadata) {
        indexes.put(indexMetadata.getIdentifier(), indexMetadata);
        return true;
    }

    @Override
    public String getIdentifier() {
        return dataSourceIdentifier + MarkConstant.SPACE + schema + MarkConstant.SPACE + name;
    }
}
