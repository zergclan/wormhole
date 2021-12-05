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

package com.zergclan.wormhole.pipeline.converter;

import com.zergclan.wormhole.core.data.IntegerDataNode;
import com.zergclan.wormhole.core.data.StringDataNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class StringToIntegerConverterTest {

    private static DataNodeConverter<StringDataNode, IntegerDataNode> wormholeConverter;
    
    @BeforeAll
    public static void init() {
        wormholeConverter = new StringToIntegerConverter();
    }
    
    @Test
    public void assertConvert() {
        StringDataNode stringDataNode = new StringDataNode("column");
        stringDataNode.setValue("1");
        IntegerDataNode target = wormholeConverter.convert(stringDataNode);
        assertEquals(1, target.getValue());
    }
}