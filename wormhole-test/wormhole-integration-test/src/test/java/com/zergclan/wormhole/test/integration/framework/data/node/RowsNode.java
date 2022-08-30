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

package com.zergclan.wormhole.test.integration.framework.data.node;

import com.zergclan.wormhole.tool.constant.MarkConstant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Rows node.
 */
@RequiredArgsConstructor
@Getter
public final class RowsNode {
    
    private final Object[] values;
    
    public RowsNode(final String values, final Collection<ColumnNode> columns) {
        this.values = initValues(splitValues(values), columns);
    }
    
    private String[] splitValues(final String values) {
        return values.split(MarkConstant.COMMA);
    }
    
    private Object[] initValues(final String[] values, final Collection<ColumnNode> columns) {
        int length = values.length;
        Object[] result = new Object[length];
        Iterator<ColumnNode> iterator = columns.iterator();
        for (int i = 0; i < length; i++) {
            result[i] = parseObject(iterator.next(), values[i]);
        }
        return result;
    }
    
    private Object parseObject(final ColumnNode columnNode, final String value) {
        switch (columnNode.getType()) {
            case "varchar" :
                return value;
            case "int" :
                return Integer.parseInt(value);
            default:
                throw new UnsupportedOperationException();
        }
    }
    
    /**
     * Get value iterator.
     *
     * @return value iterator
     */
    public Iterator<Object> getValueIterator() {
        return Arrays.stream(values).iterator();
    }
}
