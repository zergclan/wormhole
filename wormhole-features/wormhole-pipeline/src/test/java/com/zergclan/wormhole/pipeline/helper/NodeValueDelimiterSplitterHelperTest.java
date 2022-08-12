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

import com.zergclan.wormhole.data.node.DataGroup;
import com.zergclan.wormhole.data.node.DataNode;
import com.zergclan.wormhole.data.node.TextDataNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class NodeValueDelimiterSplitterHelperTest {
    
    private static NodeValueDelimiterSplitterHelper helper;
    
    @BeforeAll
    public static void init() {
        String sourceName = "nameAge";
        String[] targetNames = {"name", "age"};
        String delimiter = ":";
        helper = new NodeValueDelimiterSplitterHelper(sourceName, targetNames, delimiter);
    }
    
    @Test
    public void assertSplitNodeValue() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("nameAge", "jack:19"));
        assertTrue(helper.splitNodeValue(dataGroup));
        assertNull(dataGroup.getDataNode("nameAge"));
        DataNode<?> nameNode = dataGroup.getDataNode("name");
        assertTrue(nameNode instanceof TextDataNode);
        assertEquals("name", nameNode.getName());
        assertEquals("jack", nameNode.getValue());
        DataNode<?> ageNode = dataGroup.getDataNode("age");
        assertTrue(ageNode instanceof TextDataNode);
        assertEquals("age", ageNode.getName());
        assertEquals("19", ageNode.getValue());
    }
}
