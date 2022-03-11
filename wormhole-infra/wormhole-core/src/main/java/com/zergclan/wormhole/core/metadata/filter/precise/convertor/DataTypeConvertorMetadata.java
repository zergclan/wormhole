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

package com.zergclan.wormhole.core.metadata.filter.precise.convertor;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.core.metadata.filter.FilterMetadata;
import com.zergclan.wormhole.core.metadata.filter.FilterType;
import com.zergclan.wormhole.core.metadata.node.DataNodeTypeMetadata;
import lombok.RequiredArgsConstructor;

/**
 * Business code convertor implemented of {@link FilterMetadata}.
 */
@RequiredArgsConstructor
public final class DataTypeConvertorMetadata implements FilterMetadata {

    private static final FilterType FILTER_TYPE = FilterType.DATA_TYPE_CONVERTOR;

    private final String taskIdentifier;

    private final int order;

    @Override
    public String getIdentifier() {
        return taskIdentifier + MarkConstant.SPACE + FILTER_TYPE.name() + MarkConstant.SPACE + order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public FilterType getType() {
        return FILTER_TYPE;
    }

    /**
     * Builder for {@link DataTypeConvertorMetadata}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param targetDataType target {@link DataNodeTypeMetadata.DataType}
     * @param sourceDataType source {@link DataNodeTypeMetadata.DataType}
     * @return {@link DataTypeConvertorMetadata}
     */
    public static DataTypeConvertorMetadata builder(final String taskIdentifier, final int order, final DataNodeTypeMetadata.DataType targetDataType,
                                                    final DataNodeTypeMetadata.DataType sourceDataType) {
        // TODO create by types of TEXT, NUMERIC, MONETARY, DATETIME
        return null;
    }
}
