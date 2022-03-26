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

package com.zergclan.wormhole.pipeline.core.filter.precise.convertor;

import com.zergclan.wormhole.data.api.node.DataNode;
import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.pipeline.api.Filter;
import com.zergclan.wormhole.pipeline.core.helper.DataTypeConvertorHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Node data type convertor implemented of {@link Filter}.
 */
@RequiredArgsConstructor
public final class DataTypeConvertor implements Filter<DataGroup> {
    
    @Getter
    private final int order;
    
    private final Map<String, DataTypeConvertorHelper> dataTypeConvertorHelpers;
    
    @Override
    public boolean doFilter(final DataGroup dataGroup) {
        Iterator<Map.Entry<String, DataTypeConvertorHelper>> iterator = dataTypeConvertorHelpers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, DataTypeConvertorHelper> entry = iterator.next();
            String name = entry.getKey();
            DataTypeConvertorHelper dataTypeConvertorHelper = entry.getValue();
            DataNode<?> sourceDataNode = dataGroup.getDataNode(name);
            Optional<DataNode<?>> convertDataNode = dataTypeConvertorHelper.convert(sourceDataNode);
            if (!convertDataNode.isPresent()) {
                return false;
            }
            dataGroup.refresh(convertDataNode.get());
        }
        return true;
    }
    
    @Override
    public String getType() {
        return "DATA_TYPE_CONVERTOR";
    }
}
