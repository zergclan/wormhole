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

package com.zergclan.wormhole.scheduling;

import com.zergclan.wormhole.core.concurrent.ExecutorService;
import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.core.metadata.ColumnMetaData;
import com.zergclan.wormhole.extracter.Extractor;
import com.zergclan.wormhole.loader.Loader;
import com.zergclan.wormhole.pipeline.DataNodePipeline;
import com.zergclan.wormhole.pipeline.DefaultDataGroupTask;
import com.zergclan.wormhole.pipeline.data.DefaultDataGroupSwapper;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

/**
 * Default scheduling executor.
 */
@RequiredArgsConstructor
public class DefaultSchedulingExecutor implements SchedulingExecutor {
    
    private final Extractor extractor;
    
    private final Loader loader;
    
    private final ExecutorService executorService;

    private final DefaultDataGroupSwapper defaultDataGroupSwapper;
    
    /**
     * Execute.
     */
    public void execute() {
        // TODO Initial configuration
        Long planId = createPlanId();
        Long taskId = createTaskId();
        Map<String, DataNodePipeline<?>> pipelineMatrix = createPipelineMatrix();
        Map<String, ColumnMetaData> columns = createColumns();
        Collection<Map<String, Object>> maps = extractor.extractDatum(columns);
        final int size = maps.size();
        if (0 == size) {
            return;
        }
        CompletionService<Optional<DataGroup>> taskCompletionService = new ExecutorCompletionService<>(executorService, new ArrayBlockingQueue<>(size));
        DataGroup dataGroup;
        for (Map<String, Object> each : maps) {
            dataGroup = defaultDataGroupSwapper.mapToDataGroup(each);
            DefaultDataGroupTask defaultDataGroupTask = new DefaultDataGroupTask(planId, taskId, dataGroup, pipelineMatrix);
            taskCompletionService.submit(defaultDataGroupTask);
        }
        for (int i = 0; i < size; i++) {
            Future<Optional<DataGroup>> take;
            try {
                take = taskCompletionService.take();
                Optional<DataGroup> dataGroupOptional = take.get();
                if (dataGroupOptional.isPresent()) {
                    Map<String, Object> map = defaultDataGroupSwapper.dataGroupToMap(dataGroupOptional.get());
                    loader.loaderData(map);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, DataNodePipeline<?>> createPipelineMatrix() {
        Map<String, DataNodePipeline<?>> result = new LinkedHashMap<>();
        return result;
    }

    private Long createTaskId() {
        return 2L;
    }

    private Long createPlanId() {
        return 1L;
    }

    private Map<String, ColumnMetaData> createColumns() {
        Map<String, ColumnMetaData> result = new LinkedHashMap<>();
        result.put("id", createColumn("id", "INT(11)"));
        result.put("name", createColumn("name", "VARCHAR(32)"));
        result.put("age", createColumn("age", "INT(11)"));
        result.put("create_time", createColumn("create_time", "datetime(0)"));
        result.put("update_time", createColumn("create_time", "datetime(0)"));
        return result;
    }

    private ColumnMetaData createColumn(final String name, final String dataType) {
        String databaseIdentifier = "MySQL#127.0.0.1:3306";
        String schema = "source_test_ds";
        String table = "test_table";
        boolean nullable = false;
        String comment = "";
        return new ColumnMetaData(databaseIdentifier, schema, table, name, dataType, nullable, comment);
    }
}
