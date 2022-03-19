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

package com.zergclan.wormhole.plugin.mysql.data;

import com.zergclan.wormhole.data.api.DataGroup;
import com.zergclan.wormhole.data.api.DataNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Implemented {@link DataGroup} for MySQL data source.
 */
@RequiredArgsConstructor
public final class MySQLDataGroup implements DataGroup {
    
    private static final long serialVersionUID = 4744989988261960181L;

    @Getter
    private final Map<String, DataNode<?>> dataNodes;

    @Override
    public DataNode<?> getDataNode(final String name) {
        return dataNodes.get(name);
    }
    
    @Override
    public boolean append(final DataNode<?> dataNode) {
        final String name = dataNode.getName();
        if (dataNodes.containsKey(name)) {
            return false;
        }
        dataNodes.put(name, dataNode);
        return true;
    }
    
    @Override
    public boolean refresh(final DataNode<?> dataNode) {
        dataNodes.put(dataNode.getName(), dataNode);
        return true;
    }
}
