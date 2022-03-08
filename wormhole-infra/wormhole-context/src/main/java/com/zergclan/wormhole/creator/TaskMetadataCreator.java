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
import com.zergclan.wormhole.core.metadata.node.DataNodeMetadata;
import com.zergclan.wormhole.core.metadata.node.FilterMetadata;
import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import com.zergclan.wormhole.core.metadata.task.LinkType;
import com.zergclan.wormhole.core.metadata.task.LoadType;
import com.zergclan.wormhole.core.metadata.task.SourceMetadata;
import com.zergclan.wormhole.core.metadata.task.TargetMetadata;
import com.zergclan.wormhole.core.metadata.task.TaskMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Metadata creator of {@link TaskMetadata}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskMetadataCreator {

    /**
     * Create {@link TaskMetadata}.
     *
     * @param configuration {@link TaskConfiguration}
     * @param dataSources {@link DataSourceMetadata}
     * @return {@link TaskMetadata}
     */
    public static TaskMetadata create(final TaskConfiguration configuration, final Map<String, DataSourceMetadata> dataSources) {
        String taskIdentifier = configuration.getName();
        TargetMetadata target = createTarget(configuration.getTarget(), dataSources.get(configuration.getSource().getDataSourceName()));
        SourceMetadata source = createSource(configuration.getSource(), dataSources.get(configuration.getSource().getDataSourceName()));
        Collection<FilterMetadata> filters = createFilters();
        return new TaskMetadata(taskIdentifier, configuration.getOrder(), configuration.getBatchSize(), source, target, filters);
    }

    private static TargetMetadata createTarget(final TargetConfiguration configuration, final DataSourceMetadata targetDataSource) {
        Collection<String> tables = configuration.getTables();
        Map<String, DataNodeConfiguration> dataNodeConfigurations = configuration.getDataNodes();
        boolean transaction = configuration.isTransaction();
        LoadType loadType = LoadType.valueOf(configuration.getLoadType().toUpperCase());
        Map<String, DataNodeMetadata> dataNodes = LoadType.SHARDING == loadType ? createShardingTargetNodes(tables, dataNodeConfigurations, targetDataSource)
                : createCloneTargetNodes(tables.iterator().next(), dataNodeConfigurations, targetDataSource);
        return new TargetMetadata(targetDataSource.getIdentifier(), tables, loadType, transaction, dataNodes);
    }

    private static Map<String, DataNodeMetadata> createCloneTargetNodes(final String targetTable, final Map<String, DataNodeConfiguration> dataNodeConfigurations,
                                                                        final DataSourceMetadata targetDataSource) {
        return createTargetNodes(targetDataSource.getTable(targetTable), dataNodeConfigurations);
    }

    private static Map<String, DataNodeMetadata> createTargetNodes(final TableMetadata table, final Map<String, DataNodeConfiguration> dataNodeConfigurations) {
        Map<String, DataNodeMetadata> result = new LinkedHashMap<>();
        Map<String, ColumnMetadata> columns = table.getColumns();
        Iterator<Map.Entry<String, ColumnMetadata>> iterator = columns.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ColumnMetadata> entry = iterator.next();
            String name = entry.getKey();
            result.put(name, null == dataNodeConfigurations.get(name) ? DataNodeMetadataCreator.createDefaultMetadata(entry.getValue())
                    : DataNodeMetadataCreator.create(dataNodeConfigurations.get(name)));
        }
        return result;
    }

    private static Map<String, DataNodeMetadata> createShardingTargetNodes(final Collection<String> tables, final Map<String, DataNodeConfiguration> dataNodeConfigurations,
                                                                           final DataSourceMetadata targetDataSource) {
        Map<String, DataNodeMetadata> result = new LinkedHashMap<>();
        Iterator<String> iterator = tables.iterator();
        while (iterator.hasNext()) {
            result.putAll(createTargetNodes(targetDataSource.getTable(iterator.next()), dataNodeConfigurations));
        }
        return result;
    }

    private static SourceMetadata createSource(final SourceConfiguration configuration, final DataSourceMetadata sourceDataSource) {
        String identifier = sourceDataSource.getIdentifier();
        String primaryTable = configuration.getPrimaryTable();
        String linkTable = configuration.getLinkTable();
        LinkType linkType = LinkType.valueOf(configuration.getLinkType().toUpperCase());
        String conditionSql = configuration.getConditionSql();
        String actualSql = configuration.getActualSql();
        Map<String, DataNodeMetadata> dataNodes = createDataNodes(configuration.getDataNodes());
        return new SourceMetadata(identifier, primaryTable, linkTable, linkType, conditionSql, actualSql, dataNodes);
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

    // TODO create data node mappings
    private static Collection<FilterMetadata> createFilters() {
        Collection<FilterMetadata> result = new LinkedList<>();
        return result;
    }
}
