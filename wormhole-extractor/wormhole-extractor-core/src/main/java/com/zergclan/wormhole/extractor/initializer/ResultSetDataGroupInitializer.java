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

package com.zergclan.wormhole.extractor.initializer;

import com.zergclan.wormhole.common.WormholeInitializer;
import com.zergclan.wormhole.common.data.DataGroup;
import com.zergclan.wormhole.common.data.node.DataNodeBuilder;
import com.zergclan.wormhole.common.metadata.plan.node.DataNodeMetaData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Result set data group initializer.
 */
@RequiredArgsConstructor
public final class ResultSetDataGroupInitializer implements WormholeInitializer<ResultSet, DataGroup> {
    
    private final Map<String, DataNodeMetaData> dataNodes;
    
    @SneakyThrows
    @Override
    public DataGroup init(final ResultSet resultSet) {
        DataGroup result = new DataGroup();
        Iterator<Map.Entry<String, DataNodeMetaData>> iterator = dataNodes.entrySet().iterator();
        Map.Entry<String, DataNodeMetaData> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            result.register(DataNodeBuilder.build(resultSet.getObject(entry.getKey()), entry.getValue()));
        }
        return result;
    }
}
