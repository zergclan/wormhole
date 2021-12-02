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

package com.zergclan.wormhole.console.api.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ResultCodeTest {

    @Test
    public void assertValue() {
        assertEquals(200, ResultCode.SUCCESS.getCode());
        assertEquals(500, ResultCode.FAILED.getCode());
        assertEquals(400, ResultCode.BAD_REQUEST.getCode());
        assertEquals(401, ResultCode.UNAUTHORIZED.getCode());
        assertEquals(999, ResultCode.ERROR.getCode());
        assertEquals("SUCCESS", ResultCode.SUCCESS.getMessage());
        assertEquals("FAILED", ResultCode.FAILED.getMessage());
        assertEquals("BAD_REQUEST", ResultCode.BAD_REQUEST.getMessage());
        assertEquals("UNAUTHORIZED", ResultCode.UNAUTHORIZED.getMessage());
        assertEquals("ERROR", ResultCode.ERROR.getMessage());
    }
}
