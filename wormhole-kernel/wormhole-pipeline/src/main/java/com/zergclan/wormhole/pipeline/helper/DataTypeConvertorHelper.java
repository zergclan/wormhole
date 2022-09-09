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

package com.zergclan.wormhole.pipeline.helper;

import com.zergclan.wormhole.common.metadata.plan.node.DataType;
import com.zergclan.wormhole.tool.util.DateUtil;
import com.zergclan.wormhole.common.data.node.DataNode;
import com.zergclan.wormhole.common.data.node.BigDecimalDataNode;
import com.zergclan.wormhole.common.data.node.IntegerDataNode;
import com.zergclan.wormhole.common.data.node.LocalDateTimeDataNode;
import com.zergclan.wormhole.common.data.node.LongDataNode;
import com.zergclan.wormhole.common.data.node.PatternedDataTime;
import com.zergclan.wormhole.common.data.node.PatternedDataTimeDataNode;
import com.zergclan.wormhole.common.data.node.TextDataNode;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

/**
 * Data type convertor helper.
 */
@RequiredArgsConstructor
public final class DataTypeConvertorHelper {
    
    private final DataType sourceDataType;
    
    private final DataType targetDataType;
    
    private final String pattern;
    
    public DataTypeConvertorHelper(final DataType sourceDataType, final DataType targetDataType) {
        this(sourceDataType, targetDataType, null);
    }
    
    /**
     * Convert source to target of {@link DataNode} type.
     *
     * @param sourceDataNode source data node
     * @return source data node
     */
    public Optional<DataNode<?>> convert(final DataNode<?> sourceDataNode) {
        if (DataType.TEXT == sourceDataType) {
            return convertText((TextDataNode) sourceDataNode);
        }
        if (DataType.INT == sourceDataType) {
            return convertInt((IntegerDataNode) sourceDataNode);
        }
        if (DataType.LONG == sourceDataType) {
            return convertLong((LongDataNode) sourceDataNode);
        }
        if (DataType.DATA_TIME == sourceDataType) {
            return convertDataTime((LocalDateTimeDataNode) sourceDataNode);
        }
        return Optional.empty();
    }
    
    private Optional<DataNode<?>> convertText(final TextDataNode sourceDataNode) {
        if (DataType.INT == targetDataType) {
            return Optional.of(new IntegerDataNode(sourceDataNode.getName(), Integer.parseInt(sourceDataNode.getValue())));
        }
        if (DataType.LONG == targetDataType) {
            return Optional.of(new LongDataNode(sourceDataNode.getName(), Long.parseLong(sourceDataNode.getValue())));
        }
        if (DataType.MONETARY == targetDataType) {
            return Optional.of(new BigDecimalDataNode(sourceDataNode.getName(), new BigDecimal(sourceDataNode.getValue())));
        }
        if (DataType.DATA_TIME == targetDataType) {
            LocalDateTime localDateTime = DateUtil.swapToLocalDateTime(DateUtil.parseDate(sourceDataNode.getValue(), pattern));
            return Optional.of(new LocalDateTimeDataNode(sourceDataNode.getName(), localDateTime));
        }
        if (DataType.PATTERNED_DATA_TIME == targetDataType) {
            return Optional.of(new PatternedDataTimeDataNode(sourceDataNode.getName(), new PatternedDataTime(sourceDataNode.getValue(), pattern)));
        }
        return Optional.empty();
    }
    
    private Optional<DataNode<?>> convertInt(final IntegerDataNode sourceDataNode) {
        if (DataType.TEXT == targetDataType) {
            return Optional.of(new TextDataNode(sourceDataNode.getName(), String.valueOf(sourceDataNode.getValue())));
        }
        if (DataType.LONG == targetDataType) {
            return Optional.of(new LongDataNode(sourceDataNode.getName(), Long.valueOf(sourceDataNode.getValue())));
        }
        if (DataType.MONETARY == targetDataType) {
            return Optional.of(new BigDecimalDataNode(sourceDataNode.getName(), new BigDecimal(sourceDataNode.getValue())));
        }
        return Optional.empty();
    }
    
    private Optional<DataNode<?>> convertLong(final LongDataNode sourceDataNode) {
        if (DataType.TEXT == targetDataType) {
            return Optional.of(new TextDataNode(sourceDataNode.getName(), sourceDataNode.getValue().toString()));
        }
        if (DataType.MONETARY == targetDataType) {
            return Optional.of(new BigDecimalDataNode(sourceDataNode.getName(), new BigDecimal(sourceDataNode.getValue())));
        }
        if (DataType.DATA_TIME == targetDataType) {
            return Optional.of(new LocalDateTimeDataNode(sourceDataNode.getName(), LocalDateTime.ofInstant(Instant.ofEpochMilli(sourceDataNode.getValue()), ZoneId.systemDefault())));
        }
        return Optional.empty();
    }
    
    private Optional<DataNode<?>> convertDataTime(final LocalDateTimeDataNode sourceDataNode) {
        if (DataType.LONG == targetDataType) {
            return Optional.of(new LongDataNode(sourceDataNode.getName(), Timestamp.valueOf(sourceDataNode.getValue()).getTime()));
        }
        if (DataType.PATTERNED_DATA_TIME == targetDataType) {
            return Optional.of(new PatternedDataTimeDataNode(sourceDataNode.getName(),
                    new PatternedDataTime(DateUtil.swapToDate(sourceDataNode.getValue()), PatternedDataTime.DatePattern.valueOfPattern(pattern))));
        }
        return Optional.empty();
    }
}
