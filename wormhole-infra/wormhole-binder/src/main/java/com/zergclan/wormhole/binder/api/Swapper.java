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

package com.zergclan.wormhole.binder.api;

/**
 * The root interface from which all swapper objects shall be derived in Wormhole.
 *
 * @param <S> class type of source
 * @param <T> class type of target
 */
public interface Swapper<S, T> {
    
    /**
     * Swap source to target.
     *
     * @param source source
     * @return target
     */
    T swapToTarget(S source);
    
    /**
     * Swap target to source.
     *
     * @param target target
     * @return source
     */
    S swapToSource(T target);
}
