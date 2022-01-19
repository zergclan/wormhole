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

package com.zergclan.wormhole.common;

import com.zergclan.wormhole.common.util.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class DateUtilTest {
    
    @Test
    public void assertCurrentTimeMillis() {
        assertTrue(System.currentTimeMillis() <= DateUtil.currentTimeMillis());
    }

    @Test
    public void assertDateFormat() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String expectedText = "2021-11-11 11:11:11";
        Date date = DateUtil.parse(expectedText, pattern);
        String actualText = DateUtil.format(date, pattern);
        assertEquals(expectedText, actualText);
    }
}
