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

import com.zergclan.wormhole.data.node.DataNode;
import com.zergclan.wormhole.data.node.DataGroup;
import com.zergclan.wormhole.data.node.TextDataNode;
import lombok.RequiredArgsConstructor;

/**
 * Node value splitter helper.
 */
@RequiredArgsConstructor
public final class NodeValueDelimiterSplitterHelper {
    
    private final String sourceName;
    
    private final String[] targetNames;
    
    private final String delimiter;
    
    /**
     * Split node value.
     *
     * @param dataGroup {@link DataGroup}
     * @return is success or not
     */
    public boolean splitNodeValue(final DataGroup dataGroup) {
        DataNode<?> sourceDataNode = dataGroup.getDataNode(sourceName);
        if (sourceDataNode instanceof TextDataNode) {
            String value = ((TextDataNode) sourceDataNode).getValue();
            String[] split = value.split(delimiter);
            for (int i = 0; i < split.length; i++) {
                dataGroup.register(new TextDataNode(targetNames[i], split[i]));
            }
            dataGroup.remove(sourceName);
            return true;
        }
        return false;
    }
}
