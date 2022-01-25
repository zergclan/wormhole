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

package com.zergclan.wormhole.pipeline.filter.validator;

import com.zergclan.wormhole.api.Filter;
import com.zergclan.wormhole.common.WormholeException;
import com.zergclan.wormhole.common.util.StringUtil;
import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.core.data.DataNode;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Objects;

/**
 * Not blank validate implemented of {@link Filter}.
 */
@RequiredArgsConstructor
public final class NotBlankValidator implements Filter<DataGroup> {
    
    private final Collection<String> names;
    
    @Override
    public void doFilter(final DataGroup dataGroup) {
        for (String each : names) {
            DataNode<?> dataNode = dataGroup.getDataNode(each);
            Object value = dataNode.getValue();
            if (Objects.isNull(dataNode.getValue()) || StringUtil.isBlank(String.valueOf(value))) {
                throw new WormholeException("error : data node [%s] value can not be blank", dataNode.getName());
            }
        }
    }
}
