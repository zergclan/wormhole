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

import com.zergclan.wormhole.core.api.data.DataNode;
import com.zergclan.wormhole.core.data.BigDecimalDataNode;
import com.zergclan.wormhole.core.data.IntegerDataNode;
import com.zergclan.wormhole.core.data.LocalDateTimeDataNode;
import com.zergclan.wormhole.core.data.LongDataNode;
import com.zergclan.wormhole.core.data.PatternedDataTime;
import com.zergclan.wormhole.core.data.PatternedDataTimeDataNode;
import com.zergclan.wormhole.core.data.TextDataNode;
import com.zergclan.wormhole.core.metadata.node.DataNodeTypeMetaData;
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
    
    private final DataNodeTypeMetaData.DataType targetDataType;
    
    private final DataNodeTypeMetaData.DataType sourceDataType;
    
    /**
     * Convert source to target of {@link DataNode} type.
     *
     * @param sourceDataNode source data node
     * @return source data node
     */
    public Optional<DataNode<?>> convert(final DataNode<?> sourceDataNode) {
        if (DataNodeTypeMetaData.DataType.TEXT == sourceDataType) {
            return convertText((TextDataNode) sourceDataNode);
        }
        if (DataNodeTypeMetaData.DataType.LONG == sourceDataType) {
            return convertLong((LongDataNode) sourceDataNode);
        }
        if (DataNodeTypeMetaData.DataType.DATA_TIME == sourceDataType) {
            return convertDataTime((LocalDateTimeDataNode) sourceDataNode);
        }
        return Optional.empty();
    }
    
    private Optional<DataNode<?>> convertText(final TextDataNode sourceDataNode) {
        if (DataNodeTypeMetaData.DataType.INT == targetDataType) {
            return Optional.of(new IntegerDataNode(sourceDataNode.getName(), Integer.parseInt(sourceDataNode.getValue())));
        }
        if (DataNodeTypeMetaData.DataType.LONG == targetDataType) {
            return Optional.of(new LongDataNode(sourceDataNode.getName(), Long.parseLong(sourceDataNode.getValue())));
        }
        if (DataNodeTypeMetaData.DataType.MONETARY == targetDataType) {
            return Optional.of(new BigDecimalDataNode(sourceDataNode.getName(), new BigDecimal(sourceDataNode.getValue())));
        }
        return Optional.empty();
    }
    
    private Optional<DataNode<?>> convertLong(final LongDataNode sourceDataNode) {
        if (DataNodeTypeMetaData.DataType.DATA_TIME == targetDataType) {
            return Optional.of(new LocalDateTimeDataNode(sourceDataNode.getName(), LocalDateTime.ofInstant(Instant.ofEpochMilli(sourceDataNode.getValue()), ZoneId.systemDefault())));
        }
        if (DataNodeTypeMetaData.DataType.TEXT == targetDataType) {
            return Optional.of(new TextDataNode(sourceDataNode.getName(), sourceDataNode.getValue().toString()));
        }
        return Optional.empty();
    }
    
    private Optional<DataNode<?>> convertDataTime(final LocalDateTimeDataNode sourceDataNode) {
        if (DataNodeTypeMetaData.DataType.LONG == targetDataType) {
            return Optional.of(new LongDataNode(sourceDataNode.getName(), Timestamp.valueOf(sourceDataNode.getValue()).getTime()));
        }
        if (DataNodeTypeMetaData.DataType.PATTERNED_DATA_TIME == targetDataType) {
            return Optional.of(new PatternedDataTimeDataNode(sourceDataNode.getName(), new PatternedDataTime(sourceDataNode.getValue(), PatternedDataTime.DatePattern.STANDARD)));
        }
        return Optional.empty();
    }
}
