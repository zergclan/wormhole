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

package com.zergclan.wormhole.pipeline.filter.precise.validator;

import com.zergclan.wormhole.common.data.DataGroup;
import com.zergclan.wormhole.common.data.node.IntegerDataNode;
import com.zergclan.wormhole.common.data.node.TextDataNode;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.pipeline.filter.exception.WormholeFilterException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class NotNullValidatorTest {
    
    private static NotNullValidator validator;
    
    @BeforeAll
    public static void init() {
        String[] names = {"name", "age"};
        validator = new NotNullValidator(0, FilterType.NOT_NULL, names);
    }
    
    @Test
    public void assertDoFilterSuccess() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("name", "jack"));
        dataGroup.register(new IntegerDataNode("age", 19));
        assertTrue(validator.doFilter(dataGroup));
    }
    
    @Test
    public void assertDoFilterFailed() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.register(new TextDataNode("name", "jack"));
        dataGroup.register(new IntegerDataNode("age", null));
        WormholeFilterException exception = assertThrows(WormholeFilterException.class, () -> validator.doFilter(dataGroup));
        assertEquals("not null validator failed data node value is null, node name: [age]", exception.getMessage());
    }
    
    @Test
    public void assertGetType() {
        assertEquals(FilterType.NOT_NULL.name(), validator.getType());
    }
}
