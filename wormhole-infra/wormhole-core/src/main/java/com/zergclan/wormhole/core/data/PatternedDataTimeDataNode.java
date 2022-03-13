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

package com.zergclan.wormhole.core.data;

import com.zergclan.wormhole.core.api.data.DataNode;
import lombok.Getter;

/**
 * Data node type of {@link PatternedDataTime}.
 */
@Getter
public final class PatternedDataTimeDataNode implements DataNode<PatternedDataTime> {

    private static final long serialVersionUID = -4571978987948014231L;

    private String name;

    private PatternedDataTime value;

    public PatternedDataTimeDataNode(final String name, final PatternedDataTime value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean refreshName(final String name) {
        this.name = name;
        return false;
    }

    @Override
    public boolean refreshValue(final PatternedDataTime value) {
        this.value = value;
        return false;
    }
}
