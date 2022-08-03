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

package com.zergclan.wormhole.pipeline.core.helper;

import com.zergclan.wormhole.common.util.DateUtil;
import com.zergclan.wormhole.data.api.node.DataNode;
import com.zergclan.wormhole.data.core.node.BigDecimalDataNode;
import com.zergclan.wormhole.data.core.node.IntegerDataNode;
import com.zergclan.wormhole.data.core.node.LocalDateTimeDataNode;
import com.zergclan.wormhole.data.core.node.LongDataNode;
import com.zergclan.wormhole.data.core.node.PatternedDataTime;
import com.zergclan.wormhole.data.core.node.PatternedDataTimeDataNode;
import com.zergclan.wormhole.data.core.node.TextDataNode;
import com.zergclan.wormhole.metadata.core.plan.node.DataNodeTypeMetaData;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * Node value helper.
 */
@RequiredArgsConstructor
public final class NodeValueHelper {
    
    private final DataNodeTypeMetaData.DataType dataType;
    
    private final String name;
    
    private final String defaultValue;
    
    /**
     * Get default {@link DataNode}.
     *
     * @return {@link DataNode}
     */
    public DataNode<?> getDefaultValue() {
        switch (dataType) {
            case TEXT:
                return new TextDataNode(name, defaultValue);
            case INT:
                return new IntegerDataNode(name, Integer.parseInt(defaultValue));
            case LONG:
                return new LongDataNode(name, Long.parseLong(defaultValue));
            case MONETARY:
                return new BigDecimalDataNode(name, new BigDecimal(defaultValue));
            case DATA_TIME:
                return new LocalDateTimeDataNode(name, DateUtil.parseLocalDateTime(defaultValue, PatternedDataTime.DatePattern.STANDARD.getPattern()));
            case PATTERNED_DATA_TIME:
                PatternedDataTime.DatePattern standard = PatternedDataTime.DatePattern.STANDARD;
                DateUtil.parseLocalDateTime(defaultValue, standard.getPattern());
                return new PatternedDataTimeDataNode(name, new PatternedDataTime(DateUtil.parseDate(defaultValue, standard.getPattern()), standard));
            default:
                return null;
        }
    }
}
