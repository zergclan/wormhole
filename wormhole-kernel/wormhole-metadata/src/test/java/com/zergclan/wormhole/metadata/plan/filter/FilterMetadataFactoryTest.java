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

package com.zergclan.wormhole.metadata.plan.filter;

import com.zergclan.wormhole.common.configuration.FilterConfiguration;
import com.zergclan.wormhole.metadata.plan.filter.precise.editor.NullToDefaultEditorMetaData;
import com.zergclan.wormhole.metadata.plan.filter.precise.validator.NotBlankValidatorMetaData;
import com.zergclan.wormhole.metadata.plan.filter.precise.validator.NotNullValidatorMetaData;
import com.zergclan.wormhole.metadata.plan.node.DataNodeMetaData;
import com.zergclan.wormhole.metadata.plan.node.DataNodeTypeMetaData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

/**
 * FilterMetadataFactoryTest.
 */
public final class FilterMetadataFactoryTest {

    /**
     * Task id.
     */
    private final String taskIdentifier = "testTask";

    @Test
    public void assertGetDefaultInstance() {
        DataNodeTypeMetaData sourceDataType = new DataNodeTypeMetaData(DataNodeTypeMetaData.NodeType.STANDARD, DataNodeTypeMetaData.DataType.CODE);
        DataNodeMetaData finalSourceDataNode = new DataNodeMetaData("testTable", "SOURCE_TEST", sourceDataType);
        // 1、Test target node type can not be fixed and mapped.
        assertTargetNodeType(finalSourceDataNode);
        // 2、Test target is required and source is standard.
        assertTargetReAndSourceStan();
        // 3、Test target is default able and source is standard.
        assertTargetDeAndSourceStan(finalSourceDataNode);
    }

    private void assertTargetNodeType(final DataNodeMetaData finalSourceDataNode) {
        DataNodeTypeMetaData targetDataType = new DataNodeTypeMetaData(DataNodeTypeMetaData.NodeType.FIXED, DataNodeTypeMetaData.DataType.TEXT);
        DataNodeMetaData fixedTargetDataNode = new DataNodeMetaData("testTable", "TARGET_TEST", targetDataType);
        Assertions.assertThrows(IllegalArgumentException.class, () -> FilterMetadataFactory.getDefaultInstance(taskIdentifier, finalSourceDataNode, fixedTargetDataNode));

        targetDataType = new DataNodeTypeMetaData(DataNodeTypeMetaData.NodeType.MAPPED, DataNodeTypeMetaData.DataType.TEXT);
        DataNodeMetaData mappedTargetDataNode = new DataNodeMetaData("testTable", "TARGET_TEST", targetDataType);
        Assertions.assertThrows(IllegalArgumentException.class, () -> FilterMetadataFactory.getDefaultInstance(taskIdentifier, finalSourceDataNode, mappedTargetDataNode));

    }

    private void assertTargetReAndSourceStan() {
        DataNodeTypeMetaData targetDataType = new DataNodeTypeMetaData(DataNodeTypeMetaData.NodeType.REQUIRED, DataNodeTypeMetaData.DataType.DATA_TIME);
        DataNodeMetaData targetDataNode = new DataNodeMetaData("testTable", "TARGET_TEST", targetDataType);
        DataNodeTypeMetaData sourceDataType = new DataNodeTypeMetaData(DataNodeTypeMetaData.NodeType.STANDARD, DataNodeTypeMetaData.DataType.CODE);
        DataNodeMetaData sourceDataNode = new DataNodeMetaData("testTable", "SOURCE_TEST", sourceDataType);
        Assertions.assertEquals(2, FilterMetadataFactory.getDefaultInstance(taskIdentifier, sourceDataNode, targetDataNode).size());
    }

    private void assertTargetDeAndSourceStan(final DataNodeMetaData finalSourceDataNode) {
        DataNodeTypeMetaData targetDataType = new DataNodeTypeMetaData(DataNodeTypeMetaData.NodeType.DEFAULT_ABLE, DataNodeTypeMetaData.DataType.TEXT);
        DataNodeMetaData defaultTargetDataNode = new DataNodeMetaData("testTable", "TARGET_TEST", targetDataType);
        Assertions.assertThrows(IllegalArgumentException.class, () -> FilterMetadataFactory.getDefaultInstance(taskIdentifier, finalSourceDataNode, defaultTargetDataNode));
        DataNodeMetaData defaultTargetDataNodeT = new DataNodeMetaData("testTable", "TARGET_TEST", targetDataType, "mark");
        Assertions.assertEquals(2, FilterMetadataFactory.getDefaultInstance(taskIdentifier, finalSourceDataNode, defaultTargetDataNodeT).size());
    }

    @Test
    public void assertGetInstance() {
        // 1、Test not null filter.
        assertFilterNotNull();
        // 2、Test not blank.
        assertFilterNotBlank();
        // 3、Test null to default.
        assetNullToDefaultFilter();
    }

    private void assertFilterNotNull() {
        Properties properties = new Properties();
        FilterConfiguration notNullFilterConfiguration = new FilterConfiguration("not_null", 1, properties);
        Assertions.assertThrows(IllegalArgumentException.class, () -> FilterMetadataFactory.getInstance(taskIdentifier, notNullFilterConfiguration));
        properties.setProperty("sourceName", "name");
        Assertions.assertInstanceOf(NotNullValidatorMetaData.class, FilterMetadataFactory.getInstance(taskIdentifier, notNullFilterConfiguration));
    }

    private void assertFilterNotBlank() {
        Properties properties = new Properties();
        FilterConfiguration notBlankFilterConfiguration = new FilterConfiguration("not_blank", 1, properties);
        Assertions.assertThrows(IllegalArgumentException.class, () -> FilterMetadataFactory.getInstance(taskIdentifier, notBlankFilterConfiguration));
        properties.setProperty("sourceName", "name");
        Assertions.assertInstanceOf(NotBlankValidatorMetaData.class, FilterMetadataFactory.getInstance(taskIdentifier, notBlankFilterConfiguration));
    }

    private void assetNullToDefaultFilter() {
        Properties properties = new Properties();
        FilterConfiguration nullToDefaultFilterConfiguration = new FilterConfiguration("null_to_default", 1, properties);
        Assertions.assertThrows(IllegalArgumentException.class, () -> FilterMetadataFactory.getInstance(taskIdentifier, nullToDefaultFilterConfiguration));
        properties.setProperty("sourceName", "name");
        Assertions.assertThrows(IllegalArgumentException.class, () -> FilterMetadataFactory.getInstance(taskIdentifier, nullToDefaultFilterConfiguration));
        properties.setProperty("defaultValue", "tyron");
        Assertions.assertThrows(IllegalArgumentException.class, () -> FilterMetadataFactory.getInstance(taskIdentifier, nullToDefaultFilterConfiguration));
        properties.setProperty("dataType", "TEXT");
        Assertions.assertInstanceOf(NullToDefaultEditorMetaData.class, FilterMetadataFactory.getInstance(taskIdentifier, nullToDefaultFilterConfiguration));
    }

}
