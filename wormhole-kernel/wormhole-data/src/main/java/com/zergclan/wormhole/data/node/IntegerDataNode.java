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

package com.zergclan.wormhole.data.node;

import lombok.Getter;

/**
 * Data node type of {@link Integer}.
 */
@Getter
public final class IntegerDataNode implements DataNode<Integer> {

    private static final long serialVersionUID = -1407715713577362674L;

    private String name;
    
    private Integer value;

    public IntegerDataNode(final String name, final Integer value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean refreshName(final String name) {
        this.name = name;
        return true;
    }
    
    @Override
    public DataNode<Integer> cloneNode() {
        return new IntegerDataNode(name, value);
    }
}
