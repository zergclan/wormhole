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

package com.zergclan.wormhole.context;

import com.zergclan.wormhole.common.WormholeException;
import com.zergclan.wormhole.pipeline.DataNodePipeline;
import com.zergclan.wormhole.pipeline.DataNodePipelineFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Context of {@link DataNodePipeline}.
 */
public final class PipelineContext {

    // FIXME generator by config
    private static final Collection<String> CODES;

    static {
        CODES = new LinkedList<>();
        CODES.add("INT:DEFAULT:NULL#INT:NOT:NULL");
        CODES.add("BIGINT:DEFAULT:NULL#BIGINT:NOT:NULL");
        CODES.add("VARCHAR:DEFAULT:NULL#VARCHAR:NOT:NULL");
        CODES.add("DECIMAL:DEFAULT:NULL#DECIMAL:DEFAULT:0.00");
        CODES.add("DATETIME:DEFAULT:NULL#DATETIME:NOT:NULL");
    }

    private final Map<String, DataNodePipeline<?>> dataNodePipelines = new ConcurrentHashMap<>();

    /**
     * Init.
     */
    public void init() {
        for (String each : CODES) {
            dataNodePipelines.put(each, DataNodePipelineFactory.createDataNodePipeline(each));
        }
    }

    /**
     * Get {@link DataNodePipeline}.
     *
     * @param pipelineCode pipeline code
     * @return {@link DataNodePipeline}
     */
    public DataNodePipeline<?> getDataNodePipeline(final String pipelineCode) {
        DataNodePipeline<?> dataNodePipeline = dataNodePipelines.get(pipelineCode);
        if (null == dataNodePipeline) {
            throw new WormholeException("error : data node pipeline not find coded by [%d]", pipelineCode);
        }
        return dataNodePipeline;
    }
}
