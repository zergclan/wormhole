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

package com.zergclan.wormhole.pipeline;

import com.zergclan.wormhole.core.concurrent.PromisedTask;
import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.core.data.DataNode;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public final class DefaultDataGroupTask implements PromisedTask<Optional<DataGroup>> {

    private final Long planId;

    private final Long taskId;

    private final DataGroup dataGroup;
    
    private final Map<String, DataNodePipeline<?>> pipelineMatrix;

    @Override
    public Optional<DataGroup> call() throws Exception {
        Optional<Map<String, DataNode<?>>> dataNodesOptional = dataGroup.getDataNodes();
        if (dataNodesOptional.isPresent()) {
            handle(dataNodesOptional.get());
            return Optional.of(dataGroup);
        }
        return Optional.empty();
    }
    
    private void handle(final Map<String, DataNode<?>> dataNodeMap) {
        for (Map.Entry<String, DataNode<?>> entry : dataNodeMap.entrySet()) {
            String column = entry.getKey();
            DataNode<?> dataNode = entry.getValue();
            Optional<DataNodePipeline<?>> pipelineOptional = getPipeline(column);
            if (pipelineOptional.isPresent()) {
                DataNodePipeline dataNodePipeline = pipelineOptional.get();
                dataNodePipeline.handle(dataNode);
            }
            dataNodeMap.put(column, dataNode);
        }
    }
    
    private Optional<DataNodePipeline<?>> getPipeline(final String column) {
        DataNodePipeline<?> result = pipelineMatrix.get(column);
        return null == result ? Optional.empty() : Optional.of(result);
    }
}
