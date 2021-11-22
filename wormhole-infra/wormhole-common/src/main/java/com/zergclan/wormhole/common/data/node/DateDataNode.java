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

package com.zergclan.wormhole.common.data.node;

import com.zergclan.wormhole.common.data.node.type.DataNodeType;
import com.zergclan.wormhole.common.data.node.type.DatePatternType;
import lombok.RequiredArgsConstructor;

/**
 * Data node type of date.
 */
@RequiredArgsConstructor
public final class DateDataNode implements WormholeDataNode {
    
    private static final long serialVersionUID = -8351606898344714382L;
    
    private final String column;
    
    private final String value;
    
    private final DataNodeType dataNodeType;
    
    private final DatePatternType pattern;
    
    private final String comment;
}
