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
import com.zergclan.wormhole.common.data.node.TextDataNode;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class NodeNameConvertorTest {
    
    private static NodeNameConvertor convertor;
    
    @BeforeAll
    public static void init() {
        Map<String, String> mappings = new LinkedHashMap<>();
        mappings.put("sex", "gender");
        convertor = new NodeNameConvertor(0, FilterType.NAME_CONVERTOR, mappings);
    }
    
    @Test
    public void assertDoFilterSuccess() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("sex", "1"));
        assertTrue(convertor.doFilter(dataGroup));
        assertTrue(dataGroup.getDataNode("gender") instanceof TextDataNode);
        assertEquals("1", dataGroup.getDataNode("gender").getValue());
    }
    
    @Test
    public void assertGetType() {
        assertEquals(FilterType.NAME_CONVERTOR.name(), convertor.getType());
    }
}
