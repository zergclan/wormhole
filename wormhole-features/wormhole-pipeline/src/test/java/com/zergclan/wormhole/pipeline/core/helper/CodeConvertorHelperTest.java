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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class CodeConvertorHelperTest {
    
    private static CodeConvertorHelper helper;
    
    @BeforeAll
    public static void init() {
        Map<String, String> sourceTargetCodeMapping = new LinkedHashMap<>();
        sourceTargetCodeMapping.put("N", "1");
        sourceTargetCodeMapping.put("W", "2");
        String defaultCode = "3";
        helper = new CodeConvertorHelper(sourceTargetCodeMapping, defaultCode);
    }
    
    @Test
    public void assertConvert() {
        Optional<String> valueN = helper.convert("N");
        assertTrue(valueN.isPresent());
        assertEquals("1", valueN.get());
        Optional<String> valueW = helper.convert("W");
        assertTrue(valueW.isPresent());
        assertEquals("2", valueW.get());
        Optional<String> valueNull = helper.convert(null);
        assertTrue(valueNull.isPresent());
        assertEquals("3", valueNull.get());
        Optional<String> valueEmpty = helper.convert("");
        assertTrue(valueEmpty.isPresent());
        assertEquals("3", valueEmpty.get());
        Optional<String> valueAuto = helper.convert("auto");
        assertTrue(valueAuto.isPresent());
        assertEquals("3", valueAuto.get());
    }
}
