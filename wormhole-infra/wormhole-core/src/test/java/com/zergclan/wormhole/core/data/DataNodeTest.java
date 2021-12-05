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

import com.zergclan.wormhole.core.data.type.DatePattern;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class DataNodeTest {

    @Test
    public void assertStringDataNode() {
        DataNode<String> stringDataNode = new StringDataNode("column");
        assertNotNull(stringDataNode);
        stringDataNode.setValue("value");
        assertEquals("value", stringDataNode.getValue());
    }

    @Test
    public void assertIntegerDataNode() {
        DataNode<Integer> integerDataNode = new IntegerDataNode("column");
        assertNotNull(integerDataNode);
        integerDataNode.setValue(1);
        assertEquals(1, integerDataNode.getValue());
    }

    @Test
    public void assertDateDataNode() {
        DataNode<Date> dateDataNode = new DateDataNode("column", DatePattern.NATIVE);
        assertNotNull(dateDataNode);
        final Date date = new Date();
        dateDataNode.setValue(date);
        assertEquals(date, dateDataNode.getValue());
    }
}
