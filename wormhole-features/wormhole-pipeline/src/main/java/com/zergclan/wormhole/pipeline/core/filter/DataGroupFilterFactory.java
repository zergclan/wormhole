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

package com.zergclan.wormhole.pipeline.core.filter;

import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.data.core.node.PatternedDataTime;
import com.zergclan.wormhole.metadata.core.filter.FilterMetaData;
import com.zergclan.wormhole.metadata.core.filter.FilterType;
import com.zergclan.wormhole.metadata.core.filter.complex.NodeValueConcatMergerMetaData;
import com.zergclan.wormhole.metadata.core.filter.complex.NodeValueDelimiterSplitterMetaData;
import com.zergclan.wormhole.metadata.core.filter.precise.convertor.CodeConvertorMetaData;
import com.zergclan.wormhole.metadata.core.filter.precise.convertor.DataTypeConvertorMetaData;
import com.zergclan.wormhole.metadata.core.filter.precise.convertor.NodeNameConvertorMetaData;
import com.zergclan.wormhole.metadata.core.filter.precise.convertor.PatternedDataTimeConvertorMetaData;
import com.zergclan.wormhole.metadata.core.filter.precise.editor.FixedNodeEditorMetaData;
import com.zergclan.wormhole.metadata.core.filter.precise.editor.NullToDefaultEditorMetaData;
import com.zergclan.wormhole.metadata.core.filter.precise.editor.ValueAppendEditorMetaData;
import com.zergclan.wormhole.metadata.core.filter.precise.editor.ValueRangeEditorMetaData;
import com.zergclan.wormhole.metadata.core.filter.precise.validator.NotBlankValidatorMetaData;
import com.zergclan.wormhole.metadata.core.filter.precise.validator.NotNullValidatorMetaData;
import com.zergclan.wormhole.pipeline.api.Filter;
import com.zergclan.wormhole.pipeline.core.filter.complex.NodeValueConcatMerger;
import com.zergclan.wormhole.pipeline.core.filter.complex.NodeValueDelimiterSplitter;
import com.zergclan.wormhole.pipeline.core.filter.precise.convertor.CodeConvertor;
import com.zergclan.wormhole.pipeline.core.filter.precise.convertor.DataTypeConvertor;
import com.zergclan.wormhole.pipeline.core.filter.precise.convertor.NodeNameConvertor;
import com.zergclan.wormhole.pipeline.core.filter.precise.convertor.PatternedDataTimeConvertor;
import com.zergclan.wormhole.pipeline.core.filter.precise.editor.FixedNodeEditor;
import com.zergclan.wormhole.pipeline.core.filter.precise.editor.NullToDefaultEditor;
import com.zergclan.wormhole.pipeline.core.filter.precise.editor.ValueAppendEditor;
import com.zergclan.wormhole.pipeline.core.filter.precise.editor.ValueRangeEditor;
import com.zergclan.wormhole.pipeline.core.filter.precise.validator.NotBlankValidator;
import com.zergclan.wormhole.pipeline.core.filter.precise.validator.NotNullValidator;
import com.zergclan.wormhole.pipeline.core.helper.CodeConvertorHelper;
import com.zergclan.wormhole.pipeline.core.helper.DataTypeConvertorHelper;
import com.zergclan.wormhole.pipeline.core.helper.NodeValueHelper;
import com.zergclan.wormhole.pipeline.core.helper.NodeValueConcatMergerHelper;
import com.zergclan.wormhole.pipeline.core.helper.NodeValueDelimiterSplitterHelper;
import com.zergclan.wormhole.pipeline.core.helper.PatternedDataTimeConvertorHelper;
import com.zergclan.wormhole.pipeline.core.helper.ValueAppendHelper;
import com.zergclan.wormhole.pipeline.core.helper.ValueRangeHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Simple factory for create {@link Filter}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataGroupFilterFactory {
    
    /**
     * Create {@link Filter} of {@link DataGroup}.
     *
     * @param order order
     * @param typedFilterMetaData {@link FilterMetaData}
     * @return {@link Filter}
     */
    public static Collection<Filter<DataGroup>> createDataGroupFilters(final int order, final Map<FilterType, Collection<FilterMetaData>> typedFilterMetaData) {
        Collection<Filter<DataGroup>> result = new LinkedList<>();
        Iterator<Map.Entry<FilterType, Collection<FilterMetaData>>> iterator = typedFilterMetaData.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<FilterType, Collection<FilterMetaData>> entry = iterator.next();
            FilterType type = entry.getKey();
            switch (type) {
                case NOT_NULL:
                    result.add(createNotNullFilters(order, type, entry.getValue()));
                    break;
                case NOT_BLANK:
                    result.add(createNotBlankFilters(order, type, entry.getValue()));
                    break;
                case NULL_TO_DEFAULT:
                    result.add(createNullToDefaultFilters(order, type, entry.getValue()));
                    break;
                case FIXED_NODE:
                    result.add(createFixedNodeFilters(order, type, entry.getValue()));
                    break;
                case VALUE_RANGE:
                    result.add(createValueRangeFilters(order, type, entry.getValue()));
                    break;
                case VALUE_APPEND:
                    result.add(createValueAppendFilters(order, type, entry.getValue()));
                    break;
                case NAME_CONVERTOR:
                    result.add(createNodeNameConvertorFilters(order, type, entry.getValue()));
                    break;
                case CODE_CONVERTOR:
                    result.add(createCodeConvertorFilters(order, type, entry.getValue()));
                    break;
                case DATA_TYPE_CONVERTOR:
                    result.add(createDataTypeConvertorFilters(order, type, entry.getValue()));
                    break;
                case PATTERNED_DATA_TIME_CONVERTOR:
                    result.add(createPatternedDataTimeConvertorFilters(order, type, entry.getValue()));
                    break;
                case CONCAT_MERGER:
                    result.add(createConcatMergerFilters(order, type, entry.getValue()));
                    break;
                case DELIMITER_SPLITTER:
                    result.add(createDelimiterSplitterFilters(order, type, entry.getValue()));
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
        return result;
    }
    
    private static Filter<DataGroup> createDelimiterSplitterFilters(final int order, final FilterType type, final Collection<FilterMetaData> filters) {
        NodeValueDelimiterSplitterHelper[] nodeValueDelimiterSplitterHelpers = new NodeValueDelimiterSplitterHelper[filters.size()];
        Iterator<FilterMetaData> iterator = filters.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            NodeValueDelimiterSplitterMetaData filterMetaData = (NodeValueDelimiterSplitterMetaData) iterator.next();
            nodeValueDelimiterSplitterHelpers[index] = new NodeValueDelimiterSplitterHelper(filterMetaData.getSourceName(), filterMetaData.getTargetNames(), filterMetaData.getDelimiter());
            index++;
        }
        return new NodeValueDelimiterSplitter(order, type, nodeValueDelimiterSplitterHelpers);
    }
    
    private static Filter<DataGroup> createConcatMergerFilters(final int order, final FilterType type, final Collection<FilterMetaData> filters) {
        NodeValueConcatMergerHelper[] nodeValueConcatMergerHelpers = new NodeValueConcatMergerHelper[filters.size()];
        Iterator<FilterMetaData> iterator = filters.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            NodeValueConcatMergerMetaData filterMetaData = (NodeValueConcatMergerMetaData) iterator.next();
            nodeValueConcatMergerHelpers[index] = new NodeValueConcatMergerHelper(filterMetaData.getSourceNames(), filterMetaData.getTargetName(), filterMetaData.getDelimiter());
            index++;
        }
        return new NodeValueConcatMerger(order, type, nodeValueConcatMergerHelpers);
    }
    
    private static Filter<DataGroup> createDataTypeConvertorFilters(final int order, final FilterType type, final Collection<FilterMetaData> filters) {
        Map<String, DataTypeConvertorHelper> helpers = new LinkedHashMap<>();
        Iterator<FilterMetaData> iterator = filters.iterator();
        while (iterator.hasNext()) {
            DataTypeConvertorMetaData filterMetaData = (DataTypeConvertorMetaData) iterator.next();
            helpers.put(filterMetaData.getSourceName(), new DataTypeConvertorHelper(filterMetaData.getSourceDataType(), filterMetaData.getTargetDataType(), filterMetaData.getPattern()));
        }
        return new DataTypeConvertor(order, type, helpers);
    }
    
    private static Filter<DataGroup> createPatternedDataTimeConvertorFilters(final int order, final FilterType type, final Collection<FilterMetaData> filters) {
        Map<String, PatternedDataTimeConvertorHelper> helpers = new LinkedHashMap<>();
        Iterator<FilterMetaData> iterator = filters.iterator();
        while (iterator.hasNext()) {
            PatternedDataTimeConvertorMetaData filterMetaData = (PatternedDataTimeConvertorMetaData) iterator.next();
            PatternedDataTime.DatePattern source = PatternedDataTime.DatePattern.valueOfPattern(filterMetaData.getSourcePattern());
            PatternedDataTime.DatePattern target = PatternedDataTime.DatePattern.valueOfPattern(filterMetaData.getTargetPattern());
            helpers.put(filterMetaData.getSourceName(), new PatternedDataTimeConvertorHelper(source, target));
        }
        return new PatternedDataTimeConvertor(order, type, helpers);
    }
    
    private static Filter<DataGroup> createCodeConvertorFilters(final int order, final FilterType type, final Collection<FilterMetaData> filters) {
        Map<String, CodeConvertorHelper> codeConvertorHelpers = new LinkedHashMap<>();
        Iterator<FilterMetaData> iterator = filters.iterator();
        while (iterator.hasNext()) {
            CodeConvertorMetaData filterMetaData = (CodeConvertorMetaData) iterator.next();
            codeConvertorHelpers.put(filterMetaData.getSourceName(), new CodeConvertorHelper(filterMetaData.getSourceTargetCodeMappings(), filterMetaData.getDefaultCode()));
        }
        return new CodeConvertor(order, type, codeConvertorHelpers);
    }
    
    private static Filter<DataGroup> createNodeNameConvertorFilters(final int order, final FilterType type, final Collection<FilterMetaData> filters) {
        Map<String, String> sourceTargetNameMappings = new LinkedHashMap<>();
        Iterator<FilterMetaData> iterator = filters.iterator();
        while (iterator.hasNext()) {
            NodeNameConvertorMetaData filterMetaData = (NodeNameConvertorMetaData) iterator.next();
            sourceTargetNameMappings.put(filterMetaData.getSourceName(), filterMetaData.getTargetName());
        }
        return new NodeNameConvertor(order, type, sourceTargetNameMappings);
    }
    
    private static Filter<DataGroup> createValueRangeFilters(final int order, final FilterType type, final Collection<FilterMetaData> filters) {
        Map<String, ValueRangeHelper> valueRangeHelpers = new LinkedHashMap<>();
        Iterator<FilterMetaData> iterator = filters.iterator();
        while (iterator.hasNext()) {
            ValueRangeEditorMetaData filterMetaData = (ValueRangeEditorMetaData) iterator.next();
            valueRangeHelpers.put(filterMetaData.getSourceName(), new ValueRangeHelper(filterMetaData.getStartIndex(), filterMetaData.getEndIndex()));
        }
        return new ValueRangeEditor(order, type, valueRangeHelpers);
    }
    
    private static Filter<DataGroup> createValueAppendFilters(final int order, final FilterType type, final Collection<FilterMetaData> filters) {
        Map<String, ValueAppendHelper> valueAppendHelpers = new LinkedHashMap<>();
        Iterator<FilterMetaData> iterator = filters.iterator();
        while (iterator.hasNext()) {
            ValueAppendEditorMetaData filterMetaData = (ValueAppendEditorMetaData) iterator.next();
            valueAppendHelpers.put(filterMetaData.getSourceName(), new ValueAppendHelper(filterMetaData.getAppendValue()));
        }
        return new ValueAppendEditor(order, type, valueAppendHelpers);
    }
    
    private static Filter<DataGroup> createFixedNodeFilters(final int order, final FilterType type, final Collection<FilterMetaData> filters) {
        Collection<NodeValueHelper> nodeValueHelpers = new LinkedList<>();
        Iterator<FilterMetaData> iterator = filters.iterator();
        while (iterator.hasNext()) {
            FixedNodeEditorMetaData filterMetaData = (FixedNodeEditorMetaData) iterator.next();
            nodeValueHelpers.add(new NodeValueHelper(filterMetaData.getDataType(), filterMetaData.getSourceName(), filterMetaData.getDefaultValue()));
        }
        return new FixedNodeEditor(order, type, nodeValueHelpers);
    }
    
    private static Filter<DataGroup> createNullToDefaultFilters(final int order, final FilterType type, final Collection<FilterMetaData> filters) {
        Map<String, NodeValueHelper> nodeValueHelpers = new LinkedHashMap<>();
        Iterator<FilterMetaData> iterator = filters.iterator();
        while (iterator.hasNext()) {
            NullToDefaultEditorMetaData filterMetaData = (NullToDefaultEditorMetaData) iterator.next();
            nodeValueHelpers.put(filterMetaData.getSourceName(), new NodeValueHelper(filterMetaData.getDataType(), filterMetaData.getSourceName(), filterMetaData.getDefaultValue()));
        }
        return new NullToDefaultEditor(order, type, nodeValueHelpers);
    }
    
    private static Filter<DataGroup> createNotNullFilters(final int order, final FilterType type, final Collection<FilterMetaData> filters) {
        String[] names = new String[filters.size()];
        Iterator<FilterMetaData> iterator = filters.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            NotNullValidatorMetaData filterMetaData = (NotNullValidatorMetaData) iterator.next();
            names[index] = filterMetaData.getSourceName();
            index++;
        }
        return new NotNullValidator(order, type, names);
    }
    
    private static Filter<DataGroup> createNotBlankFilters(final int order, final FilterType type, final Collection<FilterMetaData> filters) {
        String[] names = new String[filters.size()];
        Iterator<FilterMetaData> iterator = filters.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            NotBlankValidatorMetaData filterMetaData = (NotBlankValidatorMetaData) iterator.next();
            names[index] = filterMetaData.getSourceName();
            index++;
        }
        return new NotBlankValidator(order, type, names);
    }
}
