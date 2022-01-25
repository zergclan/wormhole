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

package com.zergclan.wormhole.pipeline.data;

import com.zergclan.wormhole.api.Swapper;
import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.core.data.DataNode;

import java.util.LinkedHashMap;
import java.util.Map;

public final class MySQLDataGroup implements DataGroup {
    
    private static final long serialVersionUID = 4744989988261960181L;
    
    private static final Swapper<Map.Entry<String, Object>, DataNode<?>> SWAPPER = new MySQLDataGroupSwapper();
    
    private final Map<String, DataNode<?>> dataNodes = new LinkedHashMap<>();
    
    @Override
    public void init(final Map<String, Object> dataMap) {
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            DataNode<?> dataNode = SWAPPER.swapToTarget(entry);
            append(dataNode);
        }
    }
    
    @Override
    public Map<String, Object> getDataMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, DataNode<?>> entry : dataNodes.entrySet()) {
            result.put(SWAPPER.swapToSource(entry.getValue()).getKey(), SWAPPER.swapToSource(entry.getValue()).getValue());
        }
        return result;
    }
    
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
    
    // FIXME implemented in plugin
    private static class MySQLDataGroupSwapper implements Swapper<Map.Entry<String, Object>, DataNode<?>> {
    
        @Override
        public DataNode<?> swapToTarget(final Map.Entry<String, Object> source) {
            return null;
        }
        
        @Override
        public Map.Entry<String, Object> swapToSource(final DataNode<?> target) {
            return null;
        }
    }
}
