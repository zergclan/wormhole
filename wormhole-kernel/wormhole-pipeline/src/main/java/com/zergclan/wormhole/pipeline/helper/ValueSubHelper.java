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

import com.zergclan.wormhole.common.data.node.TextDataNode;
import com.zergclan.wormhole.tool.util.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * Value sub helper.
 */
@RequiredArgsConstructor
public final class ValueSubHelper {

    private final int start;

    private final int end;

    /**
     * Sub value.
     *
     * @param textDataNode {@link TextDataNode}
     * @return sub value
     */
    public TextDataNode sub(final TextDataNode textDataNode) {
        final String value = textDataNode.getValue();
        Validator.preState(!Objects.isNull(value), "error : range helper arg value can not be null");
        int length = value.length();
        int startIndex = computeIndex(start, length);
        int endIndex = computeIndex(end, length);
        return new TextDataNode(textDataNode.getName(), value.substring(startIndex, endIndex));
    }
    
    private int computeIndex(final int input, final int length) {
        int absInput = Math.abs(input);
        Validator.preState(absInput <= length, "error : range helper index out of bounds exception index:[%d] length:[%d]", input, length);
        return input < 0 ? length - absInput : input;
    }
}
