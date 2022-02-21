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

package com.zergclan.wormhole.core.creator;

import com.zergclan.wormhole.core.config.DataNodeConfiguration;
import com.zergclan.wormhole.core.config.SourceConfiguration;
import com.zergclan.wormhole.core.metadata.plan.DataNodeMetadata;
import com.zergclan.wormhole.core.metadata.plan.LinkType;
import com.zergclan.wormhole.core.metadata.plan.SourceMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Metadata creator of {@link SourceMetadata}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SourceMetadataCreator {
    
    /**
     * Create {@link SourceMetadata}.
     *
     * @param configuration {@link SourceConfiguration}
     * @return {@link SourceMetadata}
     */
    public static SourceMetadata create(final SourceConfiguration configuration) {
        String dataSourceIdentifier = configuration.getDataSourceIdentifier();
        String primaryTable = configuration.getPrimaryTable();
        String linkTable = configuration.getLinkTable();
        LinkType linkType = LinkType.valueOf(configuration.getLinkType().toUpperCase());
        String conditionSql = configuration.getConditionSql();
        String actualSql = configuration.getActualSql();
        return new SourceMetadata(dataSourceIdentifier, primaryTable, linkTable, linkType, conditionSql, actualSql, createDataNodes(configuration.getDataNodes()));
    }
    
    private static Map<String, DataNodeMetadata> createDataNodes(final Map<String, DataNodeConfiguration> configurations) {
        Map<String, DataNodeMetadata> result = new LinkedHashMap<>();
        Iterator<Map.Entry<String, DataNodeConfiguration>> iterator = configurations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, DataNodeConfiguration> entry = iterator.next();
            result.put(entry.getKey(), DataNodeMetadataCreator.create(entry.getValue()));
        }
        return result;
    }
}
