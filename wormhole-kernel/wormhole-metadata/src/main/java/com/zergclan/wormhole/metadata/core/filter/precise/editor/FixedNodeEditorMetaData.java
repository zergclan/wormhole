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

package com.zergclan.wormhole.metadata.core.filter.precise.editor;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.metadata.core.filter.FilterMetaData;
import com.zergclan.wormhole.metadata.core.filter.FilterType;
import com.zergclan.wormhole.metadata.core.node.DataNodeTypeMetaData;
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
     * Builder for {@link FixedNodeEditorMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param props props
     * @return {@link FixedNodeEditorMetaData}
     */
    public static FixedNodeEditorMetaData builder(final String taskIdentifier, final int order, final Properties props) {
        String sourceName = props.getProperty("sourceName");
        Validator.notNull(sourceName, "error : build FixedNodeEditorMetadata failed sourceName in props can not be null, task identifier: [%s]", taskIdentifier);
        String defaultValue = props.getProperty("defaultValue");
        Validator.notNull(defaultValue, "error : build FixedNodeEditorMetadata failed defaultValue in props can not be null, task identifier: [%s]", taskIdentifier);
        String dataType = props.getProperty("dataType");
        Validator.notNull(dataType, "error : build FixedNodeEditorMetadata failed dataType in props can not be null, task identifier: [%s]", taskIdentifier);
        return new FilterBuilder(taskIdentifier, order, sourceName, defaultValue, DataNodeTypeMetaData.DataType.valueOf(dataType.toUpperCase(Locale.ROOT))).build();
    }

    @RequiredArgsConstructor
    private static class FilterBuilder {

        private final String taskIdentifier;

        private final int order;

        private final String sourceName;

        private final String defaultValue;

        private final DataNodeTypeMetaData.DataType dataType;

        private FixedNodeEditorMetaData build() {
            return new FixedNodeEditorMetaData(taskIdentifier, order, sourceName, defaultValue, dataType);
        }
    }
}
