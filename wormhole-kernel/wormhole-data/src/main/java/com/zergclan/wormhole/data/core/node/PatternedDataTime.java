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

package com.zergclan.wormhole.data.core.node;

import com.zergclan.wormhole.common.util.DateUtil;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Patterned data time.
 */
@Getter
public final class PatternedDataTime implements Serializable {

    private static final long serialVersionUID = 6418454456243094323L;

    private final String value;

    private final DatePattern pattern;

    public PatternedDataTime(final String value, final String pattern) {
        this.value = value;
        this.pattern = DatePattern.valueOf(pattern);
    }
    
    public PatternedDataTime(final LocalDateTime localDateTime, final DatePattern pattern) {
        this.pattern = pattern;
        this.value = pattern.format(localDateTime);
    }
    
    @Getter
    public enum DatePattern {

        STANDARD("yyyy-MM-dd HH:mm:ss"),
        DATE("yyyy-MM-dd"),
        TIME("HH:mm:ss");

        private final String pattern;
        
        DatePattern(final String pattern) {
            this.pattern = pattern;
        }
        
        private String format(final LocalDateTime localDateTime) {
            return DateUtil.format(localDateTime, pattern);
        }
    }
}
