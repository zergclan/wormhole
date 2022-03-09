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
     * @param dataNodeMappings {@link DataNodeMappingConfiguration}
     * @param target {@link TargetMetadata}
     * @param source {@link SourceMetadata}
     * @return {@link FilterMetadata}
     */
    public static Collection<FilterMetadata> create(final Collection<DataNodeMappingConfiguration> dataNodeMappings, final TargetMetadata target, final SourceMetadata source) {
        Collection<FilterMetadata> result = new LinkedList<>();
        Set<String> targetNodes = target.getDataNodes().keySet();
        Set<String> sourceNodes = source.getDataNodes().keySet();
        Iterator<DataNodeMappingConfiguration> iterator = dataNodeMappings.iterator();
        while (iterator.hasNext()) {
            result.addAll(createFilters(iterator.next(), targetNodes, sourceNodes));
        }
        return result;
    }

    private static Collection<FilterMetadata> createFilters(final DataNodeMappingConfiguration dataNodeMappingConfiguration, final Set<String> targetNodes, final Set<String> sourceNodes) {
        Collection<String> targetNames = dataNodeMappingConfiguration.getTargetNames();
        Collection<String> sourceNames = dataNodeMappingConfiguration.getSourceNames();
        int targetSize = targetNames.size();
        int sourceSize = sourceNames.size();
        if (1 == targetSize && 1 == sourceSize) {
            return createPreciseFilters(dataNodeMappingConfiguration.getFilters());
        } else if (1 == targetSize) {
            return createMergedFilters(dataNodeMappingConfiguration.getFilters());
        } else if (1 == sourceSize) {
            return createSplittedFilters(dataNodeMappingConfiguration.getFilters());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static Collection<FilterMetadata> createPreciseFilters(final Collection<FilterConfiguration> filterConfigurations) {
        Collection<FilterMetadata> result = new LinkedList<>();
        for (FilterConfiguration each : filterConfigurations) {
            // TODO validator precise type
            result.add(FilterMetadataFactory.getInstance(each));
        }
        return result;
    }

    // TODO create merged filters
    private static Collection<FilterMetadata> createMergedFilters(final Collection<FilterConfiguration> filterConfigurations) {
        Collection<FilterMetadata> result = new LinkedList<>();
        return result;
    }

    // TODO create splitted filters
    private static Collection<FilterMetadata> createSplittedFilters(final Collection<FilterConfiguration> filterConfigurations) {
        Collection<FilterMetadata> result = new LinkedList<>();
        return result;
    }
}
