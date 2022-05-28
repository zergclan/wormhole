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

package com.zergclan.wormhole.metadata.core.catched;

import com.zergclan.wormhole.common.SequenceGenerator;
import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.api.MetaData;
import com.zergclan.wormhole.metadata.core.plan.PlanMetaData;
import com.zergclan.wormhole.metadata.core.task.TaskMetaData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Cached {@link PlanMetaData}.
 */
@RequiredArgsConstructor
@Getter
public final class CachedPlanMetaData implements MetaData {
    
    private final String planIdentifier;
    
    private final long planBatch;
    
    private final boolean atomic;
    
    private final Collection<Map<String, CachedTaskMetaData>> cachedOrderedTasks;
    
    private final Collection<String> taskIdentifiers;
    
    @Override
    public String getIdentifier() {
        return planIdentifier + MarkConstant.SPACE + planBatch;
    }
    
    /**
     * Builder for {@link CachedPlanMetaData}.
     *
     * @param planMetadata {@link PlanMetaData}
     * @param dataSources data sources {@link DataSourceMetaData}
     * @return {@link CachedPlanMetaData}
     * @throws SQLException SQL exception
     */
    public static CachedPlanMetaData builder(final PlanMetaData planMetadata, final Map<String, DataSourceMetaData> dataSources) throws SQLException {
        return new CachedBuilder(planMetadata, dataSources).build();
    }
    
    /**
     * Task completed.
     *
     * @param taskIdentifier task identifier
     * @return remaining task number
     */
    public synchronized int taskCompleted(final String taskIdentifier) {
        taskIdentifiers.remove(taskIdentifier);
        return taskIdentifiers.size();
    }
    
    @RequiredArgsConstructor
    private static class CachedBuilder {
    
        private final PlanMetaData planMetadata;
        
        private final Map<String, DataSourceMetaData> dataSources;
    
        private final Collection<String> taskIdentifiers = new LinkedList<>();
        
        private CachedPlanMetaData build() throws SQLException {
            Collection<CachedTaskMetaData> cachedTasks = createCachedTasks();
            return new CachedPlanMetaData(planMetadata.getIdentifier(), SequenceGenerator.generateId(), planMetadata.isAtomic(), ordered(cachedTasks), taskIdentifiers);
        }
        
        private Collection<CachedTaskMetaData> createCachedTasks() throws SQLException {
            Collection<CachedTaskMetaData> result = new LinkedList<>();
            Iterator<Map.Entry<String, TaskMetaData>> iterator = planMetadata.getTasks().entrySet().iterator();
            while (iterator.hasNext()) {
                CachedTaskMetaData cachedTaskMetaData = CachedTaskMetaData.builder(iterator.next().getValue(), dataSources);
                result.add(cachedTaskMetaData);
                taskIdentifiers.add(cachedTaskMetaData.getIdentifier());
            }
            return result;
        }
        
        private Collection<Map<String, CachedTaskMetaData>> ordered(final Collection<CachedTaskMetaData> taskList) {
            Map<String, Map<String, CachedTaskMetaData>> result = new TreeMap<>();
            Iterator<CachedTaskMetaData> iterator = taskList.iterator();
            Map<String, CachedTaskMetaData> eachMap;
            while (iterator.hasNext()) {
                CachedTaskMetaData each = iterator.next();
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
        
        private Collection<Map<String, CachedTaskMetaData>> converter(final Map<String, Map<String, CachedTaskMetaData>> map) {
            Collection<Map<String, CachedTaskMetaData>> result = new LinkedList<>();
            Iterator<Map.Entry<String, Map<String, CachedTaskMetaData>>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                result.add(iterator.next().getValue());
            }
            return result;
        }
    }
}
