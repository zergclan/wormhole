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

import com.zergclan.wormhole.common.SequenceGenerator;
import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.api.MetaData;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cached {@link TaskMetaData}.
 */
@RequiredArgsConstructor
@Getter
public final class CachedTaskMetaData implements MetaData {

    private final String identifier;
    
    private final long taskBatch;

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
        return new CachedBuilder(taskMetaData, SequenceGenerator.generateId(), dataSources).build();
    }
    
    @RequiredArgsConstructor
    private static class CachedBuilder {
        
        private final DataNodeMetadataInitializer dataNodeMetadataInitializer = new DataNodeMetadataInitializer();
        
        private final TaskMetaData task;
        
        private final long taskBatch;
    
        private final Map<String, DataSourceMetaData> dataSources;
    
        private CachedTaskMetaData build() throws SQLException {
            TargetMetaData target = task.getTarget();
            SourceMetaData source = task.getSource();
            Map<String, DataNodeMetaData[]> defaultDataNodes = createDefaultDataNodes(target, source);
            Iterator<Map.Entry<String, DataNodeMetaData[]>> iterator = defaultDataNodes.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, DataNodeMetaData[]> entry = iterator.next();
                String dataNodeName = entry.getKey();
                DataNodeMetaData[] targetSourceDataNodes = entry.getValue();
                target.getDataNodes().put(dataNodeName, targetSourceDataNodes[0]);
                source.getDataNodes().put(dataNodeName, targetSourceDataNodes[1]);
                task.getFilters().addAll(FilterMetadataFactory.getDefaultInstance(task.getIdentifier(), targetSourceDataNodes[0], targetSourceDataNodes[1]));
            }
            CachedTargetMetaData cachedTargetMetadata = CachedTargetMetaData.builder(generateIdentifier(), target, dataSources.get(target.getDataSourceIdentifier()));
            CachedSourceMetaData cachedSourceMetadata = CachedSourceMetaData.builder(generateIdentifier(), source, dataSources.get(source.getDataSourceIdentifier()));
            return new CachedTaskMetaData(task.getIdentifier(), SequenceGenerator.generateId(), task.getOrder(), task.getBatchSize(), cachedSourceMetadata, cachedTargetMetadata, task.getFilters());
        }
        
        private Map<String, DataNodeMetaData[]> createDefaultDataNodes(final TargetMetaData target, final SourceMetaData source) throws SQLException {
            TableMetaData targetTable = initTableMetaData(target.getDataSourceIdentifier(), target.getTable());
            TableMetaData sourceTable = initTableMetaData(source.getDataSourceIdentifier(), source.getTable());
            Map<String, DataNodeMetaData[]> result = new LinkedHashMap<>();
            Map<String, DataNodeMetaData> initializationTargetDataNodes = target.getDataNodes();
            Collection<String> ignoreNodes = target.getIgnoreNodes();
            Map<String, ColumnMetaData> columns = targetTable.getColumns();
            Iterator<Map.Entry<String, ColumnMetaData>> iterator = columns.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, ColumnMetaData> entry = iterator.next();
                String columnName = entry.getKey();
                if (!initializationTargetDataNodes.containsKey(columnName) && !ignoreNodes.contains(columnName)) {
                    ColumnMetaData sourceColumn = sourceTable.getColumn(columnName);
                    Validator.notNull(sourceColumn, "error: create default source data node failed, columnName: [%s]", columnName);
                    result.put(columnName, dataNodeMetadataInitializer.initDefaultTargetSourceDataNodes(entry.getValue(), sourceColumn));
                }
            }
            return result;
        }
        
        private TableMetaData initTableMetaData(final String dataSourceIdentifier, final String table) throws SQLException {
            DataSourceMetaData dataSourceMetaData = dataSources.get(dataSourceIdentifier);
            DataSourceMetadataInitializer.init(dataSourceMetaData);
            return dataSourceMetaData.getTable(table);
        }
        
        private String generateIdentifier() {
            return task.getIdentifier() + MarkConstant.SPACE + taskBatch;
        }
    }
}
