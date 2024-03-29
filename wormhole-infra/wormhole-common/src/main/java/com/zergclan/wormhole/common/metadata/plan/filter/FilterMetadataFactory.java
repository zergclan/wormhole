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

package com.zergclan.wormhole.common.metadata.plan.filter;

import com.zergclan.wormhole.common.configuration.FilterConfiguration;
import com.zergclan.wormhole.common.metadata.plan.filter.complex.NodeValueConcatMergerMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.complex.NodeValueDelimiterSplitterMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.precise.convertor.CodeConvertorMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.precise.convertor.DataTypeConvertorMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.precise.convertor.NodeNameConvertorMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.precise.convertor.PatternedDataTimeConvertorMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.precise.editor.FixedNodeEditorMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.precise.editor.NodeCopyEditorMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.precise.editor.NullToDefaultEditorMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.precise.editor.ValueAppendEditorMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.precise.editor.ValueRangeEditorMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.precise.validator.NotBlankValidatorMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.precise.validator.NotNullValidatorMetaData;
import com.zergclan.wormhole.common.metadata.plan.node.DataNodeMetaData;
import com.zergclan.wormhole.common.metadata.plan.node.DataType;
import com.zergclan.wormhole.common.metadata.plan.node.NodeType;
import com.zergclan.wormhole.tool.util.Validator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

/**
 * Simple factory for create {@link FilterMetaData}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilterMetadataFactory {

    /**
     * Get default instance of {@link FilterMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param sourceDataNode {@link DataNodeMetaData}
     * @param targetDataNode {@link DataNodeMetaData}
     * @return {@link FilterMetaData}
     */
    public static Collection<FilterMetaData> getDefaultInstance(final String taskIdentifier, final DataNodeMetaData sourceDataNode, final DataNodeMetaData targetDataNode) {
        NodeType targetNodeType = targetDataNode.getType().getNodeType();
        NodeType sourceNodeType = sourceDataNode.getType().getNodeType();
        String sourceName = sourceDataNode.getName();
        Collection<FilterMetaData> result = new LinkedList<>();
        boolean preState = NodeType.FIXED != targetNodeType && NodeType.MAPPED != targetNodeType;
        Validator.preState(preState, "error : create default filter metadata failed target NodeType can not be: [%s] task identifier: [%s]", targetNodeType.name(), taskIdentifier);
        if (NodeType.REQUIRED == targetNodeType && NodeType.STANDARD == sourceNodeType) {
            result.add(NotNullValidatorMetaData.builder(taskIdentifier, Integer.MIN_VALUE, sourceName));
        }
        if (NodeType.DEFAULT_ABLE == targetNodeType && NodeType.STANDARD == sourceNodeType) {
            String defaultValue = targetDataNode.getDefaultValue();
            Validator.notNull(defaultValue, "error : create default filter metadata failed defaultValue can not be null task identifier: [%s]", taskIdentifier);
            result.add(NullToDefaultEditorMetaData.builder(taskIdentifier, Integer.MIN_VALUE, sourceName, targetDataNode.getDefaultValue(), targetDataNode.getType().getDataType()));
        }
        result.addAll(createDataTypeConvertorMetadata(taskIdentifier, 0, sourceName, targetDataNode.getType().getDataType(), sourceDataNode.getType().getDataType()));
        return result;
    }

    private static Collection<FilterMetaData> createDataTypeConvertorMetadata(final String taskIdentifier, final int order, final String sourceName, final DataType targetDataType,
                                                                              final DataType sourceDataType) {
        Collection<FilterMetaData> result = new LinkedList<>();
        if (targetDataType != sourceDataType) {
            result.add(DataTypeConvertorMetaData.builder(taskIdentifier, order, sourceName, targetDataType, sourceDataType));
        }
        return result;
    }

    /**
     * Get instance of {@link FilterMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param filterConfiguration {@link FilterConfiguration}
     * @return {@link FilterMetaData}
     */
    public static FilterMetaData getInstance(final String taskIdentifier, final FilterConfiguration filterConfiguration) {
        return createFilterMetadata(FilterType.valueOfIgnoreCase(filterConfiguration.getType()), taskIdentifier, filterConfiguration.getOrder(), filterConfiguration.getProps());
    }

    /**
     * Get precise instance of {@link FilterMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param filterConfiguration {@link FilterConfiguration}
     * @return {@link FilterMetaData}
     */
    public static FilterMetaData getPreciseInstance(final String taskIdentifier, final FilterConfiguration filterConfiguration) {
        FilterType filterType = FilterType.valueOfIgnoreCase(filterConfiguration.getType());
        boolean preState = FilterType.CONCAT_MERGER != filterType && FilterType.DELIMITER_SPLITTER != filterType;
        Validator.preState(preState, "error : create precise filter metadata failed filterType can not be: [%s] task identifier: [%s]", filterType.name(), taskIdentifier);
        return createFilterMetadata(filterType, taskIdentifier, filterConfiguration.getOrder(), filterConfiguration.getProps());
    }

    private static FilterMetaData createFilterMetadata(final FilterType filterType, final String taskIdentifier, final int order, final Properties props) {
        switch (filterType) {
            case NOT_NULL:
                return NotNullValidatorMetaData.builder(taskIdentifier, order, props);
            case NOT_BLANK:
                return NotBlankValidatorMetaData.builder(taskIdentifier, order, props);
            case NULL_TO_DEFAULT:
                return NullToDefaultEditorMetaData.builder(taskIdentifier, order, props);
            case FIXED_NODE:
                return FixedNodeEditorMetaData.builder(taskIdentifier, order, props);
            case VALUE_SUB:
                return ValueRangeEditorMetaData.builder(taskIdentifier, order, props);
            case VALUE_APPEND:
                return ValueAppendEditorMetaData.builder(taskIdentifier, order, props);
            case NODE_COPY:
                return NodeCopyEditorMetaData.builder(taskIdentifier, order, props);
            case NAME_CONVERTOR:
                return NodeNameConvertorMetaData.builder(taskIdentifier, order, props);
            case CODE_CONVERTOR:
                return CodeConvertorMetaData.builder(taskIdentifier, order, props);
            case DATA_TYPE_CONVERTOR:
                return DataTypeConvertorMetaData.builder(taskIdentifier, order, props);
            case PATTERNED_DATA_TIME_CONVERTOR:
                return PatternedDataTimeConvertorMetaData.builder(taskIdentifier, order, props);
            case CONCAT_MERGER:
                return NodeValueConcatMergerMetaData.builder(taskIdentifier, order, props);
            case DELIMITER_SPLITTER:
                return NodeValueDelimiterSplitterMetaData.builder(taskIdentifier, order, props);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
