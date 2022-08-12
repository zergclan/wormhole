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

package com.zergclan.wormhole.data.node;

import com.zergclan.wormhole.common.exception.WormholeException;
import com.zergclan.wormhole.common.util.DateUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Patterned data time.
 */
@RequiredArgsConstructor
@Getter
public final class PatternedDataTime implements Serializable {
    
    private static final long serialVersionUID = 6418454456243094323L;
    
    private final Date value;
    
    private final DatePattern pattern;
    
    public PatternedDataTime(final String value) {
        this(value, DatePattern.STANDARD);
    }
    
    public PatternedDataTime(final String value, final String pattern) {
        this(value, DatePattern.valueOfPattern(pattern));
    }
    
    public PatternedDataTime(final String value, final DatePattern pattern) {
        this(pattern.parseDate(value), pattern);
    }
    
    /**
     * Get patterned value.
     *
     * @return patterned value
     */
    public String getPatternedValue() {
        return DateUtil.format(value, pattern.getPattern());
    }
    
    @Getter
    public enum DatePattern {
        // TODO add source date pattern
        STANDARD("yyyy-MM-dd HH:mm:ss"),
        DATE("yyyy-MM-dd"),
        TIME("HH:mm:ss"),
        FORWARD_SLASH_DATE("yyyy/MM/dd");
        
        private final String pattern;
        
        DatePattern(final String pattern) {
            this.pattern = pattern;
        }
    
        private Date parseDate(final String value) {
            return DateUtil.parseDate(value, getPattern());
        }
        
        /**
         * Value of pattern.
         *
         * @param pattern pattern
         * @return {@link DatePattern}
         */
        public static DatePattern valueOfPattern(final String pattern) {
            if ("yyyy-MM-dd HH:mm:ss".equals(pattern) || "STANDARD".equalsIgnoreCase(pattern)) {
                return STANDARD;
            }
            if ("yyyy-MM-dd".equals(pattern) || "DATE".equalsIgnoreCase(pattern)) {
                return DATE;
            }
            if ("HH:mm:ss".equals(pattern) || "TIME".equalsIgnoreCase(pattern)) {
                return TIME;
            }
            if ("yyyy/MM/dd".equals(pattern) || "FORWARD_SLASH_DATE".equalsIgnoreCase(pattern)) {
                return FORWARD_SLASH_DATE;
            }
            throw new WormholeException("error : No enum constant in PatternedDataTime.DatePattern value of [%s]", pattern);
        }
    }
}
