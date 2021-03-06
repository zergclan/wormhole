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

import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.metadata.core.filter.FilterType;
import com.zergclan.wormhole.pipeline.api.Filter;
import com.zergclan.wormhole.pipeline.core.helper.NodeValueConcatMergerHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Node value concat merger implemented of {@link Filter}.
 */
@RequiredArgsConstructor
@Getter
public final class NodeValueConcatMerger implements Filter<DataGroup> {
    
    private final int order;
    
    private final FilterType filterType;
    
    private final NodeValueConcatMergerHelper[] nodeValueConcatMergerHelpers;
    
    @Override
    public boolean doFilter(final DataGroup dataGroup) {
        int length = nodeValueConcatMergerHelpers.length;
        for (int i = 0; i < length; i++) {
            if (!nodeValueConcatMergerHelpers[i].merge(dataGroup)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String getType() {
        return filterType.name();
    }
}
