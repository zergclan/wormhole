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

import com.zergclan.wormhole.core.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.Metadata;
import com.zergclan.wormhole.core.metadata.plan.PlanMetadata;
import com.zergclan.wormhole.core.metadata.plan.SourceMetadata;
import com.zergclan.wormhole.core.metadata.plan.TargetMetadata;
import com.zergclan.wormhole.core.metadata.plan.TaskMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Cached {@link PlanMetadata}.
 */
@Getter
public final class CachedPlanMetadata implements Metadata {

    private final String identifier;

    private final Collection<Map<String, CachedTaskMetadata>> cachedTasks;
    
    private CachedPlanMetadata(final String identifier, final Collection<Map<String, CachedTaskMetadata>> cachedTasks) {
        this.identifier = identifier;
        this.cachedTasks = cachedTasks;
    }
    
    /**
     * Builder for {@link CachedPlanMetadata}.
     *
     * @param planMetadata data sources
     * @param dataSources plan metadata
     *
     * @return {@link CachedPlanMetadata}
     */
    public static CachedPlanMetadata builder(final PlanMetadata planMetadata, final Map<String, DataSourceMetadata> dataSources) {
        return new CachedBuilder(planMetadata, dataSources).build();
    }
    
    @RequiredArgsConstructor
    private static class CachedBuilder {
    
        private final PlanMetadata planMetadata;
        
        private final Map<String, DataSourceMetadata> dataSources;
        
        CachedPlanMetadata build() {
            Collection<CachedTaskMetadata> taskList = new LinkedList<>();
            Iterator<Map.Entry<String, TaskMetadata>> iterator = planMetadata.getTasks().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, TaskMetadata> next = iterator.next();
                TaskMetadata task = next.getValue();
                SourceMetadata source = task.getSource();
                TargetMetadata target = task.getTarget();
                CachedSourceMetadata cachedSourceMetadata = CachedSourceMetadata.builder(source, dataSources.get(source.getIdentifier()));
                CachedTargetMetadata cachedTargetMetadata = CachedTargetMetadata.builder(target, dataSources.get(target.getIdentifier()));
                taskList.add(new CachedTaskMetadata(task.getCode(), task.getOrder(), task.getBatchSize(), cachedSourceMetadata, cachedTargetMetadata, task.getFilters()));
            }
            return new CachedPlanMetadata(planMetadata.getIdentifier(), ordered(taskList));
        }

        private Collection<Map<String, CachedTaskMetadata>> ordered(final Collection<CachedTaskMetadata> taskList) {
            Map<String, Map<String, CachedTaskMetadata>> result = new TreeMap<>();
            Iterator<CachedTaskMetadata> iterator = taskList.iterator();
            Map<String, CachedTaskMetadata> eachMap;
            while (iterator.hasNext()) {
                CachedTaskMetadata each = iterator.next();
                String key = String.valueOf(each.getOrder());
                if (result.containsKey(key)) {
                    eachMap = result.get(key);
                    eachMap.put(key, each);
                    result.put(key, eachMap);
                }
                eachMap = new LinkedHashMap<>();
                eachMap.put(key, each);
                result.put(key, eachMap);
            }
            return converter(result);
        }

        private Collection<Map<String, CachedTaskMetadata>> converter(final Map<String, Map<String, CachedTaskMetadata>> map) {
            Collection<Map<String, CachedTaskMetadata>> result = new LinkedList<>();
            Iterator<Map.Entry<String, Map<String, CachedTaskMetadata>>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Map<String, CachedTaskMetadata>> next = iterator.next();
                result.add(next.getValue());
            }
            return result;
        }
    }
}
