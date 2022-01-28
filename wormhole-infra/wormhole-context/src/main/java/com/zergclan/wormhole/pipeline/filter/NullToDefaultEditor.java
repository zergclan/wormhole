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
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Map;

/**
 * Null to default editor implemented of {@link Filter}.
 */
@RequiredArgsConstructor
public final class NullToDefaultEditor implements Filter<DataGroup> {
    
    @Getter
    private final int order;
    
    private final Map<String, DataNode<?>> defaultValue;
    
    @Override
    public boolean doFilter(final DataGroup dataGroup) {
        Iterator<Map.Entry<String, DataNode<?>>> iterator = defaultValue.entrySet().iterator();
        Map.Entry<String, DataNode<?>> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (dataGroup.getDataNode(entry.getKey()).isNull()) {
                dataGroup.refresh(entry.getValue());
            }
        }
        return true;
    }
    
    @Override
    public String getType() {
        return "NULL_TO_DEFAULT_EDITOR";
    }
}
