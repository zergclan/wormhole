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

package com.zergclan.wormhole.common.metadata.initializer;

import com.zergclan.wormhole.common.configuration.DataNodeMappingConfiguration;
import com.zergclan.wormhole.common.configuration.FilterConfiguration;
import com.zergclan.wormhole.common.metadata.plan.SourceMetaData;
import com.zergclan.wormhole.common.metadata.plan.TargetMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterMetadataFactory;
import com.zergclan.wormhole.tool.util.Validator;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
        Validator.preState(checkDataNodeMapped(dataNodeMappings, target, source), "error : create filter metadata failed please check data node mapping configuration");
        Collection<FilterMetaData> result = new LinkedList<>();
        dataNodeMappings.forEach(each -> result.addAll(createConfiguredFilters(taskIdentifier, each)));
        return result;
    }
    
    private boolean checkDataNodeMapped(final Collection<DataNodeMappingConfiguration> dataNodeMappings, final TargetMetaData target, final SourceMetaData source) {
        Set<String> sourceMappedNodes = new LinkedHashSet<>();
        Set<String> targetMappedNodes = new LinkedHashSet<>();
        dataNodeMappings.forEach(each -> {
            sourceMappedNodes.addAll(each.getSourceNames());
            targetMappedNodes.addAll(each.getTargetNames());
        });
        return sourceMappedNodes.equals(source.getDataNodes().keySet()) && targetMappedNodes.equals(target.getDataNodes().keySet());
    }

    private Collection<FilterMetaData> createConfiguredFilters(final String taskIdentifier, final DataNodeMappingConfiguration dataNodeMappingConfiguration) {
        int targetSize = dataNodeMappingConfiguration.getTargetNames().size();
        int sourceSize = dataNodeMappingConfiguration.getSourceNames().size();
        if (1 == targetSize && 1 == sourceSize) {
            return createPreciseFilters(taskIdentifier, dataNodeMappingConfiguration.getFilters());
        } else if (1 == targetSize) {
            return createMergedFilters(taskIdentifier, dataNodeMappingConfiguration.getFilters());
        } else if (1 == sourceSize) {
            return createSplittedFilters(taskIdentifier, dataNodeMappingConfiguration.getFilters());
        } else {
            throw new UnsupportedOperationException();
        }
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
