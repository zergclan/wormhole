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

import com.zergclan.wormhole.common.util.CollectionUtil;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.core.config.DataNodeMappingConfiguration;
import com.zergclan.wormhole.core.config.FilterConfiguration;
import com.zergclan.wormhole.core.metadata.filter.FilterMetadata;
import com.zergclan.wormhole.core.metadata.filter.FilterMetadataFactory;
import com.zergclan.wormhole.core.metadata.task.SourceMetadata;
import com.zergclan.wormhole.core.metadata.task.TargetMetadata;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * Metadata creator of {@link FilterMetadata}.
 */
public final class FilterMetadataCreator {

    /**
     * Create {@link FilterMetadata}.
     *
     * @param taskIdentifier task identifier
     * @param dataNodeMappings {@link DataNodeMappingConfiguration}
     * @param target {@link TargetMetadata}
     * @param source {@link SourceMetadata}
     * @return {@link FilterMetadata}
     */
    public static Collection<FilterMetadata> create(final String taskIdentifier, final Collection<DataNodeMappingConfiguration> dataNodeMappings, final TargetMetadata target,
                                                    final SourceMetadata source) {
        Collection<FilterMetadata> result = new LinkedList<>();
        Set<String> targetNodes = target.getDataNodes().keySet();
        Set<String> sourceNodes = source.getDataNodes().keySet();
        Iterator<DataNodeMappingConfiguration> iterator = dataNodeMappings.iterator();
        while (iterator.hasNext()) {
            result.addAll(createFilters(taskIdentifier, iterator.next(), targetNodes, sourceNodes));
        }
        Validator.preState(CollectionUtil.compare(targetNodes, sourceNodes), "error : create filter metadata failed please check data node mapping configuration");
        result.addAll(createFilters(taskIdentifier, target, source, targetNodes, sourceNodes));
        return result;
    }

    private static Collection<FilterMetadata> createFilters(final String taskIdentifier, final TargetMetadata target, final SourceMetadata source, final Set<String> targetNodes,
                                                            final Set<String> sourceNodes) {
        Collection<FilterMetadata> result = new LinkedList<>();
        return result;
    }

    private static Collection<FilterMetadata> createFilters(final String taskIdentifier, final DataNodeMappingConfiguration dataNodeMappingConfiguration, final Set<String> targetNodes,
                                                            final Set<String> sourceNodes) {
        Collection<String> targetNames = dataNodeMappingConfiguration.getTargetNames();
        Collection<String> sourceNames = dataNodeMappingConfiguration.getSourceNames();
        int targetSize = targetNames.size();
        int sourceSize = sourceNames.size();
        if (1 == targetSize && 1 == sourceSize) {
            targetNodes.remove(targetNames.iterator().next());
            sourceNodes.remove(sourceNames.iterator().next());
            return createPreciseFilters(taskIdentifier, dataNodeMappingConfiguration.getFilters());
        } else if (1 == targetSize) {
            targetNodes.remove(targetNames.iterator().next());
            sourceNames.forEach(sourceNodes::remove);
            return createMergedFilters(taskIdentifier, dataNodeMappingConfiguration.getFilters());
        } else if (1 == sourceSize) {
            sourceNodes.remove(sourceNames.iterator().next());
            targetNodes.forEach(targetNames::remove);
            return createSplittedFilters(taskIdentifier, dataNodeMappingConfiguration.getFilters());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static Collection<FilterMetadata> createPreciseFilters(final String taskIdentifier, final Collection<FilterConfiguration> filterConfigurations) {
        Collection<FilterMetadata> result = new LinkedList<>();
        for (FilterConfiguration each : filterConfigurations) {
            // TODO validator precise type
            result.add(FilterMetadataFactory.getPreciseInstance(taskIdentifier, each));
        }
        return result;
    }

    // TODO create merged filters
    private static Collection<FilterMetadata> createMergedFilters(final String taskIdentifier, final Collection<FilterConfiguration> filterConfigurations) {
        Collection<FilterMetadata> result = new LinkedList<>();
        return result;
    }

    // TODO create splitted filters
    private static Collection<FilterMetadata> createSplittedFilters(final String taskIdentifier, final Collection<FilterConfiguration> filterConfigurations) {
        Collection<FilterMetadata> result = new LinkedList<>();
        return result;
    }
}
