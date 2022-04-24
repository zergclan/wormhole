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

package com.zergclan.wormhole.pipeline.core.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ValueRangeHelperTest {
    
    @Test
    public void assertAppendValue() {
        ValueRangeHelper name1Helper = new ValueRangeHelper(0, 4);
        assertEquals("name", name1Helper.sub("name-age"));
        ValueRangeHelper name2Helper = new ValueRangeHelper(-8, 4);
        assertEquals("name", name1Helper.sub("name-age"));
        ValueRangeHelper age1Helper = new ValueRangeHelper(5, 8);
        assertEquals("age", age1Helper.sub("name-age"));
        ValueRangeHelper age2Helper = new ValueRangeHelper(-3, 8);
        assertEquals("age", age2Helper.sub("name-age"));
    }
}
