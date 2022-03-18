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

package com.zergclan.wormhole.initializer;

import com.zergclan.wormhole.core.api.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.config.DataNodeConfiguration;
import com.zergclan.wormhole.core.config.SourceConfiguration;
import com.zergclan.wormhole.core.config.TargetConfiguration;
import com.zergclan.wormhole.core.config.TaskConfiguration;
import com.zergclan.wormhole.core.metadata.filter.FilterMetadata;
import com.zergclan.wormhole.core.metadata.node.DataNodeMetadata;
import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import com.zergclan.wormhole.core.metadata.task.SourceMetadata;
import com.zergclan.wormhole.core.metadata.task.TargetMetadata;
import com.zergclan.wormhole.core.metadata.task.TaskMetadata;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Task metadata initializer.
 */
public final class TaskMetadataInitializer {
    
    private final DataNodeMetadataInitializer dataNodeMetadataInitializer = new DataNodeMetadataInitializer();
    
    private final FilterMetadataInitializer filterMetadataInitializer = new FilterMetadataInitializer();
    
    /**
     * Init {@link TaskMetadata}.
     *
     * @param taskIdentifier task identifier
     * @param configuration {@link TaskConfiguration}
     * @param dataSources {@link DataSourceMetadata}
     * @return {@link TaskMetadata}
     */
    public TaskMetadata init(final String taskIdentifier, final TaskConfiguration configuration, final Map<String, DataSourceMetadata> dataSources) {
        TargetMetadata target = createTarget(configuration.getTarget(), dataSources.get(configuration.getSource().getDataSource()));
        SourceMetadata source = createSource(configuration.getSource(), dataSources.get(configuration.getSource().getDataSource()));
        Collection<FilterMetadata> filters = filterMetadataInitializer.init(taskIdentifier, configuration.getDataNodeMappings(), target, source);
        return new TaskMetadata(taskIdentifier, configuration.getOrder(), configuration.getBatchSize(), source, target, filters);
    }
    
    private TargetMetadata createTarget(final TargetConfiguration targetConfiguration, final DataSourceMetadata targetDataSource) {
        String table = targetConfiguration.getTable();
        Collection<String> uniqueNodes = targetConfiguration.getUniqueNodes();
        Collection<String> compareNodes = targetConfiguration.getCompareNodes();
        Map<String, DataNodeMetadata> dataNodes = createSingleTableDataNodes(targetDataSource.getTable(table), targetConfiguration.getDataNodes());
        return new TargetMetadata(targetDataSource.getIdentifier(), table, uniqueNodes, compareNodes, dataNodes);
    }
    
    private SourceMetadata createSource(final SourceConfiguration sourceConfiguration, final DataSourceMetadata sourceDataSource) {
        String actualSql = sourceConfiguration.getActualSql();
        String table = sourceConfiguration.getTable();
        String conditionSql = sourceConfiguration.getConditionSql();
        Map<String, DataNodeMetadata> dataNodes = createSingleTableDataNodes(sourceDataSource.getTable(table), sourceConfiguration.getDataNodes());
        return new SourceMetadata(sourceDataSource.getIdentifier(), actualSql, table, conditionSql, dataNodes);
    }
    
    private Map<String, DataNodeMetadata> createSingleTableDataNodes(final TableMetadata table, final Map<String, DataNodeConfiguration> dataNodeConfigurations) {
        Map<String, DataNodeMetadata> result = new LinkedHashMap<>();
        Map<String, ColumnMetadata> columns = table.getColumns();
        Iterator<Map.Entry<String, ColumnMetadata>> iterator = columns.entrySet().iterator();
        DataNodeConfiguration dataNodeConfiguration;
        while (iterator.hasNext()) {
            Map.Entry<String, ColumnMetadata> entry = iterator.next();
            String nodeName = entry.getKey();
            dataNodeConfiguration = dataNodeConfigurations.get(nodeName);
            result.put(nodeName, null == dataNodeConfiguration ? dataNodeMetadataInitializer.init(entry.getValue()) : dataNodeMetadataInitializer.init(nodeName, dataNodeConfiguration));
        }
        return result;
    }
}
