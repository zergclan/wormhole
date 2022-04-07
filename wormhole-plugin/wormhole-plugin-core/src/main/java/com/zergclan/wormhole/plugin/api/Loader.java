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

package com.zergclan.wormhole.plugin.api;

import com.zergclan.wormhole.common.spi.typed.TypedSPI;
import com.zergclan.wormhole.metadata.core.catched.CachedTargetMetaData;

/**
 * The root interface from which all loader shall be derived in Wormhole.
 *
 * @param <D> class type of data
 * @param <V> class type of result
 */
public interface Loader<D, V> extends TypedSPI {
    
    /**
     * Init.
     *
     * @param cachedTarget {@link CachedTargetMetaData}
     */
    void init(CachedTargetMetaData cachedTarget);
    
    /**
     * Load.
     *
     * @param data data
     * @return result
     */
    V load(D data);
}
