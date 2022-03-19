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

package com.zergclan.wormhole.pipeline.core.filter.complex;

import com.zergclan.wormhole.data.api.DataGroup;
import com.zergclan.wormhole.data.api.DataNode;
import com.zergclan.wormhole.data.core.TextDataNode;
import com.zergclan.wormhole.pipeline.api.Filter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Concat merger implemented of {@link Filter}.
 */
@RequiredArgsConstructor
public final class ConcatMerger implements Filter<DataGroup> {
    
    @Getter
    private final int order;

    private final String delimiter;
    
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
        Collection<String> names = nodeNameEntry.getValue();
        Iterator<String> iterator = names.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        DataNode<?> each;
        int count = 0;
        int size = names.size();
        while (iterator.hasNext()) {
            each = dataGroup.getDataNode(iterator.next());
            stringBuilder.append(each.getValue());
            if (count == size) {
                break;
            }
            stringBuilder.append(delimiter);
        }
        return new TextDataNode(nodeNameEntry.getKey(), stringBuilder.toString());
    }
    
    @Override
    public String getType() {
        return "CONCAT_MERGER";
    }
}
