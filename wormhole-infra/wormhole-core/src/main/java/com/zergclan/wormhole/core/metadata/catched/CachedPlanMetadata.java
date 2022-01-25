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

package com.zergclan.wormhole.core.metadata.catched;

import com.zergclan.wormhole.api.Pipeline;
import com.zergclan.wormhole.core.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.Metadata;
import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import com.zergclan.wormhole.core.metadata.plan.TaskMetadata;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cached {@link PlanMetadata}.
 */
@NoArgsConstructor
public final class CachedPlanMetadata implements Metadata {
    
    private final Map<String, TaskMetadata> tasks = new LinkedHashMap<>();
    
    /**
     * Builder for {@link CachedPlanMetadata}.
     *
     * @param planMetadata data sources
     * @param dataSources plan metadata
     * @param pipelines pipelines
     * @return {@link CachedPlanMetadata}
     */
    public static CachedPlanMetadata builder(final PlanMetadata planMetadata, final Map<String, DataSourceMetadata> dataSources, final Map<String, Pipeline<?>> pipelines) {
        return new CachedBuilder(planMetadata, dataSources, pipelines).build();
    }
    
    @Override
    public String getIdentifier() {
        return "code";
    }
    
    @RequiredArgsConstructor
    private static class CachedBuilder {
    
        private final PlanMetadata planMetadata;
        
        private final Map<String, DataSourceMetadata> dataSources;
    
        private final Map<String, Pipeline<?>> pipelines;
        
        CachedPlanMetadata build() {
            return new CachedPlanMetadata();
        }
    }
}
