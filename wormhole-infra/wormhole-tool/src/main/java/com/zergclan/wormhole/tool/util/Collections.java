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

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Util tools of collection.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Collections {
    
    /**
     * New linked hash set.
     *
     * @param elements elements
     * @param <E> class type of element
     * @return linked hash set
     */
    public static <E> Set<E> newLinkedHashSet(final E[] elements) {
        int length = elements.length;
        Set<E> result = new LinkedHashSet<>(length, 1);
        for (int i = 0; i < length; i++) {
            result.add(elements[i]);
        }
        return result;
    }
}
