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

package com.zergclan.wormhole.common.data.node;

import com.zergclan.wormhole.common.metadata.plan.node.DataNodeMetaData;
import com.zergclan.wormhole.common.metadata.plan.node.DataType;
import com.zergclan.wormhole.tool.util.DateUtil;
import com.zergclan.wormhole.tool.util.StringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Builder of {@link DataNode}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataNodeBuilder {
    
    /**
     * Build {@link DataNode}.
     *
     * @param data data
     * @param metaData {@link DataNodeMetaData}
     * @return {@link DataNode}
     */
    public static DataNode<?> build(final Object data, final DataNodeMetaData metaData) {
        String name = metaData.getName();
        DataType dataType = metaData.getType().getDataType();
        if (Objects.isNull(data)) {
            return new ObjectDataNode(name, null);
        }
        switch (dataType) {
            case OBJECT:
                return new ObjectDataNode(name, data);
            case TEXT:
            case CODE:
                return createTextDataNode(name, data);
            case INT:
                return createIntegerDataNode(name, data);
            case LONG:
                return createLongDataNode(name, data);
            case MONETARY:
                return createBigDecimalDataNode(name, data);
            case DATA_TIME:
                return createLocalDateTimeDataNode(name, data);
            case PATTERNED_DATA_TIME:
                return createPatternedDataTimeDataNode(name, data);
            default:
                throw new UnsupportedOperationException();
        }
    }
    
    private static TextDataNode createTextDataNode(final String name, final Object data) {
        return new TextDataNode(name, StringUtil.valueOf(data));
    }
    
    private static IntegerDataNode createIntegerDataNode(final String name, final Object data) {
        return new IntegerDataNode(name, Integer.parseInt(data.toString()));
    }
    
    private static LongDataNode createLongDataNode(final String name, final Object data) {
        return new LongDataNode(name, Long.parseLong(data.toString()));
    }
    
    private static BigDecimalDataNode createBigDecimalDataNode(final String name, final Object data) {
        return new BigDecimalDataNode(name, new BigDecimal(data.toString()));
    }
    
    private static LocalDateTimeDataNode createLocalDateTimeDataNode(final String name, final Object data) {
        if (data instanceof LocalDateTime) {
            return new LocalDateTimeDataNode(name, (LocalDateTime) data);
        }
        if (data instanceof java.util.Date) {
            return new LocalDateTimeDataNode(name, DateUtil.swapToLocalDateTime((java.util.Date) data));
        }
        throw new ClassCastException();
    }
    
    private static PatternedDataTimeDataNode createPatternedDataTimeDataNode(final String name, final Object data) {
        return new PatternedDataTimeDataNode(name, new PatternedDataTime(data.toString()));
    }
}
