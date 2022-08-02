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

package com.zergclan.wormhole.pipeline.core.filter.precise.validator;

import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.data.core.node.IntegerDataNode;
import com.zergclan.wormhole.data.core.node.TextDataNode;
import com.zergclan.wormhole.metadata.core.plan.filter.FilterType;
import com.zergclan.wormhole.pipeline.core.filter.exception.WormholeFilterException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class NotBlankValidatorTest {
    
    private static NotBlankValidator validator;
    
    @BeforeAll
    public static void init() {
        String[] names = {"name", "age"};
        validator = new NotBlankValidator(0, FilterType.NOT_BLANK, names);
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
        dataGroup.register(new TextDataNode("name", ""));
        dataGroup.register(new IntegerDataNode("age", 19));
        WormholeFilterException exception = assertThrows(WormholeFilterException.class, () -> validator.doFilter(dataGroup));
        assertEquals("not blank validator failed data node value is blank, node name: [name]", exception.getMessage());
    }
    
    @Test
    public void assertGetType() {
        assertEquals(FilterType.NOT_BLANK.name(), validator.getType());
    }
}
