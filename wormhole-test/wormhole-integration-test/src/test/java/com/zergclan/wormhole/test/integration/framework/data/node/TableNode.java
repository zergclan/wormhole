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

import com.zergclan.wormhole.test.integration.framework.data.config.TableConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedList;

@RequiredArgsConstructor
@Getter
public final class TableNode {
    
    private final Collection<ColumnNode> columns;
    
    private final Collection<RowNode> rows;
    
    public TableNode(final TableConfiguration table) {
        columns = initColumns(table.getColumns());
        rows = initRows(table.getRows());
    }
    
    private Collection<ColumnNode> initColumns(final Collection<String> columns) {
        Collection<ColumnNode> result = new LinkedList<>();
        for (String each : columns) {
            result.add(new ColumnNode(each));
        }
        return result;
    }
    
    private Collection<RowNode> initRows(final Collection<String> rows) {
        Collection<RowNode> result = new LinkedList<>();
        for (String each : rows) {
            result.add(new RowNode(each));
        }
        return result;
    }
}
