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
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Properties;

/**
 * Not null validator implemented of {@link FilterMetadata}.
 */
@RequiredArgsConstructor
public final class NotNullValidatorMetadata implements FilterMetadata {
    
    private static final FilterType TYPE = FilterType.NOT_NULL;
    
    private final String taskIdentifier;
    
    private final int order;
    
    @Getter
    private final String name;
    
    @Override
    public int getOrder() {
        return order;
    }
    
    @Override
    public FilterType getType() {
        return TYPE;
    }
    
    @Override
    public String getIdentifier() {
        return taskIdentifier + MarkConstant.SPACE + TYPE + MarkConstant.SPACE + order;
    }
    
    /**
     * Builder for {@link NotNullValidatorMetadata}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param props props
     * @return {@link NotNullValidatorMetadata}
     */
    public static NotNullValidatorMetadata builder(final String taskIdentifier, final int order, final Properties props) {
        String name = props.getProperty("name");
        Validator.notNull(name, "error : build NotNullValidator failed name in props can not be null, task identifier: [%s]", taskIdentifier);
        return new NotNullValidatorMetadata.FilterBuilder(taskIdentifier, order, name).build();
    }
    
    @RequiredArgsConstructor
    private static class FilterBuilder {
        
        private final String taskIdentifier;
    
        private final int order;
        
        private final String name;
        
        private NotNullValidatorMetadata build() {
            return new NotNullValidatorMetadata(taskIdentifier, order, name);
        }
    }
}
