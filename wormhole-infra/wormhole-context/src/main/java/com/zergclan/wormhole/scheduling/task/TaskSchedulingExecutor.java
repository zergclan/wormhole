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
import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.extracter.Extractor;
import com.zergclan.wormhole.loader.Loader;
import com.zergclan.wormhole.pipeline.DataNodePipeline;
import com.zergclan.wormhole.pipeline.DataNodePipelineFactory;
import com.zergclan.wormhole.pipeline.DefaultDataGroupTask;
import com.zergclan.wormhole.pipeline.data.DefaultDataGroup;
import com.zergclan.wormhole.pipeline.data.DefaultDataGroupSwapper;
import com.zergclan.wormhole.scheduling.SchedulingExecutor;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

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

    private final Map<String, ColumnMetadata> columns = new LinkedHashMap<>();

    private final Collection<Map<String, Object>> dataMaps = new LinkedList<>();

    private final Map<String, DataNodePipeline<?>> pipelineMatrix = new LinkedHashMap<>();

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

    private void extract() {
        dataMaps.addAll(extractor.extractDatum(columns));
    }

    private void transform() {
        DataGroup dataGroup;
        for (Map<String, Object> each : dataMaps) {
            dataGroup = DefaultDataGroupSwapper.mapToDataGroup(each);
            DefaultDataGroupTask defaultDataGroupTask = new DefaultDataGroupTask(planId, taskId, dataGroup, pipelineMatrix);
            executorService.submit(defaultDataGroupTask);
        }
    }

    private void load() {
        loader.loaderData(DefaultDataGroupSwapper.dataGroupToMap(new DefaultDataGroup()));
    }

    private Map<String, DataNodePipeline<?>> createPipelineMatrix() {
        Map<String, DataNodePipeline<?>> result = new LinkedHashMap<>();
        result.put("id", DataNodePipelineFactory.createDataNodePipeline("INT:NOT:NULL#INT:NOT:NULL"));
        result.put("transInt", DataNodePipelineFactory.createDataNodePipeline("INT:NOT:NULL#INT:DEFAULT:1"));
        result.put("transBigint", DataNodePipelineFactory.createDataNodePipeline("BIGINT:NOT:NULL#BIGINT:DEFAULT:2"));
        result.put("transVarchar", DataNodePipelineFactory.createDataNodePipeline("VARCHAR:NOT:NULL#VARCHAR:NOT:NULL"));
        result.put("transDecimal", DataNodePipelineFactory.createDataNodePipeline("DECIMAL:NOT:NULL#DECIMAL:NOT:NULL"));
        result.put("transDatetime", DataNodePipelineFactory.createDataNodePipeline("DATETIME:NOT:NULL#DATETIME:NOT:NULL"));
        result.put("createTime", DataNodePipelineFactory.createDataNodePipeline("DATETIME:NOT:NULL#DATETIME:NOT:NULL"));
        result.put("modifyTime", DataNodePipelineFactory.createDataNodePipeline("DATETIME:NOT:NULL#DATETIME:NOT:NULL"));
        return result;
    }

    private Map<String, ColumnMetadata> createColumns() {
        Map<String, ColumnMetadata> result = new LinkedHashMap<>();
        result.put("id", createColumn("id", "INT(11)"));
        result.put("transInt", createColumn("trans_int", "INT(11)"));
        result.put("transBigint", createColumn("trans_bigint", "BIGINT(20)"));
        result.put("transVarchar", createColumn("trans_varchar", "VARCHAR(32)"));
        result.put("transDecimal", createColumn("trans_decimal", "DECIMAL(18,2)"));
        result.put("transDatetime", createColumn("trans_datetime", "DATETIME(0)"));
        result.put("createTime", createColumn("create_time", "DATETIME(0)"));
        result.put("modifyTime", createColumn("create_time", "DATETIME(0)"));
        return result;
    }

    private ColumnMetadata createColumn(final String name, final String dataType) {
        String databaseIdentifier = "MySQL#127.0.0.1:3306";
        String schema = "source_db";
        String table = "source_table";
        return new ColumnMetadata(databaseIdentifier, schema, table, name, dataType, false);
    }
}
