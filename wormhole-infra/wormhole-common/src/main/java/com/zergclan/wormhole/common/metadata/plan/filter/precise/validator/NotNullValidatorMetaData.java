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

package com.zergclan.wormhole.common.metadata.plan.filter.precise.validator;

import com.zergclan.wormhole.common.metadata.plan.filter.FilterMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.tool.util.ValueExtractor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Properties;

/**
 * Not null validator implemented of {@link FilterMetaData}.
 */
@RequiredArgsConstructor
@Getter
public final class NotNullValidatorMetaData implements FilterMetaData {
    
    private static final FilterType FILTER_TYPE = FilterType.NOT_NULL;
    
    private final String taskIdentifier;
    
    private final int order;
    
    private final String sourceName;
    
    @Override
    public FilterType getType() {
        return FILTER_TYPE;
    }
    
    @Override
    public String getIdentifier() {
        return taskIdentifier + MarkConstant.SPACE + FILTER_TYPE.name() + MarkConstant.SPACE + order;
    }
    
    /**
     * Builder for {@link NotNullValidatorMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param props props
     * @return {@link NotNullValidatorMetaData}
     */
    public static NotNullValidatorMetaData builder(final String taskIdentifier, final int order, final Properties props) {
        String sourceName = ValueExtractor.extractRequiredString(props, "sourceName");
        return builder(taskIdentifier, order, sourceName);
    }

    /**
     * Builder for {@link NotNullValidatorMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param sourceName source name
     * @return {@link NotNullValidatorMetaData}
     */
    public static NotNullValidatorMetaData builder(final String taskIdentifier, final int order, final String sourceName) {
        return new FilterBuilder(taskIdentifier, order, sourceName).build();
    }

    @RequiredArgsConstructor
    private static class FilterBuilder {
        
        private final String taskIdentifier;
    
        private final int order;
        
        private final String sourceName;
        
        private NotNullValidatorMetaData build() {
            return new NotNullValidatorMetaData(taskIdentifier, order, sourceName);
        }
    }
}
