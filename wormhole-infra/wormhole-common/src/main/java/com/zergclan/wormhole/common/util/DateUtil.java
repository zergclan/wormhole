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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Util tools for Date.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {
    
    /**
     * Get current time millis.
     *
     * @return current time millis
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Format {@link Date}.
     *
     * @param date {@link Date}
     * @param pattern pattern
     * @return format date text
     */
    public static String format(final Date date, final String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * Parse date text to {@link Date}.
     *
     * @param text date text
     * @param pattern pattern
     * @return {@link Date}
     */
    public static Date parse(final String text, final String pattern) {
        return new SimpleDateFormat(pattern).parse(text, new ParsePosition(0));
    }
}
