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
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.core.metadata.filter.FilterMetaData;
import com.zergclan.wormhole.core.metadata.filter.FilterType;
import lombok.RequiredArgsConstructor;

import java.util.Properties;

/**
 * Name convertor implemented of {@link FilterMetaData}.
 */
@RequiredArgsConstructor
public final class NameConvertorMetaData implements FilterMetaData {

    private static final FilterType FILTER_TYPE = FilterType.NAME_CONVERTOR;

    private final String taskIdentifier;

    private final int order;
    
    private final String sourceName;

    private final String targetName;
    
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
     * Builder for {@link NameConvertorMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param props props
     * @return {@link NameConvertorMetaData}
     */
    public static NameConvertorMetaData builder(final String taskIdentifier, final int order, final Properties props) {
        String sourceName = props.getProperty("sourceName");
        Validator.notNull(sourceName, "error : build NameConvertorMetadata failed sourceName in props can not be null, task identifier: [%s]", taskIdentifier);
        String targetName = props.getProperty("targetName");
        Validator.notNull(targetName, "error : build NameConvertorMetadata failed targetName in props can not be null, task identifier: [%s]", taskIdentifier);
        return new NameConvertorMetaData.FilterBuilder(taskIdentifier, order, sourceName, targetName).build();
    }
    
    @RequiredArgsConstructor
    private static class FilterBuilder {

        private final String taskIdentifier;

        private final int order;

        private final String targetName;

        private final String sourceName;

        private NameConvertorMetaData build() {
            return new NameConvertorMetaData(taskIdentifier, order, targetName, sourceName);
        }
    }
}
