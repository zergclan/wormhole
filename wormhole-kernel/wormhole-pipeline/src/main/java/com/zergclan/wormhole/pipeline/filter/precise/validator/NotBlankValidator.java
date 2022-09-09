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

package com.zergclan.wormhole.pipeline.filter.precise.validator;

import com.zergclan.wormhole.common.data.node.DataNode;
import com.zergclan.wormhole.common.data.DataGroup;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.pipeline.filter.Filter;
import com.zergclan.wormhole.pipeline.filter.exception.WormholeFilterException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Not blank validator implemented of {@link Filter}.
 */
@RequiredArgsConstructor
public final class NotBlankValidator implements Filter<DataGroup> {
    
    @Getter
    private final int order;
    
    private final FilterType filterType;
    
    private final String[] names;
    
    @Override
    public boolean doFilter(final DataGroup dataGroup) {
        int length = names.length;
        DataNode<?> dataNode;
        for (int i = 0; i < length; i++) {
            String nodeName = names[i];
            dataNode = dataGroup.getDataNode(nodeName);
            if (dataNode.isBlank()) {
                throw new WormholeFilterException("not blank validator failed data node value is blank, node name: [%s]", nodeName);
            }
        }
        return true;
    }
    
    @Override
    public String getType() {
        return filterType.name();
    }
}
