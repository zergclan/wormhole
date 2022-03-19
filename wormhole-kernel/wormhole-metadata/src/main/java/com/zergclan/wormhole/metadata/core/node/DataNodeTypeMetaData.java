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

package com.zergclan.wormhole.metadata.core.node;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.metadata.api.MetaData;
import com.zergclan.wormhole.metadata.core.resource.ColumnMetaData;
import lombok.Getter;

import java.util.Locale;
import java.util.Optional;

@Getter
public final class DataNodeTypeMetaData implements MetaData {

    private final NodeType nodeType;

    private final DataType dataType;

    public DataNodeTypeMetaData(final ColumnMetaData columnMetadata) {
        Optional<String> defaultValue = columnMetadata.getDefaultValue();
        NodeType nodeType = NodeType.STANDARD;
        if (columnMetadata.isNullable()) {
            if (defaultValue.isPresent()) {
                nodeType = NodeType.DEFAULT_ABLE;
            }
        } else {
            nodeType = NodeType.REQUIRED;
        }
        this.nodeType = nodeType;
        this.dataType = DataType.valueOf(columnMetadata.getDataType());
    }

    public DataNodeTypeMetaData(final String nodeTypeValue, final String dataTypeValue) {
        this.nodeType = NodeType.valueOf(nodeTypeValue.toUpperCase(Locale.ROOT));
        this.dataType = DataType.valueOf(dataTypeValue.toUpperCase(Locale.ROOT));
    }

    @Override
    public String getIdentifier() {
        return nodeType.name() + MarkConstant.COLON + dataType.name();
    }

    /**
     * Node type.
     */
    public enum NodeType {

        REQUIRED, DEFAULT_ABLE, STANDARD, FIXED, MAPPED
    }

    /**
     * Data type.
     */
    public enum DataType {

        OBJECT, TEXT, INT, LONG, MONETARY, DATA_TIME, PATTERNED_DATA_TIME, CODE
    }
}
