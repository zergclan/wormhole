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

package com.zergclan.wormhole.tool.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Quote character.
 */
@RequiredArgsConstructor
@Getter
public enum QuoteCharacter {
    
    BACK_QUOTE(MarkConstant.BACK_QUOTE, MarkConstant.BACK_QUOTE),
    
    SINGLE_QUOTE(MarkConstant.SINGLE_QUOTE, MarkConstant.SINGLE_QUOTE),
    
    QUOTE(MarkConstant.QUOTE, MarkConstant.QUOTE),
    
    BRACKETS(MarkConstant.LEFT_BRACKETS, MarkConstant.RIGHT_BRACKETS),
    
    PARENTHESES(MarkConstant.LEFT_PARENTHESIS, MarkConstant.RIGHT_PARENTHESIS),
    
    NONE("", "");
    
    private final String startDelimiter;
    
    private final String endDelimiter;
    
    /**
     * Wrap value with quote character.
     *
     * @param value value to be wrapped
     * @return wrapped value
     */
    public String wrap(final String value) {
        return startDelimiter + value + endDelimiter;
    }
}
