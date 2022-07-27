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

import com.zergclan.wormhole.test.integration.framework.data.config.DataSourceConfiguration;
import com.zergclan.wormhole.test.integration.framework.data.config.TableConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Data source node.
 */
@RequiredArgsConstructor
@Getter
public final class DataSourceNode {
    
    private final Map<String, TableNode> tables;
    
    public DataSourceNode(final DataSourceConfiguration dataSource) {
        tables = initTables(dataSource.getTables());
    }
    
    private Map<String, TableNode> initTables(final Map<String, TableConfiguration> tables) {
        Map<String, TableNode> result = new LinkedHashMap<>();
        for (Entry<String, TableConfiguration> entry : tables.entrySet()) {
            result.put(entry.getKey(), new TableNode(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
