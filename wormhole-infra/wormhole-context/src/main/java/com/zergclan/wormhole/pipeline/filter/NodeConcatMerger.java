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

package com.zergclan.wormhole.pipeline.filter;

import com.zergclan.wormhole.api.Filter;
import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.core.data.DataNode;
import com.zergclan.wormhole.core.data.StringDataNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Node concat merger implemented of {@link Filter}.
 */
@RequiredArgsConstructor
public final class NodeConcatMerger implements Filter<DataGroup> {
    
    @Getter
    private final int order;
    
    private final Map<String, Collection<String>> nodeNameMapping;
    
    @Override
    public boolean doFilter(final DataGroup dataGroup) {
        Iterator<Map.Entry<String, Collection<String>>> iterator = nodeNameMapping.entrySet().iterator();
        while (iterator.hasNext()) {
            dataGroup.refresh(mergeNodeValue(iterator.next(), dataGroup));
        }
        return true;
    }
    
    private DataNode<String> mergeNodeValue(final Map.Entry<String, Collection<String>> nodeNameEntry, final DataGroup dataGroup) {
        Iterator<String> iterator = nodeNameEntry.getValue().iterator();
        DataNode<String> result = new StringDataNode(nodeNameEntry.getKey());
        StringBuilder stringBuilder = new StringBuilder();
        DataNode<?> each;
        while (iterator.hasNext()) {
            each = dataGroup.getDataNode(iterator.next());
            stringBuilder.append(each.getValue());
        }
        return result.refresh(stringBuilder.toString());
    }
    
    @Override
    public String getType() {
        return "CONCAT_MERGER";
    }
}
