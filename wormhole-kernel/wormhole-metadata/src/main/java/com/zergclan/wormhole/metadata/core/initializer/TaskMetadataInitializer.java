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

package com.zergclan.wormhole.metadata.core.initializer;

import com.zergclan.wormhole.config.core.DataNodeConfiguration;
import com.zergclan.wormhole.config.core.SourceConfiguration;
import com.zergclan.wormhole.config.core.TargetConfiguration;
import com.zergclan.wormhole.config.core.TaskConfiguration;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.filter.FilterMetaData;
import com.zergclan.wormhole.metadata.core.node.DataNodeMetaData;
import com.zergclan.wormhole.metadata.core.resource.ColumnMetaData;
import com.zergclan.wormhole.metadata.core.resource.TableMetaData;
import com.zergclan.wormhole.metadata.core.task.SourceMetaData;
import com.zergclan.wormhole.metadata.core.task.TargetMetaData;
import com.zergclan.wormhole.metadata.core.task.TaskMetaData;

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
     * Init {@link TaskMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param configuration {@link TaskConfiguration}
     * @param dataSources {@link DataSourceMetaData}
     * @return {@link TaskMetaData}
     */
    public TaskMetaData init(final String taskIdentifier, final TaskConfiguration configuration, final Map<String, DataSourceMetaData> dataSources) {
        TargetMetaData target = createTarget(configuration.getTarget(), dataSources.get(configuration.getSource().getDataSource()));
        SourceMetaData source = createSource(configuration.getSource(), dataSources.get(configuration.getSource().getDataSource()));
        Collection<FilterMetaData> filters = filterMetadataInitializer.init(taskIdentifier, configuration.getDataNodeMappings(), target, source);
        return new TaskMetaData(taskIdentifier, configuration.getOrder(), configuration.getBatchSize(), source, target, filters);
    }
    
    private TargetMetaData createTarget(final TargetConfiguration targetConfiguration, final DataSourceMetaData targetDataSource) {
        String table = targetConfiguration.getTable();
        boolean transaction = targetConfiguration.isTransaction();
        Collection<String> uniqueNodes = targetConfiguration.getUniqueNodes();
        Collection<String> compareNodes = targetConfiguration.getCompareNodes();
        Map<String, DataNodeMetaData> dataNodes = createSingleTableDataNodes(targetDataSource.getTable(table), targetConfiguration.getDataNodes());
        return new TargetMetaData(targetDataSource.getIdentifier(), table, transaction, uniqueNodes, compareNodes, dataNodes);
    }
    
    private SourceMetaData createSource(final SourceConfiguration sourceConfiguration, final DataSourceMetaData sourceDataSource) {
        String actualSql = sourceConfiguration.getActualSql();
        String table = sourceConfiguration.getTable();
        String conditionSql = sourceConfiguration.getConditionSql();
        Map<String, DataNodeMetaData> dataNodes = createSingleTableDataNodes(sourceDataSource.getTable(table), sourceConfiguration.getDataNodes());
        return new SourceMetaData(sourceDataSource.getIdentifier(), actualSql, table, conditionSql, dataNodes);
    }
    
    private Map<String, DataNodeMetaData> createSingleTableDataNodes(final TableMetaData table, final Map<String, DataNodeConfiguration> dataNodeConfigurations) {
        Map<String, DataNodeMetaData> result = new LinkedHashMap<>();
        Map<String, ColumnMetaData> columns = table.getColumns();
        Iterator<Map.Entry<String, ColumnMetaData>> iterator = columns.entrySet().iterator();
        DataNodeConfiguration dataNodeConfiguration;
        while (iterator.hasNext()) {
            Map.Entry<String, ColumnMetaData> entry = iterator.next();
            String nodeName = entry.getKey();
            dataNodeConfiguration = dataNodeConfigurations.get(nodeName);
            result.put(nodeName, null == dataNodeConfiguration ? dataNodeMetadataInitializer.init(entry.getValue()) : dataNodeMetadataInitializer.init(nodeName, dataNodeConfiguration));
        }
        return result;
    }
}
