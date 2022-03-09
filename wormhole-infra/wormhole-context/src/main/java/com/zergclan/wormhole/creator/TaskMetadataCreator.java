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

package com.zergclan.wormhole.creator;

import com.zergclan.wormhole.core.config.DataNodeConfiguration;
import com.zergclan.wormhole.core.config.SourceConfiguration;
import com.zergclan.wormhole.core.config.TargetConfiguration;
import com.zergclan.wormhole.core.config.TaskConfiguration;
import com.zergclan.wormhole.core.api.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.filter.FilterMetadata;
import com.zergclan.wormhole.core.metadata.node.DataNodeMetadata;
import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import com.zergclan.wormhole.core.metadata.task.LoadType;
import com.zergclan.wormhole.core.metadata.task.SourceMetadata;
import com.zergclan.wormhole.core.metadata.task.TargetMetadata;
import com.zergclan.wormhole.core.metadata.task.TaskMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Metadata creator of {@link TaskMetadata}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskMetadataCreator {

    /**
     * Create {@link TaskMetadata}.
     *
     * @param taskIdentifier task identifier
     * @param configuration {@link TaskConfiguration}
     * @param dataSources {@link DataSourceMetadata}
     * @return {@link TaskMetadata}
     */
    public static TaskMetadata create(final String taskIdentifier, final TaskConfiguration configuration, final Map<String, DataSourceMetadata> dataSources) {
        TargetMetadata target = createTarget(configuration.getTarget(), dataSources.get(configuration.getSource().getDataSource()));
        SourceMetadata source = createSource(configuration.getSource(), dataSources.get(configuration.getSource().getDataSource()));
        Collection<FilterMetadata> filters = FilterMetadataCreator.create(configuration.getDataNodeMappings(), target, source);
        return new TaskMetadata(taskIdentifier, configuration.getOrder(), configuration.getBatchSize(), source, target, filters);
    }
    
    private static TargetMetadata createTarget(final TargetConfiguration targetConfiguration, final DataSourceMetadata targetDataSource) {
        Collection<String> tables = targetConfiguration.getTables();
        Map<String, DataNodeConfiguration> dataNodeConfigurations = targetConfiguration.getDataNodes();
        boolean transaction = targetConfiguration.isTransaction();
        LoadType loadType = LoadType.valueOf(targetConfiguration.getLoadType().toUpperCase());
        Map<String, DataNodeMetadata> dataNodes = createTargetDataNodes(loadType, tables, dataNodeConfigurations, targetDataSource);
        return new TargetMetadata(targetDataSource.getIdentifier(), tables, loadType, transaction, dataNodes);
    }
    
    private static Map<String, DataNodeMetadata> createTargetDataNodes(final LoadType loadType, final Collection<String> tables, final Map<String, DataNodeConfiguration> dataNodeConfigurations,
                                                                       final DataSourceMetadata targetDataSource) {
        switch (loadType) {
            case SHARDING:
                return createShardingTargetDataNodes(tables, dataNodeConfigurations, targetDataSource);
            case CLONE :
                return createClonedTargetDataNodes(tables.iterator().next(), dataNodeConfigurations, targetDataSource);
            default :
                throw new UnsupportedOperationException();
        }
    }
    
    private static Map<String, DataNodeMetadata> createShardingTargetDataNodes(final Collection<String> tables, final Map<String, DataNodeConfiguration> dataNodeConfigurations,
                                                                               final DataSourceMetadata targetDataSource) {
        Map<String, DataNodeMetadata> result = new LinkedHashMap<>();
        Iterator<String> iterator = tables.iterator();
        while (iterator.hasNext()) {
            result.putAll(createSingleTableDataNodes(targetDataSource.getTable(iterator.next()), dataNodeConfigurations));
        }
        return result;
    }
    
    private static Map<String, DataNodeMetadata> createClonedTargetDataNodes(final String targetTable, final Map<String, DataNodeConfiguration> dataNodeConfigurations,
                                                                            final DataSourceMetadata targetDataSource) {
        return createSingleTableDataNodes(targetDataSource.getTable(targetTable), dataNodeConfigurations);
    }
    
    private static Map<String, DataNodeMetadata> createSingleTableDataNodes(final TableMetadata table, final Map<String, DataNodeConfiguration> dataNodeConfigurations) {
        Map<String, DataNodeMetadata> result = new LinkedHashMap<>();
        Map<String, ColumnMetadata> columns = table.getColumns();
        Iterator<Map.Entry<String, ColumnMetadata>> iterator = columns.entrySet().iterator();
        DataNodeConfiguration dataNodeConfiguration;
        while (iterator.hasNext()) {
            Map.Entry<String, ColumnMetadata> entry = iterator.next();
            String nodeName = entry.getKey();
            dataNodeConfiguration = dataNodeConfigurations.get(nodeName);
            result.put(nodeName, null == dataNodeConfiguration ? DataNodeMetadataCreator.createDefaultMetadata(entry.getValue()) : DataNodeMetadataCreator.create(nodeName, dataNodeConfiguration));
        }
        return result;
    }
    
    private static SourceMetadata createSource(final SourceConfiguration sourceConfiguration, final DataSourceMetadata sourceDataSource) {
        String identifier = sourceDataSource.getIdentifier();
        Collection<String> tables = sourceConfiguration.getTables();
        String conditionSql = sourceConfiguration.getConditionSql();
        String actualSql = sourceConfiguration.getActualSql();
        Map<String, DataNodeMetadata> dataNodes = createSourceDataNodes(tables.iterator().next(), sourceConfiguration.getDataNodes(), sourceDataSource);
        return new SourceMetadata(identifier, tables, conditionSql, actualSql, dataNodes);
    }
    
    private static Map<String, DataNodeMetadata> createSourceDataNodes(final String table, final Map<String, DataNodeConfiguration> dataNodeConfigurations,
                                                                       final DataSourceMetadata sourceDataSource) {
        return createSingleTableDataNodes(sourceDataSource.getTable(table), dataNodeConfigurations);
    }
}
