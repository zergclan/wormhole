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

package com.zergclan.wormhole.core.concurrent;

import com.zergclan.wormhole.core.metadata.DataGroup;
import com.zergclan.wormhole.core.metadata.DataNode;
import com.zergclan.wormhole.core.metadata.data.DefaultDataGroup;
import com.zergclan.wormhole.core.metadata.data.StringDataNode;
import com.zergclan.wormhole.pipeline.DataNodeFilter;
import com.zergclan.wormhole.pipeline.DataNodePipeline;
import com.zergclan.wormhole.pipeline.DefaultDataGroupTask;
import com.zergclan.wormhole.pipeline.StringDataNodePipeline;
import com.zergclan.wormhole.pipeline.filter.NullToEmptyHandler;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ExecutorServiceTest {
    
    @Test
    public void assertSubmit() throws ExecutionException, InterruptedException {
        ExecutorService executorService = ExecutorServiceFactory.newSingleThreadExecutor("junit-test", 128, 10 * 1000, FutureTask::new);
        DataGroup dataGroup = createDataGroup();
        Map<String, DataNodePipeline<?>> pipelineMatrix = createPipelineMatrix();
        PromisedTask<Optional<DataGroup>> task = new DefaultDataGroupTask(dataGroup, pipelineMatrix);
        Future<Optional<DataGroup>> submit = executorService.submit(task);
        Optional<DataGroup> dataGroupOptional = submit.get();
        assertTrue(dataGroupOptional.isPresent());
        DataGroup actualDataGroup = dataGroupOptional.get();
        assertEquals(1L, actualDataGroup.getPlanId());
        assertEquals(2L, actualDataGroup.getTaskId());
        Optional<Map<String, DataNode<?>>> dataNodesOptional = actualDataGroup.getDataNodes();
        assertTrue(dataNodesOptional.isPresent());
        Map<String, DataNode<?>> dataNodeMap = dataNodesOptional.get();
        assertEquals("hello jack", dataNodeMap.get("name").getValue());
    }
    
    private Map<String, DataNodePipeline<?>> createPipelineMatrix() {
        Map<String, DataNodePipeline<?>> result = new LinkedHashMap<>();
        result.put("name", createNamePipeline());
        return result;
    }
    
    private DataNodePipeline<String> createNamePipeline() {
        DataNodePipeline<String> result = new StringDataNodePipeline();
        DataNodeFilter<String> nullToEmptyHandler = new NullToEmptyHandler();
        result.append(nullToEmptyHandler);
        DataNodeFilter<String> appendHandler = node -> node.refresh("hello " + node.getValue());
        result.append(appendHandler);
        return result;
    }
    
    private DataGroup createDataGroup() {
        DataGroup result = new DefaultDataGroup(1L, 2L);
        result.init(createDataNodes());
        return result;
    }
    
    private Map<String, DataNode<?>> createDataNodes() {
        Map<String, DataNode<?>> result = new LinkedHashMap<>();
        result.put("name", new StringDataNode("name").refresh("jack"));
        return result;
    }
}
