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

import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

public final class CachedTableMetadata {
    
    /**
     * Builder for {@link CachedTableMetadata}.
     *
     * @param tableMetadata {@link TableMetadata}
     * @param columnIdentifiers column identifiers
     * @return {@link CachedTableMetadata}
     */
    public static CachedTableMetadata builder(final TableMetadata tableMetadata, final Collection<String> columnIdentifiers) {
        return new CachedTableMetadata.CachedBuilder(tableMetadata, columnIdentifiers).build();
    }
    
    @RequiredArgsConstructor
    private static class CachedBuilder {
        
        private final TableMetadata tableMetadata;
    
        private final Collection<String> columnIdentifiers;
        
        private CachedTableMetadata build() {
            return new CachedTableMetadata();
        }
    }
}
