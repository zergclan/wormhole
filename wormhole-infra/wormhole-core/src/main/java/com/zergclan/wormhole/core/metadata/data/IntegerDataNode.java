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

package com.zergclan.wormhole.core.metadata.data;

import com.zergclan.wormhole.core.metadata.DataNode;
import lombok.RequiredArgsConstructor;

/**
 * Data node type of {@link Integer}.
 */
@RequiredArgsConstructor
public final class IntegerDataNode implements DataNode<Integer> {
    
    private static final long serialVersionUID = -9112425094443804066L;
    
    private final String name;
    
    private Integer value;
    
    @Override
    public Integer getValue() {
        return value;
    }
    
    @Override
    public DataNode<Integer> refresh(final Integer value) {
        this.value = value;
        return this;
    }
}
