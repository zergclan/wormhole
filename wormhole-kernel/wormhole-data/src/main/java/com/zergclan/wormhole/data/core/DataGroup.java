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

package com.zergclan.wormhole.data.core;

import com.zergclan.wormhole.common.util.JsonUtil;
import com.zergclan.wormhole.data.api.node.DataNode;
import com.zergclan.wormhole.data.core.node.PatternedDataTime;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Data group.
 */
@Getter
public final class DataGroup implements Serializable {
    
    private static final long serialVersionUID = -124290955923495591L;
    
    private final Map<String, DataNode<?>> dataNodes = new LinkedHashMap<>();
    
    /**
     * Get {@link DataNode}.
     *
     * @param name name
     * @return {@link DataNode}
     */
    public DataNode<?> getDataNode(final String name) {
        return dataNodes.get(name);
    }
    
    /**
     * Register {@link DataNode}.
     *
     * @param dataNode {@link DataNode}
     * @return is registered or not
     */
    public boolean register(final DataNode<?> dataNode) {
        if (isExist(dataNode)) {
            return false;
        }
        dataNodes.put(dataNode.getName(), dataNode);
        return true;
    }
    
    private boolean isExist(final DataNode<?> dataNode) {
        return dataNodes.containsKey(dataNode.getName());
    }
    
    /**
     * Refresh {@link DataNode}.
     *
     * @param dataNode {@link DataNode}
     * @return is refreshed or not
     */
    public boolean refresh(final DataNode<?> dataNode) {
        dataNodes.put(dataNode.getName(), dataNode);
        return true;
    }
    
    /**
     * Remove {@link DataNode}.
     *
     * @param name name
     */
    public void remove(final String name) {
        dataNodes.remove(name);
    }
    
    @Override
    public String toString() {
        Map<String, Object> result = new HashMap<>(dataNodes.size(), 1);
        for (DataNode<?> each : dataNodes.values()) {
            Object value = each.getValue();
            if (value instanceof PatternedDataTime) {
                PatternedDataTime time = (PatternedDataTime) value;
                result.put(each.getName(), time.getPatternedValue());
                continue;
            }
            result.put(each.getName(), String.valueOf(each.getValue()));
        }
        return JsonUtil.toJson(result);
    }
}
