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

package com.zergclan.wormhole.common.metadata.plan.filter.precise.convertor;

import com.zergclan.wormhole.common.metadata.plan.filter.FilterMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.common.metadata.plan.node.DataType;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.tool.util.ValueExtractor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Properties;

/**
 * Data type convertor implemented of {@link FilterMetaData}.
 */
@RequiredArgsConstructor
@Getter
public final class DataTypeConvertorMetaData implements FilterMetaData {

    private static final FilterType FILTER_TYPE = FilterType.DATA_TYPE_CONVERTOR;

    private final String taskIdentifier;

    private final int order;
    
    private final String sourceName;
    
    private final DataType sourceDataType;
    
    private final DataType targetDataType;
    
    private final String pattern;

    @Override
    public String getIdentifier() {
        return taskIdentifier + MarkConstant.SPACE + FILTER_TYPE.name() + MarkConstant.SPACE + order;
    }

    @Override
    public FilterType getType() {
        return FILTER_TYPE;
    }

    /**
     * Builder for {@link DataTypeConvertorMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param props props
     * @return {@link FilterMetaData}
     */
    public static DataTypeConvertorMetaData builder(final String taskIdentifier, final int order, final Properties props) {
        String sourceName = ValueExtractor.extractRequiredString(props, "sourceName");
        String sourceDataType = ValueExtractor.extractRequiredString(props, "sourceDataType");
        String targetDataType = ValueExtractor.extractRequiredString(props, "targetDataType");
        String pattern = ValueExtractor.extractRequiredString(props, "pattern", "");
        return builder(taskIdentifier, order, sourceName, DataType.valueOf(sourceDataType), DataType.valueOf(targetDataType), pattern);
    }
    
    /**
     * Builder for {@link DataTypeConvertorMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param sourceName source name
     * @param sourceDataType source {@link DataType}
     * @param targetDataType target {@link DataType}
     * @return {@link DataTypeConvertorMetaData}
     */
    public static DataTypeConvertorMetaData builder(final String taskIdentifier, final int order, final String sourceName, final DataType sourceDataType, final DataType targetDataType) {
        return new FilterBuilder(taskIdentifier, order, sourceName, sourceDataType, targetDataType, null).build();
    }
    
    /**
     * Builder for {@link DataTypeConvertorMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param sourceName source name
     * @param sourceDataType source {@link DataType}
     * @param targetDataType target {@link DataType}
     * @param pattern pattern
     * @return {@link DataTypeConvertorMetaData}
     */
    public static DataTypeConvertorMetaData builder(final String taskIdentifier, final int order, final String sourceName, final DataType sourceDataType, final DataType targetDataType,
                                                    final String pattern) {
        return new FilterBuilder(taskIdentifier, order, sourceName, sourceDataType, targetDataType, pattern).build();
    }

    @RequiredArgsConstructor
    private static class FilterBuilder {

        private final String taskIdentifier;

        private final int order;
    
        private final String sourceName;
    
        private final DataType sourceDataType;
    
        private final DataType targetDataType;
    
        private final String pattern;

        private DataTypeConvertorMetaData build() {
            return new DataTypeConvertorMetaData(taskIdentifier, order, sourceName, sourceDataType, targetDataType, pattern);
        }
    }
}
