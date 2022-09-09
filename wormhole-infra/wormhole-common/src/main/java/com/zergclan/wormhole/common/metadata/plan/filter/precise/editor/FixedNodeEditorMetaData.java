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

package com.zergclan.wormhole.common.metadata.plan.filter.precise.editor;

import com.zergclan.wormhole.common.metadata.plan.filter.FilterMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.common.metadata.plan.node.DataType;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.tool.util.ValueExtractor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.Properties;

/**
 * Fixed node editor implemented of {@link FilterMetaData}.
 */
@RequiredArgsConstructor
@Getter
public final class FixedNodeEditorMetaData implements FilterMetaData {

    private static final FilterType FILTER_TYPE = FilterType.FIXED_NODE;

    private final String taskIdentifier;
    
    private final int order;

    private final String sourceName;

    private final String defaultValue;

    private final DataType dataType;

    @Override
    public String getIdentifier() {
        return taskIdentifier + MarkConstant.SPACE + FILTER_TYPE.name() + MarkConstant.SPACE + order;
    }

    @Override
    public FilterType getType() {
        return FILTER_TYPE;
    }

    /**
     * Builder for {@link FixedNodeEditorMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param props props
     * @return {@link FixedNodeEditorMetaData}
     */
    public static FixedNodeEditorMetaData builder(final String taskIdentifier, final int order, final Properties props) {
        String sourceName = ValueExtractor.extractRequiredString(props, "sourceName");
        String defaultValue = ValueExtractor.extractRequiredString(props, "defaultValue");
        String dataType = ValueExtractor.extractRequiredString(props, "defaultValue");
        return new FilterBuilder(taskIdentifier, order, sourceName, defaultValue, DataType.valueOf(dataType.toUpperCase(Locale.ROOT))).build();
    }

    @RequiredArgsConstructor
    private static class FilterBuilder {

        private final String taskIdentifier;

        private final int order;

        private final String sourceName;

        private final String defaultValue;

        private final DataType dataType;

        private FixedNodeEditorMetaData build() {
            return new FixedNodeEditorMetaData(taskIdentifier, order, sourceName, defaultValue, dataType);
        }
    }
}
