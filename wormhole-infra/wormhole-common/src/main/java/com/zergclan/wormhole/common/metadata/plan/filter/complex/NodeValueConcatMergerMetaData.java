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

package com.zergclan.wormhole.common.metadata.plan.filter.complex;

import com.zergclan.wormhole.common.metadata.plan.filter.FilterMetaData;
import com.zergclan.wormhole.common.metadata.plan.filter.FilterType;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.tool.util.ValueExtractor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Properties;

/**
 * Node value concat merger implemented of {@link FilterMetaData}.
 */
@RequiredArgsConstructor
@Getter
public final class NodeValueConcatMergerMetaData implements FilterMetaData {

    private static final FilterType FILTER_TYPE = FilterType.CONCAT_MERGER;

    private final String taskIdentifier;

    private final int order;
    
    private final String[] sourceNames;
    
    private final String targetName;
    
    private final String delimiter;

    @Override
    public String getIdentifier() {
        return taskIdentifier + MarkConstant.SPACE + FILTER_TYPE.name() + MarkConstant.SPACE + order;
    }
    
    @Override
    public FilterType getType() {
        return FILTER_TYPE;
    }
    
    /**
     * Builder for {@link NodeValueConcatMergerMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param props props
     * @return {@link NodeValueConcatMergerMetaData}
     */
    public static NodeValueConcatMergerMetaData builder(final String taskIdentifier, final int order, final Properties props) {
        String targetName = ValueExtractor.extractRequiredString(props, "targetName");
        String[] sourceNames = ValueExtractor.extractRequiredArray(props, "sourceNames", MarkConstant.COMMA);
        String delimiter = ValueExtractor.extractRequiredString(props, "delimiter", "");
        return new FilterBuilder(taskIdentifier, order, sourceNames, targetName, delimiter).build();
    }
    
    @RequiredArgsConstructor
    private static class FilterBuilder {
        
        private final String taskIdentifier;
        
        private final int order;
    
        private final String[] sourceNames;
    
        private final String targetName;
    
        private final String delimiter;
        
        private NodeValueConcatMergerMetaData build() {
            return new NodeValueConcatMergerMetaData(taskIdentifier, order, sourceNames, targetName, delimiter);
        }
    }
}
