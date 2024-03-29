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

package com.zergclan.wormhole.common.metadata.plan;

import com.zergclan.wormhole.common.WormholeMetaData;
import com.zergclan.wormhole.common.metadata.plan.node.DataNodeMetaData;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;

/**
 * Target metadata.
 */
@RequiredArgsConstructor
@Getter
public final class TargetMetaData implements WormholeMetaData {
    
    private final String dataSourceIdentifier;

    private final String table;
    
    private final Collection<String> compareNodes;
    
    private final Collection<String> uniqueNodes;
    
    private final Collection<String> ignoreNodes;
    
    private final String versionNode;
    
    private final Map<String, DataNodeMetaData> dataNodes;
    
    @Override
    public String getIdentifier() {
        return dataSourceIdentifier + MarkConstant.SPACE + table;
    }
}
