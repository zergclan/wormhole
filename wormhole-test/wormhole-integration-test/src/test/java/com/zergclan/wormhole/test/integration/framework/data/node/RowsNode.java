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

import com.google.common.base.Splitter;
import com.zergclan.wormhole.common.constant.MarkConstant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Rows node.
 */
@RequiredArgsConstructor
@Getter
public final class RowsNode {
    
    private final List<Object> values;
    
    public RowsNode(final String values, final Collection<ColumnNode> columns) {
        this.values = initValues(Splitter.on(MarkConstant.COMMA).trimResults().splitToList(values), columns);
    }
    
    private List<Object> initValues(final List<String> values, final Collection<ColumnNode> columns) {
        int size = values.size();
        List<Object> result = new ArrayList<>(values.size());
        Iterator<ColumnNode> iterator = columns.iterator();
        for (int i = 0; i < size; i++) {
            result.add(parseObject(iterator.next(), values.get(i)));
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
}