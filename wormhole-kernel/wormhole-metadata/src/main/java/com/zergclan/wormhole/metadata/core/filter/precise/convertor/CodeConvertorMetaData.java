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

package com.zergclan.wormhole.metadata.core.filter.precise.convertor;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.metadata.core.filter.FilterMetaData;
import com.zergclan.wormhole.metadata.core.filter.FilterType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Business code convertor implemented of {@link FilterMetaData}.
 */
@RequiredArgsConstructor
@Getter
public final class CodeConvertorMetaData implements FilterMetaData {
    
    private static final FilterType FILTER_TYPE = FilterType.CODE_CONVERTOR;
    
    private final String taskIdentifier;
    
    private final int order;
    
    private final String sourceName;

    private final Map<String, String> sourceTargetCodeMappings;

    private final String defaultCode;
    
    @Override
    public String getIdentifier() {
        return taskIdentifier + MarkConstant.SPACE + FILTER_TYPE.name() + MarkConstant.SPACE + order;
    }
    
    @Override
    public FilterType getType() {
        return FILTER_TYPE;
    }
    
    /**
     * Builder for {@link CodeConvertorMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param props props
     * @return {@link CodeConvertorMetaData}
     */
    public static CodeConvertorMetaData builder(final String taskIdentifier, final int order, final Properties props) {
        String sourceName = props.getProperty("sourceName");
        Validator.notNull(sourceName, "error : build CodeConvertorMetadata failed sourceName in props can not be null, task identifier: [%s]", taskIdentifier);
        Map<String, String> sourceTargetCodeMappings = createSourceTargetCodeMappings(props);
        Validator.preState(sourceTargetCodeMappings.isEmpty(),
                "error : build CodeConvertorMetadata failed source target code mappings in props can not be empty, task identifier: [%s]", taskIdentifier);
        return new FilterBuilder(taskIdentifier, order, sourceName, sourceTargetCodeMappings, props.getProperty("defaultCode")).build();
    }

    private static Map<String, String> createSourceTargetCodeMappings(final Properties props) {
        Map<String, String> result = new LinkedHashMap<>();
        Iterator<Map.Entry<Object, Object>> iterator = props.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            result.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return result;
    }

    @RequiredArgsConstructor
    private static class FilterBuilder {
    
        private final String taskIdentifier;
    
        private final int order;

        private final String sourceName;

        private final Map<String, String> sourceTargetCodeMappings;

        private final String defaultCode;
        
        private CodeConvertorMetaData build() {
            return new CodeConvertorMetaData(taskIdentifier, order, sourceName, sourceTargetCodeMappings, defaultCode);
        }
    }
}
