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

package com.zergclan.wormhole.core.data;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class DataNodeTest {

    @Test
    public void assertStringDataNode() {
        DataNode<String> dataNode = new StringDataNode("column");
        assertNotNull(dataNode);
        assertEquals("value", dataNode.refresh("value").getValue());
    }

    @Test
    public void assertIntegerDataNode() {
        DataNode<Integer> integerDataNode = new IntegerDataNode("column");
        assertNotNull(integerDataNode);
        assertEquals(1, integerDataNode.refresh(1).getValue());
    }

    @Test
    public void assertDateDataNode() {
        Date now = new Date();
        PatternDate patternDate = new PatternDate(now, DatePattern.NATIVE_DATE_TIME);
        DataNode<PatternDate> dateDataNode = new PatternDateDataNode("column");
        assertNotNull(dateDataNode);
        assertEquals(patternDate, dateDataNode.refresh(patternDate).getValue());
    }
}
