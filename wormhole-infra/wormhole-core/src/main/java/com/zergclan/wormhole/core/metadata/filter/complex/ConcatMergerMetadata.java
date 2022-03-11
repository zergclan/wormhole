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

package com.zergclan.wormhole.core.metadata.filter.complex;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.core.metadata.filter.FilterMetadata;
import com.zergclan.wormhole.core.metadata.filter.FilterType;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * Concat merger implemented of {@link FilterMetadata}.
 */
@RequiredArgsConstructor
public final class ConcatMergerMetadata implements FilterMetadata {

    private static final FilterType FILTER_TYPE = FilterType.CONCAT_MERGER;

    private final String taskIdentifier;

    private final int order;
    
    private final String delimiter;
    
    private final String targetName;
    
    private final Collection<String> sourceNames;

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
     * Builder for {@link ConcatMergerMetadata}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param props props
     * @return {@link ConcatMergerMetadata}
     */
    public static ConcatMergerMetadata builder(final String taskIdentifier, final int order, final Properties props) {
        String targetName = props.getProperty("targetName");
        Validator.notNull(targetName, "error : build ConcatMergerMetadata failed targetName in props can not be null, task identifier: [%s]", taskIdentifier);
        String sourceNames = props.getProperty("sourceNames");
        Validator.notNull(sourceNames, "error : build ConcatMergerMetadata failed sourceNames in props can not be null, task identifier: [%s]", taskIdentifier);
        String delimiter = props.getProperty("delimiter", "");
        return new FilterBuilder(taskIdentifier, order, delimiter, targetName, initSourceNames(sourceNames.split(MarkConstant.COMMA))).build();
    }
    
    private static Collection<String> initSourceNames(final String[] split) {
        int length = split.length;
        Validator.preState(1 < length, "error : build ConcatMergerMetadata failed sourceNames must be plural separated by ','");
        Collection<String> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            result.add(split[i].trim());
        }
        return result;
    }
    
    @RequiredArgsConstructor
    private static class FilterBuilder {
        
        private final String taskIdentifier;
        
        private final int order;
        
        private final String delimiter;
        
        private final String targetName;
        
        private final Collection<String> sourceNames;
        
        private ConcatMergerMetadata build() {
            return new ConcatMergerMetadata(taskIdentifier, order, delimiter, targetName, sourceNames);
        }
    }
}