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

package com.zergclan.wormhole.test.integration.framework.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings("all")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomGenerator {
    
    /**
     * Generate random int equal probability in interval [0, Integer.MAX_VALUE).
     *
     * @return random int
     */
    public static int randomInt() {
        return random(0, Integer.MAX_VALUE);
    }
    
    /**
     * Generate random long equal probability in interval [0, Long.MAX_VALUE).
     *
     * @return random long
     */
    public static long randomLong() {
        return random(0L, Long.MAX_VALUE);
    }
    
    /**
     * Generate random int equal probability in interval [min, max).
     *
     * @param min min
     * @param max max
     * @return random int value
     */
    public static int random(final int min, final int max) {
        return randomFactor(max - min) + min;
    }
    
    /**
     * Generate random long equal probability in interval [min, max).
     *
     * @param min min
     * @param max max
     * @return random long value
     */
    public static long random(final long min, final long max) {
        return randomFactor(max - min) + min;
    }
    
    /**
     * Generate factor for random int.
     *
     * @param max max
     * @return random factor
     */
    public static int randomFactor(final int max) {
        return (int) (Math.random() * max);
    }
    
    /**
     * Generate factor for random long.
     *
     * @param max max
     * @return random factor
     */
    public static long randomFactor(final long max) {
        return (long) (Math.random() * max);
    }
}
