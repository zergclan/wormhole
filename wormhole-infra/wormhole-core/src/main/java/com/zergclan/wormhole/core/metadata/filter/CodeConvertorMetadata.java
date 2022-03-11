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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * Business code convertor implemented of {@link FilterMetadata}.
 */
@RequiredArgsConstructor
public final class CodeConvertorMetadata implements FilterMetadata {
    
    private static final FilterType FILTER_TYPE = FilterType.CODE_CONVERTOR;
    
    private final String taskIdentifier;
    
    private final int order;
    
    private final String sourceName;
    
    private final Collection<String> sourceCodes;
    
    private final String targetCode;
    
    private final String defaultCode;
    
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
     * Builder for {@link CodeConvertorMetadata}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param props props
     * @return {@link CodeConvertorMetadata}
     */
    public static CodeConvertorMetadata builder(final String taskIdentifier, final int order, final Properties props) {
        String sourceName = props.getProperty("sourceName");
        Validator.notNull(sourceName, "error : build CodeConvertorMetadata failed sourceName in props can not be null, task identifier: [%s]", taskIdentifier);
        String sourceCodes = props.getProperty("sourceCodes");
        Validator.notNull(sourceCodes, "error : build CodeConvertorMetadata failed sourceCodes in props can not be null, task identifier: [%s]", taskIdentifier);
        String targetCode = props.getProperty("targetCode");
        Validator.notNull(targetCode, "error : build CodeConvertorMetadata failed targetCode in props can not be null, task identifier: [%s]", taskIdentifier);
        return new FilterBuilder(taskIdentifier, order, sourceName, initSourceCodes(sourceCodes.split(MarkConstant.COMMA)), targetCode, props.getProperty("defaultCode")).build();
    }
    
    private static Collection<String> initSourceCodes(final String[] split) {
        int length = split.length;
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
    
        private final String sourceName;
    
        private final Collection<String> sourceCodes;
    
        private final String targetCode;
    
        private final String defaultCode;
        
        private CodeConvertorMetadata build() {
            return new CodeConvertorMetadata(taskIdentifier, order, sourceName, sourceCodes, targetCode, defaultCode);
        }
    }
}
