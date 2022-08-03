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

package com.zergclan.wormhole.pipeline.core.filter.precise.convertor;

import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.data.core.node.BigDecimalDataNode;
import com.zergclan.wormhole.data.core.node.IntegerDataNode;
import com.zergclan.wormhole.data.core.node.LongDataNode;
import com.zergclan.wormhole.data.core.node.TextDataNode;
import com.zergclan.wormhole.metadata.core.plan.filter.FilterType;
import com.zergclan.wormhole.metadata.core.plan.node.DataNodeTypeMetaData;
import com.zergclan.wormhole.pipeline.core.helper.DataTypeConvertorHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class DataTypeConvertorTest {
    
    private static DataTypeConvertor convertor;
    
    @BeforeAll
    public static void init() {
        Map<String, DataTypeConvertorHelper> helpers = new LinkedHashMap<>();
        DataTypeConvertorHelper intHelper = new DataTypeConvertorHelper(DataNodeTypeMetaData.DataType.TEXT, DataNodeTypeMetaData.DataType.INT);
        DataTypeConvertorHelper longHelper = new DataTypeConvertorHelper(DataNodeTypeMetaData.DataType.TEXT, DataNodeTypeMetaData.DataType.LONG);
        DataTypeConvertorHelper monetaryHelper = new DataTypeConvertorHelper(DataNodeTypeMetaData.DataType.TEXT, DataNodeTypeMetaData.DataType.MONETARY);
        helpers.put("int", intHelper);
        helpers.put("long", longHelper);
        helpers.put("monetary", monetaryHelper);
        convertor = new DataTypeConvertor(0, FilterType.DATA_TYPE_CONVERTOR, helpers);
    }
    
    @Test
    public void assertDoFilterSuccessText() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("int", "1"));
        dataGroup.register(new TextDataNode("long", "1"));
        dataGroup.register(new TextDataNode("monetary", "1.01"));
        assertTrue(convertor.doFilter(dataGroup));
        assertTrue(dataGroup.getDataNode("int") instanceof IntegerDataNode);
        assertEquals(1, ((IntegerDataNode) dataGroup.getDataNode("int")).getValue());
        assertTrue(dataGroup.getDataNode("long") instanceof LongDataNode);
        assertEquals(1L, ((LongDataNode) dataGroup.getDataNode("long")).getValue());
        assertTrue(dataGroup.getDataNode("monetary") instanceof BigDecimalDataNode);
        assertEquals(new BigDecimal("1.01"), ((BigDecimalDataNode) dataGroup.getDataNode("monetary")).getValue());
    }
    
    @Test
    public void assertGetType() {
        assertEquals(FilterType.DATA_TYPE_CONVERTOR.name(), convertor.getType());
    }
}
