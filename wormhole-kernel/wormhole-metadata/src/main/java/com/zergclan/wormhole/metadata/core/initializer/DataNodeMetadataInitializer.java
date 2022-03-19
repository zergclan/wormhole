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

package com.zergclan.wormhole.metadata.core.initializer;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.config.core.DataNodeConfiguration;
import com.zergclan.wormhole.metadata.core.node.DataNodeMetaData;
import com.zergclan.wormhole.metadata.core.node.DataNodeTypeMetaData;
import com.zergclan.wormhole.metadata.core.resource.ColumnMetaData;

import java.util.Optional;

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
     * Init defaulted {@link DataNodeMetaData}.
     *
     * @param columnMetadata {@link ColumnMetaData}
     * @return {@link DataNodeMetaData}
     */
    public DataNodeMetaData init(final ColumnMetaData columnMetadata) {
        String name = columnMetadata.getName();
        String tableName = columnMetadata.getSchema() + MarkConstant.POINT + columnMetadata.getTable();
        DataNodeTypeMetaData type = new DataNodeTypeMetaData(columnMetadata);
        Optional<String> defaultValue = columnMetadata.getDefaultValue();
        return defaultValue.map(value -> new DataNodeMetaData(name, tableName, type, value)).orElseGet(() -> new DataNodeMetaData(name, tableName, type));
    }
}
