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

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.common.util.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Properties;

/**
 * Fixed node editor implemented of {@link FilterMetadata}.
 */
@RequiredArgsConstructor
public final class FixedNodeEditorMetadata implements FilterMetadata {

    private static final FilterType FILTER_TYPE = FilterType.FIXED_NODE;

    private final String taskIdentifier;

    private final int order;

    private final String name;

    private final String value;

    private final String type;

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
     * Builder for {@link FixedNodeEditorMetadata}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param props props
     * @return {@link FixedNodeEditorMetadata}
     */
    public static FixedNodeEditorMetadata builder(final String taskIdentifier, final int order, final Properties props) {
        String name = props.getProperty("name");
        Validator.notNull(name, "error : build FixedNodeEditor failed name in props can not be null, task identifier: [%s]", taskIdentifier);
        String value = props.getProperty("value");
        Validator.notNull(value, "error : build FixedNodeEditor failed value in props can not be null, task identifier: [%s]", taskIdentifier);
        // FIXME Refactoring type with enums
        String type = props.getProperty("type");
        Validator.notNull(type, "error : build FixedNodeEditor failed type in props can not be null, task identifier: [%s]", taskIdentifier);
        return new FixedNodeEditorMetadata.FilterBuilder(taskIdentifier, order, name, value, type).build();
    }

    @RequiredArgsConstructor
    private static class FilterBuilder {

        private final String taskIdentifier;

        private final int order;

        private final String name;

        private final String value;

        private final String type;

        private FixedNodeEditorMetadata build() {
            return new FixedNodeEditorMetadata(taskIdentifier, order, name, value, type);
        }
    }
}
