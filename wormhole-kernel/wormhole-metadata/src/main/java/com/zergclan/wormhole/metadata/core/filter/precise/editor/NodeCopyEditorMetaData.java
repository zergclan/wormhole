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
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Properties;

/**
 * Node copy editor implemented of {@link FilterMetaData}.
 */
@RequiredArgsConstructor
@Getter
public final class NodeCopyEditorMetaData implements FilterMetaData {
    
    private static final FilterType FILTER_TYPE = FilterType.NODE_COPY;
    
    private final String taskIdentifier;
    
    private final int order;
    
    private final String sourceName;
    
    private final String targetName;
    
    @Override
    public String getIdentifier() {
        return taskIdentifier + MarkConstant.SPACE + FILTER_TYPE.name() + MarkConstant.SPACE + order;
    }
    
    @Override
    public FilterType getType() {
        return FILTER_TYPE;
    }
    
    /**
     * Builder for {@link NodeCopyEditorMetaData}.
     *
     * @param taskIdentifier task identifier
     * @param order order
     * @param props props
     * @return {@link NodeCopyEditorMetaData}
     */
    public static NodeCopyEditorMetaData builder(final String taskIdentifier, final int order, final Properties props) {
        String sourceName = props.getProperty("sourceName");
        Validator.notNull(sourceName, "error : build NodeCopyEditorMetaData failed sourceName in props can not be null, task identifier: [%s]", taskIdentifier);
        String targetName = String.valueOf(props.get("targetName"));
        Validator.notNull(targetName, "error : build NodeCopyEditorMetaData failed targetName in props can not be null, task identifier: [%s]", taskIdentifier);
        return new NodeCopyEditorMetaData.FilterBuilder(taskIdentifier, order, sourceName, targetName).build();
    }
    
    @RequiredArgsConstructor
    private static class FilterBuilder {
        
        private final String taskIdentifier;
        
        private final int order;
        
        private final String sourceName;
        
        private final String targetName;
        
        private NodeCopyEditorMetaData build() {
            return new NodeCopyEditorMetaData(taskIdentifier, order, sourceName, targetName);
        }
    }
}
