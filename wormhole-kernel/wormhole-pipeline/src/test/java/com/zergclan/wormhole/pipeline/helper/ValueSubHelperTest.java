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

import com.zergclan.wormhole.common.data.node.TextDataNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ValueSubHelperTest {
    
    @Test
    public void assertSubValue() {
        ValueSubHelper name1Helper = new ValueSubHelper(0, 4);
        TextDataNode jackName1 = name1Helper.sub(new TextDataNode("name", "jack-age"));
        assertEquals("name", jackName1.getName());
        assertEquals("jack", jackName1.getValue());
        ValueSubHelper name2Helper = new ValueSubHelper(-8, 4);
        TextDataNode jackName2 = name2Helper.sub(new TextDataNode("name", "jack-age"));
        assertEquals("name", jackName2.getName());
        assertEquals("jack", jackName2.getValue());
        ValueSubHelper age1Helper = new ValueSubHelper(5, 8);
        TextDataNode age1 = age1Helper.sub(new TextDataNode("name", "jack-age"));
        assertEquals("name", age1.getName());
        assertEquals("age", age1.getValue());
        ValueSubHelper age2Helper = new ValueSubHelper(-3, 8);
        TextDataNode age2 = age2Helper.sub(new TextDataNode("name", "jack-age"));
        assertEquals("name", age2.getName());
        assertEquals("age", age2.getValue());
    }
}
