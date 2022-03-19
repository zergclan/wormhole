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
 * Delimiter splitter implemented of {@link Filter}.
 */
@RequiredArgsConstructor
public final class DelimiterSplitter implements Filter<DataGroup> {
    
    @Getter
    private final int order;
    
    private final String delimiter;
    
    private final Map<String, Collection<String>> nodeNameMapping;
    
    @Override
    public boolean doFilter(final DataGroup dataGroup) {
        Iterator<Map.Entry<String, Collection<String>>> iterator = nodeNameMapping.entrySet().iterator();
        while (iterator.hasNext()) {
            if (!splitNodeValue(iterator.next(), dataGroup)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean splitNodeValue(final Map.Entry<String, Collection<String>> nodeNameEntry, final DataGroup dataGroup) {
        String name = nodeNameEntry.getKey();
        DataNode<?> dataNode = dataGroup.getDataNode(nodeNameEntry.getKey());
        Collection<String> names = nodeNameEntry.getValue();
        String value = dataNode.getValue().toString();
        String[] split = value.split(delimiter);
        int length = split.length;
        if (length != names.size()) {
            return false;
        }
        int index = 0;
        Iterator<String> iterator = names.iterator();
        while (iterator.hasNext()) {
            String eachName = iterator.next();
            if (name.equals(eachName)) {
                dataGroup.refresh(new TextDataNode(iterator.next(), split[index]));
            } else {
                if (!dataGroup.append(new TextDataNode(iterator.next(), split[index]))) {
                    return false;
                }
            }
            index++;
        }
        return true;
    }
    
    @Override
    public String getType() {
        return "DELIMITER_SPLITTER";
    }
}
