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

import com.zergclan.wormhole.core.api.metadata.DataSourceMetaData;
import com.zergclan.wormhole.core.metadata.task.TargetMetaData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public final class CachedTargetMetaData {
    
    private final Map<String, CachedTableMetaData> relatedTables;
    
    public CachedTargetMetaData(final Map<String, CachedTableMetaData> tables) {
        relatedTables = tables;
    }
    
    /**
     * Builder for {@link CachedTargetMetaData}.
     *
     * @param target {@link TargetMetaData}
     * @param dataSource {@link DataSourceMetaData}
     * @return {@link CachedTargetMetaData}
     */
    public static CachedTargetMetaData builder(final TargetMetaData target, final DataSourceMetaData dataSource) {
        return new CachedBuilder(target, dataSource).build();
    }
    
    @RequiredArgsConstructor
    private static class CachedBuilder {
        
        private final TargetMetaData target;
        
        private final DataSourceMetaData dataSource;
        
        private CachedTargetMetaData build() {
            // FIXME create CachedTableMetadata
            Map<String, CachedTableMetaData> tables = new LinkedHashMap<>();
            return new CachedTargetMetaData(tables);
        }
    }
}