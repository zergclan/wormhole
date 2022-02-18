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
import com.zergclan.wormhole.pipeline.data.CodeMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Business code convertor implemented of {@link Filter}.
 */
@RequiredArgsConstructor
public final class CodeConvertor implements Filter<DataGroup> {
    
    @Getter
    private final int order;
    
    private final Map<String, CodeMapper> codeMappers;
    
    @Override
    public boolean doFilter(final DataGroup dataGroup) {
        Iterator<Map.Entry<String, CodeMapper>> iterator = codeMappers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, CodeMapper> entry = iterator.next();
            DataNode<?> dataNode = dataGroup.getDataNode(entry.getKey());
            Optional<String> targetCode = entry.getValue().getTargetCode(String.valueOf(dataNode.getValue()));
            if (!targetCode.isPresent()) {
                return false;
            }
            refreshDataNode(dataGroup, dataNode.getName(), targetCode.get());
        }
        return true;
    }
    
    private void refreshDataNode(final DataGroup dataGroup, final String name, final String targetCode) {
        dataGroup.refresh(new StringDataNode(name, targetCode));
    }
    
    @Override
    public String getType() {
        return "CODE_CONVERTOR";
    }
}
