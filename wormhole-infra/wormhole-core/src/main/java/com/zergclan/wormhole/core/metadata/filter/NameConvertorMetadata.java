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
import lombok.RequiredArgsConstructor;

/**
 * Name convertor implemented of {@link FilterMetadata}.
 */
@RequiredArgsConstructor
public final class NameConvertorMetadata implements FilterMetadata {

    private static final FilterType FILTER_TYPE = FilterType.NAME_CONVERTOR;

    private final String taskIdentifier;

    private final int order;

    private final String targetName;

    private final String sourceName;

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
     * Builder for {@link NameConvertorMetadata}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param targetName target name
     * @param sourceName source name
     * @return {@link NameConvertorMetadata}
     */
    public static NameConvertorMetadata builder(final String taskIdentifier, final int order, final String targetName, final String sourceName) {
        return new NameConvertorMetadata.FilterBuilder(taskIdentifier, order, targetName, sourceName).build();
    }

    @RequiredArgsConstructor
    private static class FilterBuilder {

        private final String taskIdentifier;

        private final int order;

        private final String targetName;

        private final String sourceName;

        private NameConvertorMetadata build() {
            return new NameConvertorMetadata(taskIdentifier, order, targetName, sourceName);
        }
    }

}
