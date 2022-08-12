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

package com.zergclan.wormhole.metadata.initializer;

import com.zergclan.wormhole.common.configuration.DataNodeConfiguration;
import com.zergclan.wormhole.metadata.plan.node.DataNodeMetaData;
import com.zergclan.wormhole.metadata.plan.node.DataNodeTypeMetaData;
import com.zergclan.wormhole.metadata.plan.node.DataNodeTypeMetaDataFactory;
import com.zergclan.wormhole.metadata.datasource.ColumnMetaData;

import java.util.Objects;

/**
 * Data node metadata initializer.
 */
public final class DataNodeMetadataInitializer {
    
    /**
     * Init {@link DataNodeMetaData}.
     *
     * @param nodeName node name
     * @param configuration {@link DataNodeConfiguration}
     * @return {@link DataNodeMetaData}
     */
    public DataNodeMetaData init(final String nodeName, final DataNodeConfiguration configuration) {
        String tableName = configuration.getTable();
        String defaultValue = configuration.getDefaultValue();
        DataNodeTypeMetaData dataNodeTypeMetadata = new DataNodeTypeMetaData(configuration.getNodeType(), configuration.getDataType());
        return new DataNodeMetaData(nodeName, tableName, dataNodeTypeMetadata, defaultValue);
    }
    
    /**
     * Init default target source data nodes.
     *
     * @param targetColumn {@link ColumnMetaData}
     * @param sourceColumn {@link ColumnMetaData}
     * @return default target source data nodes
     */
    public DataNodeMetaData[] initDefaultTargetSourceDataNodes(final ColumnMetaData targetColumn, final ColumnMetaData sourceColumn) {
        DataNodeMetaData[] result = new DataNodeMetaData[2];
        if (isSameDataNodeType(targetColumn, sourceColumn)) {
            result[0] = DataNodeTypeMetaDataFactory.createObjectDataType(targetColumn);
            result[1] = DataNodeTypeMetaDataFactory.createObjectDataType(sourceColumn);
            return result;
        }
        result[0] = DataNodeTypeMetaDataFactory.createDataType(targetColumn);
        result[1] = DataNodeTypeMetaDataFactory.createDataType(sourceColumn);
        return result;
    }
    
    private boolean isSameDataNodeType(final ColumnMetaData targetColumn, final ColumnMetaData sourceColumn) {
        boolean isSameType = Objects.equals(targetColumn.getDataType(), sourceColumn.getDataType());
        boolean isSameNullable = Objects.equals(targetColumn.isNullable(), sourceColumn.isNullable());
        boolean isSameDefaultValue = Objects.equals(targetColumn.getDefaultValue(), sourceColumn.getDefaultValue());
        return isSameType && isSameNullable && isSameDefaultValue;
    }
}
