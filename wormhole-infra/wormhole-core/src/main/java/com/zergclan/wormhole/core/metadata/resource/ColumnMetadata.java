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
import com.zergclan.wormhole.core.api.metadata.Metadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Metadata for column.
 */
@RequiredArgsConstructor
@Getter
public final class ColumnMetadata implements Metadata {

    private final String dataSourceIdentifier;

    private final String schema;

    private final String table;

    private final String name;

    private final String dataType;

    private final String defaultValue;

    private final boolean nullable;

    /**
     * Get default value.
     *
     * @return default value
     */
    public Optional<String> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    @Override
    public String getIdentifier() {
        return dataSourceIdentifier + MarkConstant.SPACE + schema + MarkConstant.SPACE + table + MarkConstant.SPACE + name;
    }
}
