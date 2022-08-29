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

package com.zergclan.wormhole.pipeline.helper;

import com.zergclan.wormhole.tool.util.Validator;
import com.zergclan.wormhole.common.data.node.DataNode;
import com.zergclan.wormhole.common.data.node.DataGroup;
import com.zergclan.wormhole.common.data.node.TextDataNode;
import lombok.RequiredArgsConstructor;

/**
 * Node value concat merger helper.
 */
@RequiredArgsConstructor
public final class NodeValueConcatMergerHelper {
    
    private final String[] sourceNames;
    
    private final String targetName;
    
    private final String delimiter;
    
    /**
     * merge {@link DataNode}.
     *
     * @param dataGroup {@link DataGroup}
     * @return is success or not
     */
    public boolean merge(final DataGroup dataGroup) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = sourceNames.length;
        int count = 0;
        for (int i = 0; i < length; i++) {
            DataNode<?> dataNode = dataGroup.getDataNode(sourceNames[i]);
            boolean state = null != dataNode && !dataNode.isNull();
            Validator.preState(state, "error : merge node value failed node name [%s] can not be null", String.valueOf(sourceNames[i]));
            stringBuilder.append(dataNode.getValue());
            if (length == ++count) {
                break;
            }
            stringBuilder.append(delimiter);
        }
        return dataGroup.refresh(new TextDataNode(targetName, stringBuilder.toString()));
    }
}
