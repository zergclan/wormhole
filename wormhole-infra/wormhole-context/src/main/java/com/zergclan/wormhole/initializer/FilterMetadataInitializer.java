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

import com.zergclan.wormhole.common.util.CollectionUtil;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.core.config.DataNodeMappingConfiguration;
import com.zergclan.wormhole.core.config.FilterConfiguration;
import com.zergclan.wormhole.core.metadata.filter.FilterMetaData;
import com.zergclan.wormhole.core.metadata.filter.FilterMetadataFactory;
import com.zergclan.wormhole.core.metadata.node.DataNodeMetaData;
import com.zergclan.wormhole.core.metadata.task.SourceMetaData;
import com.zergclan.wormhole.core.metadata.task.TargetMetaData;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Filter metadata initializer.
 */
public final class FilterMetadataInitializer {
    
    /**
     * Init {@link FilterMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param dataNodeMappings {@link DataNodeMappingConfiguration}
     * @param target {@link TargetMetaData}
     * @param source {@link SourceMetaData}
     * @return {@link FilterMetaData}
     */
    public Collection<FilterMetaData> init(final String taskIdentifier, final Collection<DataNodeMappingConfiguration> dataNodeMappings, final TargetMetaData target,
                                           final SourceMetaData source) {
        Collection<FilterMetaData> result = new LinkedList<>();
        Set<String> targetNodes = target.getDataNodes().keySet();
        Set<String> sourceNodes = source.getDataNodes().keySet();
        for (DataNodeMappingConfiguration each : dataNodeMappings) {
            result.addAll(createConfigurationFilters(taskIdentifier, each, targetNodes, sourceNodes));
        }
        Validator.preState(CollectionUtil.compare(targetNodes, sourceNodes), "error : create filter metadata failed please check data node mapping configuration");
        result.addAll(createDefaultFilters(taskIdentifier, target, source, targetNodes));
        return result;
    }
    
    private Collection<FilterMetaData> createConfigurationFilters(final String taskIdentifier, final DataNodeMappingConfiguration dataNodeMappingConfiguration, final Set<String> targetNodes,
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
    
    private Collection<FilterMetaData> createDefaultFilters(final String taskIdentifier, final TargetMetaData target, final SourceMetaData source, final Set<String> nodeNames) {
        Collection<FilterMetaData> result = new LinkedList<>();
        Map<String, DataNodeMetaData> sourceDataNodes = source.getDataNodes();
        Map<String, DataNodeMetaData> targetDataNodes = target.getDataNodes();
        for (String each : nodeNames) {
            DataNodeMetaData sourceDataNodeMetadata = sourceDataNodes.get(each);
            DataNodeMetaData targetDataNodeMetadata = targetDataNodes.get(each);
            result.addAll(FilterMetadataFactory.getDefaultInstance(taskIdentifier, sourceDataNodeMetadata, targetDataNodeMetadata));
        }
        return result;
    }
    
    private Collection<FilterMetaData> createPreciseFilters(final String taskIdentifier, final Collection<FilterConfiguration> filterConfigurations) {
        Collection<FilterMetaData> result = new LinkedList<>();
        for (FilterConfiguration each : filterConfigurations) {
            result.add(FilterMetadataFactory.getPreciseInstance(taskIdentifier, each));
        }
        return result;
    }
    
    private Collection<FilterMetaData> createMergedFilters(final String taskIdentifier, final Collection<FilterConfiguration> filterConfigurations) {
        Collection<FilterMetaData> result = new LinkedList<>();
        for (FilterConfiguration each : filterConfigurations) {
            result.add(FilterMetadataFactory.getInstance(taskIdentifier, each));
        }
        return result;
    }
    
    private Collection<FilterMetaData> createSplittedFilters(final String taskIdentifier, final Collection<FilterConfiguration> filterConfigurations) {
        Collection<FilterMetaData> result = new LinkedList<>();
        for (FilterConfiguration each : filterConfigurations) {
            result.add(FilterMetadataFactory.getInstance(taskIdentifier, each));
        }
        return result;
    }
}
