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

import com.zergclan.wormhole.core.api.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.core.metadata.task.SourceMetadata;
import lombok.RequiredArgsConstructor;

import java.util.Map;

public final class CachedSourceMetadata {
    
    private final String query;
    
    private final Map<String, ColumnMetadata> columnMetadata;
    
    public CachedSourceMetadata(final String query, final Map<String, ColumnMetadata> columnMetadata) {
        this.query = query;
        this.columnMetadata = columnMetadata;
    }
    
    /**
     * Builder for {@link CachedSourceMetadata}.
     *
     * @param source {@link SourceMetadata}
     * @param dataSource {@link DataSourceMetadata}
     * @return {@link CachedSourceMetadata}
     */
    public static CachedSourceMetadata builder(final SourceMetadata source, final DataSourceMetadata dataSource) {
        return new CachedBuilder(source, dataSource).build();
    }
    
    @RequiredArgsConstructor
    private static class CachedBuilder {
        
        private final SourceMetadata source;
        
        private final DataSourceMetadata dataSource;
    
        private CachedSourceMetadata build() {
            String querySql;
            Map<String, ColumnMetadata> columnMetadata;
            return null;
        }
    }
}
