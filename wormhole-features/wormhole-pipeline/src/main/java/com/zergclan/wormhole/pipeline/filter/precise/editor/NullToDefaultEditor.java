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

package com.zergclan.wormhole.pipeline.filter.precise.editor;

import com.zergclan.wormhole.data.node.DataNode;
import com.zergclan.wormhole.data.node.DataGroup;
import com.zergclan.wormhole.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.pipeline.filter.Filter;
import com.zergclan.wormhole.pipeline.helper.NodeValueHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Null to default editor implemented of {@link Filter}.
 */
@RequiredArgsConstructor
@Getter
public final class NullToDefaultEditor implements Filter<DataGroup> {
    
    private final int order;
    
    private final FilterType filterType;
    
    private final Map<String, NodeValueHelper> defaultValueHelpers;
    
    @Override
    public boolean doFilter(final DataGroup dataGroup) {
        Iterator<Map.Entry<String, NodeValueHelper>> iterator = defaultValueHelpers.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, NodeValueHelper> entry = iterator.next();
            String nodeName = entry.getKey();
            DataNode<?> dataNode = dataGroup.getDataNode(nodeName);
            NodeValueHelper nodeValueHelper = entry.getValue();
            if (Objects.isNull(dataNode)) {
                dataGroup.refresh(nodeValueHelper.getDefaultValue());
                continue;
            }
            if (dataNode.isNull()) {
                dataGroup.refresh(nodeValueHelper.getDefaultValue());
            }
        }
        return true;
    }
    
    @Override
    public String getType() {
        return filterType.name();
    }
}
