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

package com.zergclan.wormhole.pipeline.filter.precise.convertor;

import com.zergclan.wormhole.common.data.node.DataNode;
import com.zergclan.wormhole.common.data.DataGroup;
import com.zergclan.wormhole.common.data.node.PatternedDataTime;
import com.zergclan.wormhole.common.data.node.PatternedDataTimeDataNode;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.pipeline.filter.DataGroupFilterChain;
import com.zergclan.wormhole.pipeline.filter.exception.WormholeFilterException;
import com.zergclan.wormhole.pipeline.helper.PatternedDataTimeConvertorHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 *  {@link PatternedDataTime} convertor implemented of {@link DataGroupFilterChain}.
 */
@RequiredArgsConstructor
@Getter
public final class PatternedDataTimeConvertor implements DataGroupFilterChain {
    
    private final int order;
    
    private final FilterType filterType;
    
    private final Map<String, PatternedDataTimeConvertorHelper> patternedDataTimeConvertorHelpers;
    
    @Override
    public boolean doFilter(final DataGroup dataGroup) {
        Iterator<Map.Entry<String, PatternedDataTimeConvertorHelper>> iterator = patternedDataTimeConvertorHelpers.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, PatternedDataTimeConvertorHelper> entry = iterator.next();
            String nodeName = entry.getKey();
            DataNode<?> dataNode = dataGroup.getDataNode(nodeName);
            if (dataNode instanceof PatternedDataTimeDataNode) {
                Optional<PatternedDataTime> target = entry.getValue().convert((PatternedDataTime) dataNode.getValue());
                if (!target.isPresent()) {
                    return false;
                }
                dataGroup.refresh(new PatternedDataTimeDataNode(nodeName, target.get()));
                continue;
            }
            throw new WormholeFilterException("value append editor failed data node must be patterned data time data node, node name: [%s]", nodeName);
        }
        return true;
    }
    
    @Override
    public String getType() {
        return FilterType.PATTERNED_DATA_TIME_CONVERTOR.name();
    }
}
