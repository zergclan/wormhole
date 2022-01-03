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

import com.zergclan.wormhole.common.StringUtil;
import com.zergclan.wormhole.common.WormholeException;
import com.zergclan.wormhole.core.data.PatternDate;
import com.zergclan.wormhole.pipeline.filter.*;
import com.zergclan.wormhole.pipeline.impl.IntegerDataNodePipeline;
import com.zergclan.wormhole.pipeline.impl.LongDataNodePipeline;
import com.zergclan.wormhole.pipeline.impl.PatternDateDataNodePipeline;
import com.zergclan.wormhole.pipeline.impl.StringDataNodePipeline;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * {@link DataNodePipeline} factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataNodePipelineFactory {

    /**
     * The newly created {@link DataNodePipeline} by code.
     *
     * @param code code
     * @return {@link DataNodePipeline}
     */
    public static DataNodePipeline<?> createDataNodePipeline(final String code) {
        if (validator(code)) {
            throw new WormholeException("error : Illegally code for create data node pipeline");
        }
        if ("INT:NOT:NULL#INT:NOT:NULL".equals(code)) {
            return createIntegerDataNodePipeline();
        }
        if ("INT:NOT:NULL#INT:DEFAULT:1".equals(code)) {
            return createIntegerDataNodePipelineDefault();
        }
        if ("BIGINT:NOT:NULL#BIGINT:DEFAULT:2".equals(code)) {
            return createLongDataNodePipelineDefault();
        }
        if ("VARCHAR:NOT:NULL#VARCHAR:NOT:NULL".equals(code)) {
            return createStringDataNodePipeline();
        }
        if ("DECIMAL:NOT:NULL#DECIMAL:NOT:NULL".equals(code)) {
            return createStringDataNodePipelineDecimal();
        }
        if ("DATETIME:NOT:NULL#DATETIME:NOT:NULL".equals(code)) {
            return createPatternDateDataNodePipeline();
        }
        return null;
    }

    private static boolean validator(final String code) {
        return StringUtil.isBlank(code);
    }

    private static DataNodePipeline<Integer> createIntegerDataNodePipeline() {
        DataNodePipeline<Integer> result = new IntegerDataNodePipeline();
        result.append(new IntegerRequiredValidator());
        return result;
    }

    private static DataNodePipeline<Integer> createIntegerDataNodePipelineDefault() {
        DataNodePipeline<Integer> result = new IntegerDataNodePipeline();
        result.append(new IntegerNullToDefaultHandler(1));
        return result;
    }

    private static DataNodePipeline<Long> createLongDataNodePipelineDefault() {
        DataNodePipeline<Long> result = new LongDataNodePipeline();
        result.append(new LongNullToDefaultHandler(2L));
        return result;
    }

    private static DataNodePipeline<String> createStringDataNodePipeline() {
        DataNodePipeline<String> result = new StringDataNodePipeline();
        result.append(new StringRequiredValidator());
        return result;
    }

    private static DataNodePipeline<String> createStringDataNodePipelineDecimal() {
        DataNodePipeline<String> result = new StringDataNodePipeline();
        result.append(new StringRequiredValidator());
        return result;
    }

    private static DataNodePipeline<String> createDecimalDataNodePipeline() {
        DataNodePipeline<String> result = new StringDataNodePipeline();
        result.append(new StringBlankToDefaultHandler("0.00"));
        return result;
    }

    private static DataNodePipeline<PatternDate> createPatternDateDataNodePipeline() {
        DataNodePipeline<PatternDate> result = new PatternDateDataNodePipeline();
        result.append(new DateValueRequiredValidator());
        return result;
    }
}
