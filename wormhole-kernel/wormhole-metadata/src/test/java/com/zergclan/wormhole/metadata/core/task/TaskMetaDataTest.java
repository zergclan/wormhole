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

package com.zergclan.wormhole.metadata.core.task;

import com.zergclan.wormhole.metadata.core.filter.FilterMetaData;
import com.zergclan.wormhole.metadata.core.node.DataNodeMetaData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TaskMetaDataTest {
    
    private static TaskMetaData metaData;
    
    @BeforeAll
    public static void init() {
        String identifier = "taskIdentifier";
        int order = 0;
        int batchSize = 1000;
        SourceMetaData source = createSource();
        TargetMetaData target = createTarget();
        Collection<FilterMetaData> filters = new LinkedList<>();
        metaData = new TaskMetaData(identifier, order, batchSize, source, target, filters);
    }
    
    private static TargetMetaData createTarget() {
        String dataSourceIdentifier = "dataSourceIdentifier";
        String table = "table";
        Collection<String> uniqueNodes = new LinkedList<>();
        Collection<String> compareNodes = new LinkedList<>();
        Collection<String> ignoreNodes = new LinkedList<>();
        String versionNode = "taskBatch";
        Map<String, DataNodeMetaData> dataNodes = new LinkedHashMap<>();
        return new TargetMetaData(dataSourceIdentifier, table, uniqueNodes, compareNodes, ignoreNodes, versionNode, dataNodes);
    }
    
    private static SourceMetaData createSource() {
        String dataSourceIdentifier = "dataSourceIdentifier";
        String actualSql = "select * from table";
        String table = "table";
        String conditionSql = "id = 1";
        Map<String, DataNodeMetaData> dataNodes = new LinkedHashMap<>();
        return new SourceMetaData(dataSourceIdentifier, actualSql, table, conditionSql, dataNodes);
    }
    
    @Test
    public void assertGetIdentifier() {
        assertEquals("taskIdentifier", metaData.getIdentifier());
    }
}
