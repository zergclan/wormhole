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

package com.zergclan.wormhole.core.creator;

import com.zergclan.wormhole.core.config.FilterConfiguration;
import com.zergclan.wormhole.core.config.TaskConfiguration;
import com.zergclan.wormhole.core.metadata.plan.FilterMetadata;
import com.zergclan.wormhole.core.metadata.plan.SourceMetadata;
import com.zergclan.wormhole.core.metadata.plan.TargetMetadata;
import com.zergclan.wormhole.core.metadata.plan.TaskMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Metadata creator of {@link TaskMetadata}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskMetadataCreator {

    /**
     * Create {@link TaskMetadata}.
     *
     * @param configuration {@link TaskConfiguration}
     * @return {@link TaskMetadata}
     */
    public static TaskMetadata create(final TaskConfiguration configuration) {
        SourceMetadata source = SourceMetadataCreator.create(configuration.getSource());
        TargetMetadata target = TargetMetadataCreator.create(configuration.getTarget());
        Collection<FilterMetadata> filters = createFilters(configuration.getFilters());
        return new TaskMetadata(configuration.getName(), configuration.getOrder(), configuration.getBatchSize(), source, target, filters);
    }
    
    private static Collection<FilterMetadata> createFilters(final Collection<FilterConfiguration> configurations) {
        // FIXME create filters by data nodes
        return new LinkedList<>();
    }
}
