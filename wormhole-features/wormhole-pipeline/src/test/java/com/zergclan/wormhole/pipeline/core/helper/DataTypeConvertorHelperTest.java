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
import com.zergclan.wormhole.data.core.node.LocalDateTimeDataNode;
import com.zergclan.wormhole.data.core.node.LongDataNode;
import com.zergclan.wormhole.data.core.node.PatternedDataTime;
import com.zergclan.wormhole.data.core.node.TextDataNode;
import com.zergclan.wormhole.metadata.core.node.DataNodeTypeMetaData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class DataTypeConvertorHelperTest {
    
    @Test
    public void assertConvertText() {
        DataTypeConvertorHelper intHelper = new DataTypeConvertorHelper(DataNodeTypeMetaData.DataType.TEXT, DataNodeTypeMetaData.DataType.INT);
        Optional<DataNode<?>> anInt = intHelper.convert(new TextDataNode("int", "1"));
        assertTrue(anInt.isPresent());
        assertEquals("int", anInt.get().getName());
        assertEquals(1, anInt.get().getValue());
        DataTypeConvertorHelper longHelper = new DataTypeConvertorHelper(DataNodeTypeMetaData.DataType.TEXT, DataNodeTypeMetaData.DataType.LONG);
        Optional<DataNode<?>> anLong = longHelper.convert(new TextDataNode("long", "1"));
        assertTrue(anLong.isPresent());
        assertEquals("long", anLong.get().getName());
        assertEquals(1L, anLong.get().getValue());
        DataTypeConvertorHelper monetaryHelper = new DataTypeConvertorHelper(DataNodeTypeMetaData.DataType.TEXT, DataNodeTypeMetaData.DataType.MONETARY);
        Optional<DataNode<?>> anMonetary = monetaryHelper.convert(new TextDataNode("monetary", "1"));
        assertTrue(anMonetary.isPresent());
        assertEquals("monetary", anMonetary.get().getName());
        assertEquals(new BigDecimal("1"), anMonetary.get().getValue());
    }
    
    @Test
    public void assertConvertLong() {
        LocalDateTime localDateTime = DateUtil.parseLocalDateTime("2022-04-24 15:00:00", "yyyy-MM-dd HH:mm:ss");
        long timeMillis = DateUtil.getTimeMillis(localDateTime);
        DataTypeConvertorHelper localDateTimeHelper = new DataTypeConvertorHelper(DataNodeTypeMetaData.DataType.LONG, DataNodeTypeMetaData.DataType.DATA_TIME);
        Optional<DataNode<?>> anLocalDateTime = localDateTimeHelper.convert(new LongDataNode("localDateTime", timeMillis));
        assertTrue(anLocalDateTime.isPresent());
        assertEquals("localDateTime", anLocalDateTime.get().getName());
        assertEquals(localDateTime, anLocalDateTime.get().getValue());
        DataTypeConvertorHelper textHelper = new DataTypeConvertorHelper(DataNodeTypeMetaData.DataType.LONG, DataNodeTypeMetaData.DataType.TEXT);
        Optional<DataNode<?>> anString = textHelper.convert(new LongDataNode("string", 1L));
        assertTrue(anString.isPresent());
        assertEquals("string", anString.get().getName());
        assertEquals("1", anString.get().getValue());
        DataTypeConvertorHelper intHelper = new DataTypeConvertorHelper(DataNodeTypeMetaData.DataType.LONG, DataNodeTypeMetaData.DataType.INT);
        Optional<DataNode<?>> anInt = intHelper.convert(new LongDataNode("int", 1L));
        assertFalse(anInt.isPresent());
    }
    
    @Test
    public void assertConvertDataTime() {
        LocalDateTime localDateTime = DateUtil.parseLocalDateTime("2022-04-24 15:00:00", "yyyy-MM-dd HH:mm:ss");
        long timeMillis = DateUtil.getTimeMillis(localDateTime);
        DataTypeConvertorHelper longHelper = new DataTypeConvertorHelper(DataNodeTypeMetaData.DataType.DATA_TIME, DataNodeTypeMetaData.DataType.LONG);
        Optional<DataNode<?>> anLong = longHelper.convert(new LocalDateTimeDataNode("long", localDateTime));
        assertTrue(anLong.isPresent());
        assertEquals("long", anLong.get().getName());
        assertEquals(timeMillis, anLong.get().getValue());
        DataTypeConvertorHelper patternedDataTimeHelper = new DataTypeConvertorHelper(DataNodeTypeMetaData.DataType.DATA_TIME, DataNodeTypeMetaData.DataType.PATTERNED_DATA_TIME);
        Optional<DataNode<?>> anPatternedDataTime = patternedDataTimeHelper.convert(new LocalDateTimeDataNode("patternedDataTime", localDateTime));
        assertTrue(anPatternedDataTime.isPresent());
        assertEquals("patternedDataTime", anPatternedDataTime.get().getName());
        assertTrue(anPatternedDataTime.get().getValue() instanceof PatternedDataTime);
        PatternedDataTime value = (PatternedDataTime) anPatternedDataTime.get().getValue();
        assertEquals("2022-04-24 15:00:00", value.getPatternedValue());
        assertEquals(PatternedDataTime.DatePattern.STANDARD, value.getPattern());
    }
}
