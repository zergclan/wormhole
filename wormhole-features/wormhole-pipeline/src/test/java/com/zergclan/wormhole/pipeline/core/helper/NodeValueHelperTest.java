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
import com.zergclan.wormhole.metadata.core.node.DataNodeTypeMetaData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class NodeValueHelperTest {
    
    @Test
    public void assertGetDefaultTextValue() {
        NodeValueHelper testHelper = new NodeValueHelper(DataNodeTypeMetaData.DataType.TEXT, "text", "default_value");
        DataNode<?> textValue = testHelper.getDefaultValue();
        assertTrue(textValue instanceof TextDataNode);
        assertEquals("text", textValue.getName());
        assertEquals("default_value", textValue.getValue());
    }
    
    @Test
    public void assertGetDefaultIntValue() {
        NodeValueHelper intHelper = new NodeValueHelper(DataNodeTypeMetaData.DataType.INT, "int", "0");
        DataNode<?> intValue = intHelper.getDefaultValue();
        assertTrue(intValue instanceof IntegerDataNode);
        assertEquals("int", intValue.getName());
        assertEquals(0, intValue.getValue());
    }
    
    @Test
    public void assertGetDefaultLongValue() {
        NodeValueHelper longHelper = new NodeValueHelper(DataNodeTypeMetaData.DataType.LONG, "long", "0");
        DataNode<?> longValue = longHelper.getDefaultValue();
        assertTrue(longValue instanceof LongDataNode);
        assertEquals("long", longValue.getName());
        assertEquals(0L, longValue.getValue());
    }
    
    @Test
    public void assertGetDefaultMonetaryValue() {
        NodeValueHelper monetaryHelper = new NodeValueHelper(DataNodeTypeMetaData.DataType.MONETARY, "monetary", "1.01");
        DataNode<?> monetaryValue = monetaryHelper.getDefaultValue();
        assertTrue(monetaryValue instanceof BigDecimalDataNode);
        assertEquals("monetary", monetaryValue.getName());
        assertEquals(new BigDecimal("1.01"), monetaryValue.getValue());
    }
    
    @Test
    public void assertGetDefaultDataTimeValue() {
        LocalDateTime localDateTime = DateUtil.parseLocalDateTime("2022-04-24 15:00:00", "yyyy-MM-dd HH:mm:ss");
        NodeValueHelper localDateTimeHelper = new NodeValueHelper(DataNodeTypeMetaData.DataType.DATA_TIME, "localDateTime", "2022-04-24 15:00:00");
        DataNode<?> localDateTimeValue = localDateTimeHelper.getDefaultValue();
        assertTrue(localDateTimeValue instanceof LocalDateTimeDataNode);
        assertEquals("localDateTime", localDateTimeValue.getName());
        assertEquals(localDateTime, localDateTimeValue.getValue());
    }
    
    @Test
    public void assertGetDefaultPatternedDataTimeValue() {
        NodeValueHelper patternedDataTimeHelper = new NodeValueHelper(DataNodeTypeMetaData.DataType.PATTERNED_DATA_TIME, "patternedDataTime", "2022-04-24 15:00:00");
        DataNode<?> patternedDataTimeValue = patternedDataTimeHelper.getDefaultValue();
        assertTrue(patternedDataTimeValue instanceof PatternedDataTimeDataNode);
        assertEquals("patternedDataTime", patternedDataTimeValue.getName());
        assertTrue(patternedDataTimeValue.getValue() instanceof PatternedDataTime);
        PatternedDataTime value = (PatternedDataTime) patternedDataTimeValue.getValue();
        assertEquals(PatternedDataTime.DatePattern.valueOfPattern("yyyy-MM-dd HH:mm:ss"), value.getPattern());
        assertEquals("2022-04-24 15:00:00", value.getPatternedValue());
    }
}
