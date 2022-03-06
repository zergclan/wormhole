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

import com.zergclan.wormhole.common.concurrent.ExecutorService;
import com.zergclan.wormhole.core.api.Pipeline;
import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.extracter.Extractor;
import com.zergclan.wormhole.loader.Loader;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Task implemented of {@link SchedulingExecutor}.
 */
@RequiredArgsConstructor
public final class TaskSchedulingExecutor implements SchedulingExecutor {

    private final Long planId;

    private final Long taskId;

    private final Extractor extractor;

    private final Loader loader;

    private final ExecutorService executorService;

    private final Map<String, ColumnMetadata> columns = new LinkedHashMap<>();

    private final Collection<Map<String, Object>> dataMaps = new LinkedList<>();

    private final Map<String, Pipeline<?>> pipelineMatrix = new LinkedHashMap<>();

    /**
     * Execute.
     */
    public void execute() {
        if (initEnvironment()) {
            extract();
            transform();
        }
    }

    private boolean initEnvironment() {
        columns.putAll(createColumns());
        pipelineMatrix.putAll(createPipelineMatrix());
        return true;
    }

    private void extract() {
        dataMaps.addAll(extractor.extractData(columns));
    }

    private void transform() {
    }
    
    private Map<String, Pipeline<?>> createPipelineMatrix() {
        Map<String, Pipeline<?>> result = new LinkedHashMap<>();
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
        return new ColumnMetadata(databaseIdentifier, schema, table, name, dataType, "", false);
    }
}
