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

package com.zergclan.wormhole.common.metadata.datasource;

import com.zergclan.wormhole.common.metadata.plan.TargetMetaData;
import com.zergclan.wormhole.common.metadata.plan.node.DataNodeMetaData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TargetMetaDataTest {
    
    private static TargetMetaData metaData;
    
    @BeforeAll
    public static void init() {
        String dataSourceIdentifier = "dataSourceIdentifier";
        String table = "table";
        Map<String, DataNodeMetaData> dataNodes = new LinkedHashMap<>();
        Collection<String> uniqueNodes = new LinkedList<>();
        Collection<String> compareNodes = new LinkedList<>();
        Collection<String> ignoreNodes = new LinkedList<>();
        String versionNode = "taskBatch";
        metaData = new TargetMetaData(dataSourceIdentifier, table, compareNodes, uniqueNodes, ignoreNodes, versionNode, dataNodes);
    }
    
    @Test
    public void assertGetIdentifier() {
        assertEquals("dataSourceIdentifier#table", metaData.getIdentifier());
    }
}
