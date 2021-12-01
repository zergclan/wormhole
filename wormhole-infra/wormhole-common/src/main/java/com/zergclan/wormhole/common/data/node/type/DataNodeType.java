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

package com.zergclan.wormhole.common.data.node.type;

import lombok.Getter;

/**
 * Data node type.
 */
@Getter
public enum DataNodeType {
    
    /**
     * Native type of data node.
     */
    NATIVE(0, "native"),
    
    /**
     * Not null type of data node.
     */
    NOT_NULL(1, "not null"),
    
    /**
     * Have default value type of data node.
     */
    HAVE_DEFAULT_VALUE(2, "have default value"),
    
    /**
     * Unique index type of data node.
     */
    UNIQUE_INDEX(4, "unique index"),
    
    /**
     * Primary key type of data node.
     */
    PRIMARY_KEY(5, "primary key");
    
    private final int code;
    
    private final String name;
    
    DataNodeType(final int code, final String name) {
        this.code = code;
        this.name = name;
    }
}
