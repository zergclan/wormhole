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

package com.zergclan.wormhole.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class CronUtilTest {
    
    @Test
    public void assertValidate() {
        CronUtil.validate("*/5 * * * * *");
    }
    
    @Test
    public void assertValidateException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> CronUtil.validate("aaa"));
    }
    
    @Test
    public void assertNextTimeMillis() {
        long start = DateUtil.currentTimeMillis();
        Optional<Long> result = CronUtil.nextTimeMillis("*/1 * * * * *");
        assertTrue(result.isPresent());
        assertTrue(result.get() > start);
    }
}
