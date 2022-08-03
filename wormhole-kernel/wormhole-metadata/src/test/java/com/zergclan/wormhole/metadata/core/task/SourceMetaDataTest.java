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

import com.zergclan.wormhole.metadata.core.plan.node.DataNodeMetaData;
import com.zergclan.wormhole.metadata.core.plan.SourceMetaData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SourceMetaDataTest {
    
    private static SourceMetaData metaData;
    
    @BeforeAll
    public static void init() {
        String dataSourceIdentifier = "dataSourceIdentifier";
        String actualSql = "select * from table";
        String table = "table";
        String conditionSql = "id = 1";
        Map<String, DataNodeMetaData> dataNodes = new LinkedHashMap<>();
        metaData = new SourceMetaData(dataSourceIdentifier, actualSql, table, conditionSql, dataNodes);
    }
    
    @Test
    public void assertGetIdentifier() {
        assertEquals("dataSourceIdentifier#table", metaData.getIdentifier());
    }
}
