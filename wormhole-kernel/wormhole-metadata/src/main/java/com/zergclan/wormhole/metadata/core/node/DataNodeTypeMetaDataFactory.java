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

import com.zergclan.wormhole.metadata.core.resource.ColumnMetaData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Simple factory for create {@link DataNodeTypeMetaData}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataNodeTypeMetaDataFactory {
    
    /**
     * Create objected {@link DataNodeMetaData}.
     *
     * @param columnMetaData {@link ColumnMetaData}
     * @return {@link DataNodeMetaData}
     */
    public static DataNodeMetaData createObjectDataType(final ColumnMetaData columnMetaData) {
        DataNodeTypeMetaData dataNodeType = new DataNodeTypeMetaData(DataNodeTypeMetaData.initNodeType(columnMetaData), DataNodeTypeMetaData.DataType.OBJECT);
        return new DataNodeMetaData(columnMetaData.getName(), columnMetaData.getTable(), dataNodeType, columnMetaData.getDefaultValue());
    }
    
    /**
     * Create {@link DataNodeMetaData}.
     *
     * @param columnMetaData {@link ColumnMetaData}
     * @return {@link DataNodeMetaData}
     */
    public static DataNodeMetaData createDataType(final ColumnMetaData columnMetaData) {
        DataNodeTypeMetaData dataNodeType = new DataNodeTypeMetaData(columnMetaData);
        return new DataNodeMetaData(columnMetaData.getName(), columnMetaData.getTable(), dataNodeType, columnMetaData.getDefaultValue());
    }
}
