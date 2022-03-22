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

import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.filter.FilterMetaData;
import com.zergclan.wormhole.metadata.core.filter.FilterMetadataFactory;
import com.zergclan.wormhole.metadata.core.initializer.DataNodeMetadataInitializer;
import com.zergclan.wormhole.metadata.core.initializer.DataSourceMetadataInitializer;
import com.zergclan.wormhole.metadata.core.node.DataNodeMetaData;
import com.zergclan.wormhole.metadata.core.resource.ColumnMetaData;
import com.zergclan.wormhole.metadata.core.resource.TableMetaData;
import com.zergclan.wormhole.metadata.core.task.SourceMetaData;
import com.zergclan.wormhole.metadata.core.task.TargetMetaData;
import com.zergclan.wormhole.metadata.core.task.TaskMetaData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
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
     * @return {@link CachedTaskMetaData}
     * @throws SQLException SQL exception
     */
    public static CachedTaskMetaData builder(final TaskMetaData taskMetaData, final Map<String, DataSourceMetaData> dataSources) throws SQLException {
        return new CachedBuilder(taskMetaData, dataSources).build();
    }
    
    @RequiredArgsConstructor
    private static class CachedBuilder {
        
        private final DataNodeMetadataInitializer dataNodeMetadataInitializer = new DataNodeMetadataInitializer();
        
        private final TaskMetaData task;
    
        private final Map<String, DataSourceMetaData> dataSources;
    
        private CachedTaskMetaData build() throws SQLException {
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
    
        private Map<String, DataNodeMetaData> createDefaultTargetDataNodes(final TargetMetaData target) throws SQLException {
            Map<String, DataNodeMetaData> result = new LinkedHashMap<>();
            DataSourceMetaData dataSourceMetaData = dataSources.get(target.getDataSourceIdentifier());
            DataSourceMetadataInitializer.init(dataSourceMetaData);
            TableMetaData targetTable = dataSourceMetaData.getTable(target.getTable());
            Map<String, ColumnMetaData> defaultColumns = createRelatedTargetDefaultColumn(target, targetTable);
            defaultColumns.forEach((key, value) -> result.put(key, dataNodeMetadataInitializer.init(value)));
            return result;
        }
        
        private Map<String, ColumnMetaData> createRelatedTargetDefaultColumn(final TargetMetaData target, final TableMetaData table) {
            Map<String, ColumnMetaData> result = new LinkedHashMap<>();
            Map<String, ColumnMetaData> targetColumns = table.getColumns();
            Map<String, DataNodeMetaData> initializationDataNodes = target.getDataNodes();
            Collection<String> ignoreNodes = target.getIgnoreNodes();
            for (Map.Entry<String, ColumnMetaData> entry : targetColumns.entrySet()) {
                String columnName = entry.getKey();
                if (!initializationDataNodes.containsKey(columnName) && !ignoreNodes.contains(columnName)) {
                    
                    result.put(columnName, entry.getValue());
                }
            }
            return result;
        }
        
        private Map<String, DataNodeMetaData> createDefaultSourceDataNodes(final Map<String, DataNodeMetaData> defaultTargetDataNodes, final SourceMetaData source) throws SQLException {
            Map<String, DataNodeMetaData> result = new LinkedHashMap<>();
            DataSourceMetaData dataSourceMetaData = dataSources.get(source.getDataSourceIdentifier());
            DataSourceMetadataInitializer.init(dataSourceMetaData);
            Map<String, ColumnMetaData> columns = dataSourceMetaData.getTable(source.getTable()).getColumns();
            for (Map.Entry<String, DataNodeMetaData> entry : defaultTargetDataNodes.entrySet()) {
                String columnName = entry.getKey();
                ColumnMetaData columnMetaData = columns.get(columnName);
                Validator.notNull(columnMetaData, "error: create default source data node failed, columnName: [%s]", columnName);
                result.put(columnName, dataNodeMetadataInitializer.init(columnMetaData));
            }
            return result;
        }
        
        private Collection<FilterMetaData> createDefaultFilters(final Map<String, DataNodeMetaData> defaultTarget, final Map<String, DataNodeMetaData> defaultSource) {
            Collection<FilterMetaData> result = new LinkedList<>();
            for (Map.Entry<String, DataNodeMetaData> entry : defaultTarget.entrySet()) {
                String nodeName = entry.getKey();
                DataNodeMetaData targetDataNode = entry.getValue();
                DataNodeMetaData sourceDataNode = defaultSource.get(nodeName);
                result.addAll(FilterMetadataFactory.getDefaultInstance(task.getIdentifier(), sourceDataNode, targetDataNode));
            }
            return result;
        }
    }
}
