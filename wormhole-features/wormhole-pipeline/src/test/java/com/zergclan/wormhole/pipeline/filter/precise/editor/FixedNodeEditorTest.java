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

import com.zergclan.wormhole.data.node.DataGroup;
import com.zergclan.wormhole.data.node.DataNode;
import com.zergclan.wormhole.data.node.IntegerDataNode;
import com.zergclan.wormhole.data.node.TextDataNode;
import com.zergclan.wormhole.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.metadata.plan.node.DataNodeTypeMetaData;
import com.zergclan.wormhole.pipeline.helper.NodeValueHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class FixedNodeEditorTest {
    
    private static FixedNodeEditor editor;
    
    @BeforeAll
    public static void init() {
        Collection<NodeValueHelper> helpers = new LinkedList<>();
        NodeValueHelper testHelper = new NodeValueHelper(DataNodeTypeMetaData.DataType.TEXT, "name", "jack");
        helpers.add(testHelper);
        editor = new FixedNodeEditor(0, FilterType.FIXED_NODE, helpers);
    }
    
    @Test
    public void assertDoFilterSuccess() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("name", "rose"));
        dataGroup.register(new IntegerDataNode("age", 19));
        assertTrue(editor.doFilter(dataGroup));
        DataNode<?> name = dataGroup.getDataNode("name");
        assertEquals("name", name.getName());
        assertEquals("jack", name.getValue());
    }
    
    @Test
    public void assertDoFilterSuccessBlank() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("name", null));
        dataGroup.register(new IntegerDataNode("age", 19));
        assertTrue(editor.doFilter(dataGroup));
        DataNode<?> name = dataGroup.getDataNode("name");
        assertEquals("name", name.getName());
        assertEquals("jack", name.getValue());
    }
    
    @Test
    public void assertDoFilterSuccessNull() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new IntegerDataNode("age", 19));
        assertTrue(editor.doFilter(dataGroup));
        DataNode<?> name = dataGroup.getDataNode("name");
        assertEquals("name", name.getName());
        assertEquals("jack", name.getValue());
    }
    
    @Test
    public void assertGetType() {
        assertEquals(FilterType.FIXED_NODE.name(), editor.getType());
    }
}
