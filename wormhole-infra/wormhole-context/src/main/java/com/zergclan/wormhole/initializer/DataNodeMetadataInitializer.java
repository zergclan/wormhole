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

package com.zergclan.wormhole.initializer;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.core.config.DataNodeConfiguration;
import com.zergclan.wormhole.core.metadata.node.DataNodeMetadata;
import com.zergclan.wormhole.core.metadata.node.DataNodeTypeMetadata;
import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;

import java.util.Optional;

/**
 * Data node metadata initializer.
 */
public final class DataNodeMetadataInitializer {
    
    /**
     * Init {@link DataNodeMetadata}.
     *
     * @param nodeName node name
     * @param configuration {@link DataNodeConfiguration}
     * @return {@link DataNodeMetadata}
     */
    public DataNodeMetadata init(final String nodeName, final DataNodeConfiguration configuration) {
        String tableName = configuration.getTable();
        String defaultValue = configuration.getDefaultValue();
        DataNodeTypeMetadata dataNodeTypeMetadata = new DataNodeTypeMetadata(configuration.getNodeType(), configuration.getDataType());
        return new DataNodeMetadata(nodeName, tableName, dataNodeTypeMetadata, defaultValue);
    }
    
    /**
     * Init defaulted {@link DataNodeMetadata}.
     *
     * @param columnMetadata {@link ColumnMetadata}
     * @return {@link DataNodeMetadata}
     */
    public DataNodeMetadata init(final ColumnMetadata columnMetadata) {
        String name = columnMetadata.getName();
        String tableName = columnMetadata.getSchema() + MarkConstant.POINT + columnMetadata.getTable();
        DataNodeTypeMetadata type = new DataNodeTypeMetadata(columnMetadata);
        Optional<String> defaultValue = columnMetadata.getDefaultValue();
        return defaultValue.map(value -> new DataNodeMetadata(name, tableName, type, value)).orElseGet(() -> new DataNodeMetadata(name, tableName, type));
    }
}
