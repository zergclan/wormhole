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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Util tools of map.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Maps {
    
    /**
     * New {@link HashMap}.
     *
     * @param initialCapacity initial capacity
     * @param loadFactor load factor
     * @param <K> class type of key
     * @param <V> class type of value
     * @return {@link HashMap}
     */
    public static <K, V> Map<K, V> newHashMap(final int initialCapacity, final float loadFactor) {
        return new HashMap<>(initialCapacity, loadFactor);
    }
    
    /**
     * New {@link LinkedHashMap}.
     *
     * @param initialCapacity initial capacity
     * @param loadFactor load factor
     * @param <K> class type of key
     * @param <V> class type of value
     * @return {@link LinkedHashMap}
     */
    public static <K, V> Map<K, V> newLinkedHashMap(final int initialCapacity, final float loadFactor) {
        return new LinkedHashMap<>(initialCapacity, loadFactor);
    }
}
