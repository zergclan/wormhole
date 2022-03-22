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

package com.zergclan.wormhole.metadata.core.catched;

import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.filter.FilterMetaData;
import com.zergclan.wormhole.metadata.core.node.DataNodeMetaData;
import com.zergclan.wormhole.metadata.core.task.SourceMetaData;
import com.zergclan.wormhole.metadata.core.task.TargetMetaData;
import com.zergclan.wormhole.metadata.core.task.TaskMetaData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Cached {@link TaskMetaData}.
 */
@RequiredArgsConstructor
@Getter
public final class CachedTaskMetaData {

    private final String identifier;

    private final int order;

    private final int batchSize;

    private final CachedSourceMetaData source;

    private final CachedTargetMetaData target;

    private final Collection<FilterMetaData> filters;
    
    /**
     * Builder for {@link CachedTaskMetaData}.
     *
     * @param taskMetaData {@link TaskMetaData}
     * @param dataSources data sources {@link DataSourceMetaData}
     *
     * @return {@link CachedTaskMetaData}
     */
    public static CachedTaskMetaData builder(final TaskMetaData taskMetaData, final Map<String, DataSourceMetaData> dataSources) {
        return new CachedBuilder(taskMetaData, dataSources).build();
    }
    
    @RequiredArgsConstructor
    private static class CachedBuilder {
        
        private final TaskMetaData task;
    
        private final Map<String, DataSourceMetaData> dataSources;
    
        private CachedTaskMetaData build() {
            TargetMetaData target = task.getTarget();
            Map<String, DataNodeMetaData> defaultTargetDataNodes = createDefaultTargetDataNodes(target);
            SourceMetaData source = task.getSource();
            Map<String, DataNodeMetaData> defaultSourceDataNodes = createDefaultSourceDataNodes(defaultTargetDataNodes, source);
            Collection<FilterMetaData> defaultFilters = createDefaultFilters(defaultTargetDataNodes, defaultSourceDataNodes);
            target.getDataNodes().putAll(defaultTargetDataNodes);
            source.getDataNodes().putAll(defaultSourceDataNodes);
            CachedTargetMetaData cachedTargetMetadata = CachedTargetMetaData.builder(target, dataSources.get(target.getDataSourceIdentifier()));
            CachedSourceMetaData cachedSourceMetadata = CachedSourceMetaData.builder(source, dataSources.get(source.getDataSourceIdentifier()));
            task.getFilters().addAll(defaultFilters);
            return new CachedTaskMetaData(task.getIdentifier(), task.getOrder(), task.getBatchSize(), cachedSourceMetadata, cachedTargetMetadata, task.getFilters());
        }
    
        private Map<String, DataNodeMetaData> createDefaultTargetDataNodes(final TargetMetaData target) {
            Map<String, DataNodeMetaData> result = new LinkedHashMap<>();
            // TODO create default target data nodes
            return result;
        }
    
        private Map<String, DataNodeMetaData> createDefaultSourceDataNodes(final Map<String, DataNodeMetaData> defaultTargetDataNodes, final SourceMetaData source) {
            Map<String, DataNodeMetaData> result = new LinkedHashMap<>();
            // TODO create default source data nodes
            return result;
        }
    
        private Collection<FilterMetaData> createDefaultFilters(final Map<String, DataNodeMetaData> defaultTarget, final Map<String, DataNodeMetaData> defaultSource) {
            Collection<FilterMetaData> result = new LinkedList<>();
            // TODO create filter by default data node meta data
            return result;
        }
    }
}
