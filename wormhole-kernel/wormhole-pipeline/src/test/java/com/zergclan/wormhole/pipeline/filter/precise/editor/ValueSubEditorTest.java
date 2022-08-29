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

package com.zergclan.wormhole.pipeline.filter.precise.editor;

import com.zergclan.wormhole.common.data.node.DataGroup;
import com.zergclan.wormhole.common.data.node.IntegerDataNode;
import com.zergclan.wormhole.common.data.node.TextDataNode;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.pipeline.filter.exception.WormholeFilterException;
import com.zergclan.wormhole.pipeline.helper.ValueSubHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ValueSubEditorTest {
    
    private static ValueSubEditor editor;
    
    @BeforeAll
    public static void init() {
        Map<String, ValueSubHelper> helpers = new LinkedHashMap<>();
        helpers.put("name", new ValueSubHelper(0, 4));
        editor = new ValueSubEditor(0, FilterType.VALUE_APPEND, helpers);
    }
    
    @Test
    public void assertDoFilterSuccess() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("name", "jack#append"));
        assertTrue(editor.doFilter(dataGroup));
        assertEquals("name", dataGroup.getDataNode("name").getName());
        assertEquals("jack", dataGroup.getDataNode("name").getValue());
    }
    
    @Test
    public void assertDoFilterNullFailed() {
        DataGroup dataGroup = new DataGroup();
        WormholeFilterException exception = assertThrows(WormholeFilterException.class, () -> editor.doFilter(dataGroup));
        assertEquals("value sub editor failed data node must be text data node, node name: [name]", exception.getMessage());
    }
    
    @Test
    public void assertDoFilterNodeTypeFailed() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new IntegerDataNode("name", 1));
        WormholeFilterException exception = assertThrows(WormholeFilterException.class, () -> editor.doFilter(dataGroup));
        assertEquals("value sub editor failed data node must be text data node, node name: [name]", exception.getMessage());
    }
}
