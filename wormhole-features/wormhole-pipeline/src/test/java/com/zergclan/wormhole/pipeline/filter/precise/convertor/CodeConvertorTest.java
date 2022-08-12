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

import com.zergclan.wormhole.data.node.DataGroup;
import com.zergclan.wormhole.data.node.TextDataNode;
import com.zergclan.wormhole.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.pipeline.helper.CodeConvertorHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class CodeConvertorTest {

    private static CodeConvertor convertor;
    
    @BeforeAll
    public static void init() {
        Map<String, CodeConvertorHelper> helpers = new LinkedHashMap<>();
        Map<String, String> sourceTargetCodeMapping = new LinkedHashMap<>();
        sourceTargetCodeMapping.put("N", "1");
        sourceTargetCodeMapping.put("W", "2");
        String defaultCode = "3";
        CodeConvertorHelper helper = new CodeConvertorHelper(sourceTargetCodeMapping, defaultCode);
        helpers.put("gender", helper);
        convertor = new CodeConvertor(0, FilterType.CODE_CONVERTOR, helpers);
    }
    
    @Test
    public void assertDoFilterSuccess1() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("gender", "N"));
        assertTrue(convertor.doFilter(dataGroup));
        assertEquals("1", dataGroup.getDataNode("gender").getValue());
    }
    
    @Test
    public void assertDoFilterSuccess2() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("gender", "W"));
        assertTrue(convertor.doFilter(dataGroup));
        assertEquals("2", dataGroup.getDataNode("gender").getValue());
    }
    
    @Test
    public void assertDoFilterSuccess3() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("gender", "A"));
        assertTrue(convertor.doFilter(dataGroup));
        assertEquals("3", dataGroup.getDataNode("gender").getValue());
    }
    
    @Test
    public void assertDoFilterSuccessNull() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("gender", null));
        assertTrue(convertor.doFilter(dataGroup));
        assertEquals("3", dataGroup.getDataNode("gender").getValue());
    }
    
    @Test
    public void assertDoFilterSuccessBlank() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("gender", ""));
        assertTrue(convertor.doFilter(dataGroup));
        assertEquals("3", dataGroup.getDataNode("gender").getValue());
    }
    
    @Test
    public void assertGetType() {
        assertEquals(FilterType.CODE_CONVERTOR.name(), convertor.getType());
    }
}
