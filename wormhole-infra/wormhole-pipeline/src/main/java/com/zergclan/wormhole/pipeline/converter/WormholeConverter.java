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

package com.zergclan.wormhole.pipeline.converter;

import com.zergclan.wormhole.common.data.node.WormholeDataNode;
import com.zergclan.wormhole.pipeline.WormholeFilter;

/**
 * The root interface from which all data node converter objects shall be derived in Wormhole.
 *
 * @param <S> class type of source data node
 * @param <T> class type of target data node
 */
public interface WormholeConverter<S extends WormholeDataNode<?>, T extends WormholeDataNode<?>> extends WormholeFilter {
    
    /**
     * Convert data node.
     *
     * @param dataNode source data node
     * @return target data node
     */
    T convert(S dataNode);
}
