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
package com.zergclan.wormhole.loader;

import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;

/**
 * The load content for Jdbc
 */
@Data
@RequiredArgsConstructor
public class JdbcLoadContent implements LoadContent {

    Collection<Map<String, Object>> loadData;

    Map<String, TableMetadata> tables;

    /**
     * Register {@link TableMetadata}.
     *
     * @param tableMetadata {@link TableMetadata}
     * @return is registered or not.
     */
    public boolean registerTable(final TableMetadata tableMetadata) {
        tables.put(tableMetadata.getIdentifier(), tableMetadata);
        return true;
    }
}
