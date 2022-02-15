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

package com.zergclan.wormhole.core.metadata.catched;

import com.zergclan.wormhole.core.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.plan.TargetMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public final class CachedTargetMetadata {
    
    private final Map<String, CachedTableMetadata> relatedTables;
    
    public CachedTargetMetadata(final Map<String, CachedTableMetadata> tables) {
        relatedTables = tables;
    }
    
    /**
     * Builder for {@link CachedTargetMetadata}.
     *
     * @param target {@link TargetMetadata}
     * @param dataSource {@link DataSourceMetadata}
     * @return {@link CachedTargetMetadata}
     */
    public static CachedTargetMetadata builder(final TargetMetadata target, final DataSourceMetadata dataSource) {
        return new CachedBuilder(target, dataSource).build();
    }
    
    @RequiredArgsConstructor
    private static class CachedBuilder {
        
        private final TargetMetadata target;
        
        private final DataSourceMetadata dataSource;
        
        private CachedTargetMetadata build() {
            Map<String, TableMetadata> sourceTables = dataSource.getSchema(target.getSchema()).getTables();
            Iterator<Map.Entry<String, Collection<String>>> iterator = target.getRelatedTables().entrySet().iterator();
            Map<String, CachedTableMetadata> tables = new LinkedHashMap<>();
            while (iterator.hasNext()) {
                Map.Entry<String, Collection<String>> entry = iterator.next();
                String tableIdentifier = entry.getKey();
                TableMetadata tableMetadata = sourceTables.get(tableIdentifier);
                tables.put(tableIdentifier, CachedTableMetadata.builder(tableMetadata, entry.getValue()));
            }
            return new CachedTargetMetadata(tables);
        }
    }
}
