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

package com.zergclan.wormhole.pipeline.swapper;

import com.zergclan.wormhole.api.Swapper;
import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.pipeline.data.MySQLDataGroup;

import java.util.Map;

/**
 * MySQL data group swapper.
 */
public final class MySQLDataGroupSwapper implements Swapper<Map<String, Object>, DataGroup> {
    
    @Override
    public DataGroup swapToTarget(final Map<String, Object> dataMap) {
        MySQLDataGroup result = new MySQLDataGroup();
        result.init(dataMap);
        return result;
    }
    
    @Override
    public Map<String, Object> swapToSource(final DataGroup dataGroup) {
        return dataGroup.getDataMap();
    }
}
