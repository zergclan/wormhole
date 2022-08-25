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

package com.zergclan.wormhole.metadata.plan.filter.precise.editor;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.common.util.ValueExtractor;
import com.zergclan.wormhole.metadata.plan.filter.FilterMetaData;
import com.zergclan.wormhole.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.metadata.plan.node.DataNodeTypeMetaData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Properties;

/**
 * Null to default editor implemented of {@link FilterMetaData}.
 */
@RequiredArgsConstructor
@Getter
public final class NullToDefaultEditorMetaData implements FilterMetaData {

    private static final FilterType FILTER_TYPE = FilterType.NULL_TO_DEFAULT;

    private final String taskIdentifier;

    private final int order;

    private final String sourceName;

    private final String defaultValue;
    
    private final DataNodeTypeMetaData.DataType dataType;

    @Override
    public String getIdentifier() {
        return taskIdentifier + MarkConstant.SPACE + FILTER_TYPE.name() + MarkConstant.SPACE + order;
    }

    @Override
    public FilterType getType() {
        return FILTER_TYPE;
    }

    /**
     * Builder for {@link NullToDefaultEditorMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param props props
     * @return {@link NullToDefaultEditorMetaData}
     */
    public static NullToDefaultEditorMetaData builder(final String taskIdentifier, final int order, final Properties props) {
        String sourceName = ValueExtractor.extractRequiredString(props, "sourceName");
        String defaultValue = ValueExtractor.extractRequiredString(props, "defaultValue");
        String dataType = ValueExtractor.extractRequiredString(props, "dataType");
        return builder(taskIdentifier, order, sourceName, defaultValue, DataNodeTypeMetaData.DataType.valueOf(dataType));
    }

    /**
     * Builder for {@link NullToDefaultEditorMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param sourceName source name
     * @param defaultValue default value
     * @param dataType {@link DataNodeTypeMetaData.DataType}
     * @return {@link NullToDefaultEditorMetaData}
     */
    public static NullToDefaultEditorMetaData builder(final String taskIdentifier, final int order, final String sourceName, final String defaultValue, final DataNodeTypeMetaData.DataType dataType) {
        return new FilterBuilder(taskIdentifier, order, sourceName, defaultValue, dataType).build();
    }

    @RequiredArgsConstructor
    private static class FilterBuilder {

        private final String taskIdentifier;

        private final int order;

        private final String sourceName;

        private final String defaultValue;
    
        private final DataNodeTypeMetaData.DataType dataType;

        private NullToDefaultEditorMetaData build() {
            return new NullToDefaultEditorMetaData(taskIdentifier, order, sourceName, defaultValue, dataType);
        }
    }
}
