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

import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.core.data.DataNode;
import com.zergclan.wormhole.core.data.IntegerDataNode;
import com.zergclan.wormhole.core.data.PatternDateDataNode;
import com.zergclan.wormhole.core.data.StringDataNode;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Swapper for {@link DataGroup} to data map.
 */
public final class DefaultDataGroupSwapper {

    /**
     * Swap data map to {@link DataGroup}.
     *
     * @param dataMap data map
     * @return {@link DataGroup}
     */
    public DataGroup mapToDataGroup(final Map<String, Object> dataMap) {
        DefaultDataGroup result = new DefaultDataGroup();
        IntegerDataNode integerDataNode;
        StringDataNode stringDataNode;
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Integer) {
                integerDataNode = new IntegerDataNode(name);
                result.append(name, integerDataNode.refresh((Integer) value));
            } else if (value instanceof Date) {
                PatternDateDataNode patternDateDataNode = new PatternDateDataNode(name);
                // TODO to date
            } else {
                stringDataNode = new StringDataNode(name);
                result.append(name, stringDataNode.refresh(String.valueOf(value)));
            }
        }
        return result;
    }

    /**
     * Swap {@link DataGroup} to data map.
     *
     * @param dataGroup {@link DataGroup}
     * @return data map
     */
    public Map<String, Object> dataGroupToMap(final DataGroup dataGroup) {
        Map<String, Object> result = new LinkedHashMap<>();
        Optional<Map<String, DataNode<?>>> dataNodes = dataGroup.getDataNodes();
        if (dataNodes.isPresent()) {
            Map<String, DataNode<?>> dataNodeMap = dataNodes.get();
            for (Map.Entry<String, DataNode<?>> entry : dataNodeMap.entrySet()) {
                result.put(entry.getKey(), entry.getValue().getValue());
            }
        }
        return result;
    }
}
