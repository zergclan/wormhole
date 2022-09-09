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

package com.zergclan.wormhole.pipeline.filter.precise.convertor;

import com.zergclan.wormhole.common.data.DataGroup;
import com.zergclan.wormhole.common.data.node.DataNode;
import com.zergclan.wormhole.common.data.node.PatternedDataTime;
import com.zergclan.wormhole.common.data.node.PatternedDataTimeDataNode;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.pipeline.helper.PatternedDataTimeConvertorHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class PatternedDataTimeConvertorTest {
    
    private static PatternedDataTimeConvertor convertor;
    
    @BeforeAll
    public static void init() {
        Map<String, PatternedDataTimeConvertorHelper> helpers = new LinkedHashMap<>();
        PatternedDataTime.DatePattern source = PatternedDataTime.DatePattern.valueOfPattern("STANDARD");
        PatternedDataTime.DatePattern target = PatternedDataTime.DatePattern.valueOfPattern("DATE");
        PatternedDataTimeConvertorHelper helper = new PatternedDataTimeConvertorHelper(source, target);
        helpers.put("time", helper);
        convertor = new PatternedDataTimeConvertor(0, FilterType.PATTERNED_DATA_TIME_CONVERTOR, helpers);
    }
    
    @Test
    public void assertDoFilterSuccess() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new PatternedDataTimeDataNode("time", new PatternedDataTime("2022-04-24 15:00:00")));
        assertTrue(convertor.doFilter(dataGroup));
        DataNode<?> time = dataGroup.getDataNode("time");
        assertTrue(time instanceof PatternedDataTimeDataNode);
        assertTrue(time.getValue() instanceof PatternedDataTime);
        assertEquals("2022-04-24", ((PatternedDataTime) time.getValue()).getPatternedValue());
    }
    
    @Test
    public void assertGetType() {
        assertEquals(FilterType.PATTERNED_DATA_TIME_CONVERTOR.name(), convertor.getType());
    }
}
