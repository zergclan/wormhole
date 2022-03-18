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

package com.zergclan.wormhole.core.metadata.resource;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.core.api.metadata.MetaData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Schema metadata.
 */
@RequiredArgsConstructor
@Getter
public final class SchemaMetaData implements MetaData {

    private final String dataSourceIdentifier;

    private final String name;

    private final Map<String, TableMetaData> tables = new LinkedHashMap<>();

    /**
     * Register {@link TableMetaData}.
     *
     * @param tableMetadata {@link TableMetaData}
     * @return is registered or not.
     */
    public boolean registerTable(final TableMetaData tableMetadata) {
        tables.put(tableMetadata.getIdentifier(), tableMetadata);
        return true;
    }

    /**
     * Get {@link TableMetaData}.
     *
     * @param name name
     * @return {@link TableMetaData}
     */
    public TableMetaData getTable(final String name) {
        return tables.get(name);
    }

    @Override
    public String getIdentifier() {
        return dataSourceIdentifier + MarkConstant.SPACE + name;
    }
}
