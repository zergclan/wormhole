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

package com.zergclan.wormhole.metadata.core.filter;

import com.zergclan.wormhole.metadata.core.node.DataNodeMetaData;
import com.zergclan.wormhole.metadata.core.node.DataNodeTypeMetaData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

/**
 * FilterMetadataFactoryTest.
 */
public final class FilterMetadataFactoryTest {

    @Test
    public void assertGetDefaultInstance() {
        String taskIdentifier = "testTask";
        DataNodeTypeMetaData sourceDataType = new DataNodeTypeMetaData(DataNodeTypeMetaData.NodeType.REQUIRED, DataNodeTypeMetaData.DataType.CODE);
        DataNodeMetaData sourceDataNode = new DataNodeMetaData("测试表", "SOURCE_TEST", sourceDataType);
        DataNodeTypeMetaData targetDataType = new DataNodeTypeMetaData(DataNodeTypeMetaData.NodeType.REQUIRED, DataNodeTypeMetaData.DataType.TEXT);
        DataNodeMetaData targetDataNode = new DataNodeMetaData("测试表", "TARGET_TEST", targetDataType);
        Collection<FilterMetaData> defaultInstance = FilterMetadataFactory.getDefaultInstance(taskIdentifier, sourceDataNode, targetDataNode);
        Assertions.assertEquals(1, defaultInstance.size());
    }

}
