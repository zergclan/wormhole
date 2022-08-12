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

package com.zergclan.wormhole.pipeline.helper;

import com.zergclan.wormhole.data.node.DataNode;
import com.zergclan.wormhole.data.node.BigDecimalDataNode;
import com.zergclan.wormhole.data.node.IntegerDataNode;
import com.zergclan.wormhole.data.node.LongDataNode;
import com.zergclan.wormhole.data.node.TextDataNode;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Code convertor helper.
 */
@RequiredArgsConstructor
public final class CodeConvertorHelper {
    
    private final Map<String, String> sourceTargetCodeMapping;
    
    private final String defaultCode;
    
    /**
     * Get target code.
     *
     * @param source source {@link DataNode}
     * @return target code
     */
    public Optional<DataNode<?>> convert(final DataNode<?> source) {
        String nodeName = source.getName();
        if (source instanceof TextDataNode) {
            return createTextTargetCode(nodeName, (TextDataNode) source);
        }
        if (source instanceof IntegerDataNode) {
            return createIntegerTargetCode(nodeName, (IntegerDataNode) source);
        }
        if (source instanceof BigDecimalDataNode) {
            return createBigDecimalTargetCode(nodeName, (BigDecimalDataNode) source);
        }
        if (source instanceof LongDataNode) {
            return createLongTargetCode(nodeName, (LongDataNode) source);
        }
        return Optional.empty();
    }
    
    private Optional<DataNode<?>> createLongTargetCode(final String nodeName, final LongDataNode source) {
        return getTargetCode(source.getValue().toString()).map(targetCode -> new LongDataNode(nodeName, Long.parseLong(targetCode)));
    }
    
    private Optional<DataNode<?>> createIntegerTargetCode(final String nodeName, final IntegerDataNode source) {
        return getTargetCode(source.getValue().toString()).map(targetCode -> new IntegerDataNode(nodeName, Integer.parseInt(targetCode)));
    }
    
    private Optional<DataNode<?>> createBigDecimalTargetCode(final String nodeName, final BigDecimalDataNode source) {
        return getTargetCode(source.getValue().toString()).map(targetCode -> new BigDecimalDataNode(nodeName, new BigDecimal(targetCode)));
    }
    
    private Optional<DataNode<?>> createTextTargetCode(final String nodeName, final TextDataNode source) {
        return getTargetCode(source.getValue()).map(targetCode -> new TextDataNode(nodeName, targetCode));
    }
    
    private Optional<String> getTargetCode(final String sourceCode) {
        String result = sourceTargetCodeMapping.get(sourceCode);
        if (Objects.isNull(result)) {
            return Optional.ofNullable(defaultCode);
        }
        return Optional.of(result);
    }
}
