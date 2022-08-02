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
import com.zergclan.wormhole.metadata.core.plan.filter.FilterType;
import com.zergclan.wormhole.pipeline.api.Filter;
import com.zergclan.wormhole.pipeline.core.filter.exception.WormholeFilterException;
import com.zergclan.wormhole.pipeline.core.helper.CodeConvertorHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * Business code convertor implemented of {@link Filter}.
 */
@RequiredArgsConstructor
@Getter
public final class CodeConvertor implements Filter<DataGroup> {
    
    private final int order;
    
    private final FilterType filterType;
    
    private final Map<String, CodeConvertorHelper> codeConvertorHelpers;
    
    @Override
    public boolean doFilter(final DataGroup dataGroup) {
        Iterator<Map.Entry<String, CodeConvertorHelper>> iterator = codeConvertorHelpers.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, CodeConvertorHelper> entry = iterator.next();
            String nodeName = entry.getKey();
            DataNode<?> dataNode = dataGroup.getDataNode(nodeName);
            CodeConvertorHelper codeConvertorHelper = entry.getValue();
            Optional<DataNode<?>> targetCode = codeConvertorHelper.convert(dataNode);
            if (!targetCode.isPresent()) {
                throw new WormholeFilterException("code convertor failed source code: [%s] can not convertor to target code, node name: [%s]", dataNode.getValue(), nodeName);
            }
            dataGroup.refresh(targetCode.get());
        }
        return true;
    }
    
    @Override
    public String getType() {
        return filterType.name();
    }
}
