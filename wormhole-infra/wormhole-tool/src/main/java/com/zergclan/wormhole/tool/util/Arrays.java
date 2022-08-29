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

/**
 * Util tools for array.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Arrays {

    private static final int DEFAULT_START_INDEX = 0;

    /**
     * Array contains.
     *
     * @param array array
     * @param toFind to find
     * @return contains or not
     */
    public static boolean contains(final Object[] array, final Object toFind) {
        return -1 != indexOf(array, toFind, DEFAULT_START_INDEX);
    }

    private static <T> int indexOf(final T[] array, final T toFind, final int startIndex) {
        if (array == null) {
            return -1;
        }
        Validator.preState(-1 < startIndex, "error: Arrays indexOf arg startIndex can not less than zero. {%d}", startIndex);
        int length = array.length;
        int i;
        if (null == toFind) {
            for (i = startIndex; i < length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
            return -1;
        }
        for (i = startIndex; i < length; ++i) {
            if (toFind.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }
}
