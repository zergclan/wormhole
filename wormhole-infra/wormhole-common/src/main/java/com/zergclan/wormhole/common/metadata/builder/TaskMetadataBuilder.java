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

package com.zergclan.wormhole.common.metadata.builder;

import com.zergclan.wormhole.common.configuration.DataNodeConfiguration;
import com.zergclan.wormhole.common.configuration.SourceConfiguration;
import com.zergclan.wormhole.common.configuration.TargetConfiguration;
import com.zergclan.wormhole.common.configuration.TaskConfiguration;
import com.zergclan.wormhole.common.metadata.datasource.WormholeDataSourceMetaData;
import com.zergclan.wormhole.common.metadata.plan.SourceMetaData;
import com.zergclan.wormhole.common.metadata.plan.TargetMetaData;
import com.zergclan.wormhole.common.metadata.plan.TaskMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterMetaData;
import com.zergclan.wormhole.common.metadata.plan.node.DataNodeMetaData;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Task metadata builder.
 */
public final class TaskMetadataBuilder {
    
    private final DataNodeMetadataBuilder dataNodeMetadataBuilder = new DataNodeMetadataBuilder();
    
    private final FilterMetadataBuilder filterMetadataBuilder = new FilterMetadataBuilder();
    
    /**
     * Build {@link TaskMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param configuration {@link TaskConfiguration}
     * @param dataSources {@link WormholeDataSourceMetaData}
     * @return {@link TaskMetaData}
     */
    public TaskMetaData build(final String taskIdentifier, final TaskConfiguration configuration, final Map<String, WormholeDataSourceMetaData> dataSources) {
        TargetMetaData target = buildTarget(configuration.getTarget(), dataSources.get(configuration.getTarget().getDataSource()));
        SourceMetaData source = buildSource(configuration.getSource(), dataSources.get(configuration.getSource().getDataSource()));
        Collection<FilterMetaData> filters = filterMetadataBuilder.build(taskIdentifier, configuration.getDataNodeMappings(), target, source);
        return new TaskMetaData(taskIdentifier, configuration.getOrder(), configuration.getBatchSize(), source, target, filters);
    }
    
    private TargetMetaData buildTarget(final TargetConfiguration targetConfiguration, final WormholeDataSourceMetaData targetDataSource) {
        String table = targetConfiguration.getTable();
        Map<String, DataNodeMetaData> dataNodes = buildConfiguredDataNodes(targetConfiguration.getDataNodes());
        Collection<String> uniqueNodes = targetConfiguration.getUniqueNodes();
        Collection<String> compareNodes = targetConfiguration.getCompareNodes();
        Collection<String> ignoreNodes = targetConfiguration.getIgnoreNodes();
        String versionNode = targetConfiguration.getVersionNode();
        return new TargetMetaData(targetDataSource.getIdentifier(), table, compareNodes, uniqueNodes, ignoreNodes, versionNode, dataNodes);
    }
    
    private SourceMetaData buildSource(final SourceConfiguration sourceConfiguration, final WormholeDataSourceMetaData sourceDataSource) {
        String actualSql = sourceConfiguration.getActualSql();
        String table = sourceConfiguration.getTable();
        String conditionSql = sourceConfiguration.getConditionSql();
        Map<String, DataNodeMetaData> dataNodes = buildConfiguredDataNodes(sourceConfiguration.getDataNodes());
        return new SourceMetaData(sourceDataSource.getIdentifier(), table, actualSql, conditionSql, dataNodes);
    }
    
    private Map<String, DataNodeMetaData> buildConfiguredDataNodes(final Map<String, DataNodeConfiguration> dataNodeConfigurations) {
        Map<String, DataNodeMetaData> result = new LinkedHashMap<>();
        dataNodeConfigurations.forEach((key, value) -> result.put(key, dataNodeMetadataBuilder.build(key, value)));
        return result;
    }
}
