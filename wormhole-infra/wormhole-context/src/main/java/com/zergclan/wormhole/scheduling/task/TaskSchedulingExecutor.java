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

package com.zergclan.wormhole.scheduling.task;

import com.zergclan.wormhole.core.concurrent.ExecutorService;
import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.core.metadata.ColumnMetaData;
import com.zergclan.wormhole.extracter.Extractor;
import com.zergclan.wormhole.loader.Loader;
import com.zergclan.wormhole.pipeline.DataNodePipeline;
import com.zergclan.wormhole.pipeline.DefaultDataGroupTask;
import com.zergclan.wormhole.pipeline.data.DefaultDataGroupSwapper;
import com.zergclan.wormhole.scheduling.SchedulingExecutor;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

/**
 * Task implemented of {@link SchedulingExecutor}.
 */
@RequiredArgsConstructor
public class TaskSchedulingExecutor implements SchedulingExecutor {

    private final Long planId;

    private final Long taskId;

    private final Extractor extractor;

    private final Loader loader;

    private final ExecutorService executorService;

    private final Map<String, ColumnMetaData> columns = new LinkedHashMap<>();

    private final Collection<Map<String, Object>> dataMaps = new LinkedList<>();

    private final Map<String, DataNodePipeline<?>> pipelineMatrix = new LinkedHashMap<>();

    private CompletionService<Optional<DataGroup>> completionService;

    /**
     * Execute.
     */
    public void execute() {
        if (initEnvironment()) {
            extract();
            transform();
            load();
        }
    }

    private boolean initEnvironment() {
        columns.putAll(createColumns());
        pipelineMatrix.putAll(createPipelineMatrix());
        return true;
    }

    private boolean extract() {
        dataMaps.addAll(extractor.extractDatum(columns));
        return true;
    }

    private void transform() {
        int size = dataMaps.size();
        completionService = new ExecutorCompletionService<>(executorService, new ArrayBlockingQueue<>(size));
        DataGroup dataGroup;
        for (Map<String, Object> each : dataMaps) {
            dataGroup = DefaultDataGroupSwapper.mapToDataGroup(each);
            DefaultDataGroupTask defaultDataGroupTask = new DefaultDataGroupTask(planId, taskId, dataGroup, pipelineMatrix);
            completionService.submit(defaultDataGroupTask);
        }
    }

    private void load() {
        int size = dataMaps.size();
        for (int i = 0; i < size; i++) {
            Future<Optional<DataGroup>> take;
            try {
                take = completionService.take();
                Optional<DataGroup> dataGroupOptional = take.get();
                if (dataGroupOptional.isPresent()) {
                    Map<String, Object> map = DefaultDataGroupSwapper.dataGroupToMap(dataGroupOptional.get());
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
