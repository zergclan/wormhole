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

package com.zergclan.wormhole.core.metadata.filter;

import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.core.config.FilterConfiguration;
import com.zergclan.wormhole.core.metadata.filter.complex.ConcatMergerMetadata;
import com.zergclan.wormhole.core.metadata.filter.complex.DelimiterSplitterMetadata;
import com.zergclan.wormhole.core.metadata.filter.precise.convertor.CodeConvertorMetadata;
import com.zergclan.wormhole.core.metadata.filter.precise.convertor.DataTypeConvertorMetadata;
import com.zergclan.wormhole.core.metadata.filter.precise.convertor.NameConvertorMetadata;
import com.zergclan.wormhole.core.metadata.filter.precise.editor.FixedNodeEditorMetadata;
import com.zergclan.wormhole.core.metadata.filter.precise.editor.NullToDefaultEditorMetadata;
import com.zergclan.wormhole.core.metadata.filter.precise.editor.ValueRangeEditorMetadata;
import com.zergclan.wormhole.core.metadata.filter.precise.validator.NotBlankValidatorMetadata;
import com.zergclan.wormhole.core.metadata.filter.precise.validator.NotNullValidatorMetadata;
import com.zergclan.wormhole.core.metadata.node.DataNodeMetadata;
import com.zergclan.wormhole.core.metadata.node.DataNodeTypeMetadata;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;

/**
 * Simple factory for create {@link FilterMetadata}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilterMetadataFactory {

    /**
     * Get default instance of {@link FilterMetadata}.
     *
     * @param taskIdentifier task identifier
     * @param sourceDataNode {@link DataNodeMetadata}
     * @param targetDataNode {@link DataNodeMetadata}
     * @return {@link FilterMetadata}
     */
    public static Collection<FilterMetadata> getDefaultInstance(final String taskIdentifier, final DataNodeMetadata sourceDataNode, final DataNodeMetadata targetDataNode) {
        DataNodeTypeMetadata.NodeType targetNodeType = targetDataNode.getType().getNodeType();
        DataNodeTypeMetadata.NodeType sourceNodeType = sourceDataNode.getType().getNodeType();
        String sourceName = sourceDataNode.getName();
        Collection<FilterMetadata> result = new LinkedList<>();
        boolean preState = DataNodeTypeMetadata.NodeType.FIXED != targetNodeType && DataNodeTypeMetadata.NodeType.MAPPED != targetNodeType;
        Validator.preState(preState, "error : create default filter metadata failed target NodeType can not be: [%s] task identifier: [%s]", targetNodeType.name(), taskIdentifier);
        if (DataNodeTypeMetadata.NodeType.REQUIRED == targetNodeType && DataNodeTypeMetadata.NodeType.STANDARD == sourceNodeType) {
            result.add(NotNullValidatorMetadata.builder(taskIdentifier, Integer.MIN_VALUE, sourceName));
        }
        if (DataNodeTypeMetadata.NodeType.DEFAULT_ABLE == targetNodeType && DataNodeTypeMetadata.NodeType.STANDARD == sourceNodeType) {
            String defaultValue = targetDataNode.getDefaultValue();
            Validator.notNull(defaultValue, "error : create default filter metadata failed defaultValue can not be null task identifier: [%s]", taskIdentifier);
            result.add(NullToDefaultEditorMetadata.builder(taskIdentifier, Integer.MIN_VALUE, sourceName, targetDataNode.getDefaultValue()));
        }
        result.addAll(createDataTypeConvertorMetadata(taskIdentifier, 0, sourceName, targetDataNode.getType().getDataType(), sourceDataNode.getType().getDataType()));
        return result;
    }

    private static Collection<FilterMetadata> createDataTypeConvertorMetadata(final String taskIdentifier, final int order, final String sourceName, final DataNodeTypeMetadata.DataType targetDataType,
                                                                              final DataNodeTypeMetadata.DataType sourceDataType) {
        Collection<FilterMetadata> result = new LinkedList<>();
        if (targetDataType != sourceDataType) {
            result.add(DataTypeConvertorMetadata.builder(taskIdentifier, order, sourceName, targetDataType, sourceDataType));
        }
        return result;
    }

    /**
     * Get instance of {@link FilterMetadata}.
     *
     * @param taskIdentifier task identifier
     * @param filterConfiguration {@link FilterConfiguration}
     * @return {@link FilterMetadata}
     */
    public static FilterMetadata getInstance(final String taskIdentifier, final FilterConfiguration filterConfiguration) {
        return createFilterMetadata(FilterType.valueOf(filterConfiguration.getType().toUpperCase(Locale.ROOT)), taskIdentifier, filterConfiguration.getOrder(), filterConfiguration.getProps());
    }

    /**
     * Get precise instance of {@link FilterMetadata}.
     *
     * @param taskIdentifier task identifier
     * @param filterConfiguration {@link FilterConfiguration}
     * @return {@link FilterMetadata}
     */
    public static FilterMetadata getPreciseInstance(final String taskIdentifier, final FilterConfiguration filterConfiguration) {
        FilterType filterType = FilterType.valueOf(filterConfiguration.getType().toUpperCase(Locale.ROOT));
        boolean preState = FilterType.CONCAT_MERGER == filterType || FilterType.DELIMITER_SPLITTER == filterType;
        Validator.preState(preState, "error : create precise filter metadata failed filterType can not be: [%s] task identifier: [%s]", filterType.name(), taskIdentifier);
        return createFilterMetadata(filterType, taskIdentifier, filterConfiguration.getOrder(), filterConfiguration.getProps());
    }

    private static FilterMetadata createFilterMetadata(final FilterType filterType, final String taskIdentifier, final int order, final Properties props) {
        switch (filterType) {
            case NOT_NULL:
                return NotNullValidatorMetadata.builder(taskIdentifier, order, props);
            case NOT_BLANK:
                return NotBlankValidatorMetadata.builder(taskIdentifier, order, props);
            case NULL_TO_DEFAULT:
                return NullToDefaultEditorMetadata.builder(taskIdentifier, order, props);
            case FIXED_NODE:
                return FixedNodeEditorMetadata.builder(taskIdentifier, order, props);
            case VALUE_RANGE:
                return ValueRangeEditorMetadata.builder(taskIdentifier, order, props);
            case NAME_CONVERTOR:
                return NameConvertorMetadata.builder(taskIdentifier, order, props);
            case CODE_CONVERTOR:
                return CodeConvertorMetadata.builder(taskIdentifier, order, props);
            case DATA_TYPE_CONVERTOR:
                return DataTypeConvertorMetadata.builder(taskIdentifier, order, props);
            case CONCAT_MERGER:
                return ConcatMergerMetadata.builder(taskIdentifier, order, props);
            case DELIMITER_SPLITTER:
                return DelimiterSplitterMetadata.builder(taskIdentifier, order, props);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
