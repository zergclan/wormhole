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

package com.zergclan.wormhole.tool.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Util tools for Validator.
 * <p>
 *     Mainly for internal use within the framework,
 *     referenced {@code org.apache.commons.lang3.Validate} from
 *     <a href="https://commons.apache.org/proper/commons-lang/">Apache Commons Lang</a>
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validator {
    
    /**
     * Validate not null.
     *
     * @param object object
     * @param message error message
     * @param args args
     * @param <T> class type of object
     */
    public static <T> void notNull(final T object, final String message, final Object... args) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }
    
    /**
     * Validate not null.
     *
     * @param input input
     * @param message error message
     * @param args args
     */
    public static void notBlank(final String input, final String message, final Object... args) {
        if (StringUtil.isBlank(input)) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }
    
    /**
     * Validate not empty.
     *
     * @param input input
     * @param message message
     * @param args args
     */
    public static void notEmpty(final Collection<?> input, final String message, final Object... args) {
        notNull(input, "error : arg input can not be null.");
        if (input.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }
    
    /**
     * Validate not empty.
     *
     * @param input input
     * @param message message
     * @param args args
     */
    public static void notEmpty(final Map<?, ?> input, final String message, final Object... args) {
        notNull(input, "error : arg input can not be null.");
        if (input.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }
    
    /**
     * Validate pre state.
     *
     * @param expression expression
     * @param message error message
     * @param args error args
     */
    public static void preState(final boolean expression, final String message, final Object... args) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }
}
