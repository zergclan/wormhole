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

import com.zergclan.wormhole.core.api.Filter;
import com.zergclan.wormhole.core.api.data.DataGroup;
import com.zergclan.wormhole.core.api.data.DataNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Map;

/**
 * Name convertor implemented of {@link Filter}.
 */
@RequiredArgsConstructor
public final class NameConvertor implements Filter<DataGroup> {

    @Getter
    private final int order;

    private final Map<String, String> names;

    @Override
    public boolean doFilter(final DataGroup dataGroup) {
        Iterator<Map.Entry<String, String>> iterator = names.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            DataNode<?> dataNode = dataGroup.getDataNode(entry.getKey());
            dataNode.refreshName(entry.getValue());
        }
        return true;
    }

    @Override
    public String getType() {
        return "NAME_CONVERTOR";
    }
}
