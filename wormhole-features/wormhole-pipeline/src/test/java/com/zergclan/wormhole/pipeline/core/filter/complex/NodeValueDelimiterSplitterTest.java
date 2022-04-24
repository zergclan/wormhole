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

package com.zergclan.wormhole.pipeline.core.filter.complex;

import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.data.core.node.TextDataNode;
import com.zergclan.wormhole.metadata.core.filter.FilterType;
import com.zergclan.wormhole.pipeline.core.helper.NodeValueDelimiterSplitterHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class NodeValueDelimiterSplitterTest {
    
    private static NodeValueDelimiterSplitter nodeValueDelimiterSplitter;
    
    @BeforeAll
    public static void init() {
        nodeValueDelimiterSplitter = new NodeValueDelimiterSplitter(0, FilterType.DELIMITER_SPLITTER, createHelpers());
    }
    
    private static NodeValueDelimiterSplitterHelper[] createHelpers() {
        NodeValueDelimiterSplitterHelper[] result = new NodeValueDelimiterSplitterHelper[1];
        String delimiter = ":";
        String sourceName = "nameAge";
        String[] targetNames = {"name", "age"};
        result[0] = new NodeValueDelimiterSplitterHelper(sourceName, targetNames, delimiter);
        return result;
    }
    
    @Test
    public void assertDoFilter() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("nameAge", "jack:19"));
        assertTrue(nodeValueDelimiterSplitter.doFilter(dataGroup));
    }
    
    @Test
    public void assertGetType() {
        assertEquals(FilterType.DELIMITER_SPLITTER.name(), nodeValueDelimiterSplitter.getType());
    }
}
