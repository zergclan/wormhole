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

package com.zergclan.wormhole.pipeline.core.helper;

import com.zergclan.wormhole.data.api.node.DataNode;
import com.zergclan.wormhole.data.core.node.TextDataNode;
import lombok.RequiredArgsConstructor;

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
            String sourceCode = ((TextDataNode) source).getValue();
            Optional<String> targetCode = getTargetCode(sourceCode);
            if (targetCode.isPresent()) {
                return Optional.of(new TextDataNode(nodeName, targetCode.get()));
            }
        }
        return Optional.empty();
    }
    
    private Optional<String> getTargetCode(final String sourceCode) {
        String result = sourceTargetCodeMapping.get(sourceCode);
        if (Objects.isNull(result)) {
            return Optional.ofNullable(defaultCode);
        }
        return Optional.of(result);
    }
}
