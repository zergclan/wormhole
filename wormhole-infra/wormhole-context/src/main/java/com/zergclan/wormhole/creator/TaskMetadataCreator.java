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
import com.zergclan.wormhole.core.config.FilterConfiguration;
import com.zergclan.wormhole.core.config.SourceConfiguration;
import com.zergclan.wormhole.core.config.TargetConfiguration;
import com.zergclan.wormhole.core.config.TaskConfiguration;
import com.zergclan.wormhole.core.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.plan.DataNodeMetadata;
import com.zergclan.wormhole.core.metadata.plan.FilterMetadata;
import com.zergclan.wormhole.core.metadata.plan.LinkType;
import com.zergclan.wormhole.core.metadata.plan.LoadType;
import com.zergclan.wormhole.core.metadata.plan.SourceMetadata;
import com.zergclan.wormhole.core.metadata.plan.TargetMetadata;
import com.zergclan.wormhole.core.metadata.plan.TaskMetadata;
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
        SourceMetadata source = createSource(configuration.getSource(), dataSources.get(configuration.getSource().getDataSourceName()));
        TargetMetadata target = createTarget(configuration.getTarget(), dataSources.get(configuration.getSource().getDataSourceName()));
        String taskIdentifier = configuration.getName();
        Collection<FilterMetadata> filters = createFilters(taskIdentifier, configuration.getFilters(), source.getDataNodes(), target.getDataNodes());
        return new TaskMetadata(taskIdentifier, configuration.getOrder(), configuration.getBatchSize(), source, target, filters);
    }

    private static SourceMetadata createSource(final SourceConfiguration configuration, final DataSourceMetadata sourceDataSource) {
        String primaryTable = configuration.getPrimaryTable();
        String linkTable = configuration.getLinkTable();
        LinkType linkType = LinkType.valueOf(configuration.getLinkType().toUpperCase());
        String conditionSql = configuration.getConditionSql();
        String actualSql = configuration.getActualSql();
        return new SourceMetadata(sourceDataSource.getIdentifier(), primaryTable, linkTable, linkType, conditionSql, actualSql, createDataNodes(configuration.getDataNodes(), sourceDataSource));
    }

    private static TargetMetadata createTarget(final TargetConfiguration configuration, final DataSourceMetadata targetDataSource) {
        Collection<String> tables = configuration.getTables();
        LoadType loadType = LoadType.valueOf(configuration.getLoadType().toUpperCase());
        return new TargetMetadata(targetDataSource.getIdentifier(), tables, loadType, createDataNodes(configuration.getDataNodes(), targetDataSource));
    }

    // FIXME create data nodes
    private static Map<String, DataNodeMetadata> createDataNodes(final Map<String, DataNodeConfiguration> configurations, final DataSourceMetadata dataSource) {
        Map<String, DataNodeMetadata> result = new LinkedHashMap<>();
        Iterator<Map.Entry<String, DataNodeConfiguration>> iterator = configurations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, DataNodeConfiguration> entry = iterator.next();
            result.put(entry.getKey(), DataNodeMetadataCreator.create(entry.getValue()));
        }
        return result;
    }

    // FIXME create filters
    private static Collection<FilterMetadata> createFilters(final String taskIdentifier, final Collection<FilterConfiguration> configurations, final Map<String, DataNodeMetadata> sourceDataNodes,
                                                            final Map<String, DataNodeMetadata> targetDataNodes) {
        Collection<FilterMetadata> result = new LinkedList<>();
        Iterator<FilterConfiguration> iterator = configurations.iterator();
        while (iterator.hasNext()) {
            FilterConfiguration each = iterator.next();
            result.add(new FilterMetadata(taskIdentifier, each.getOrder(), each.getType(), each.getProperties()));
        }
        return result;
    }
}
