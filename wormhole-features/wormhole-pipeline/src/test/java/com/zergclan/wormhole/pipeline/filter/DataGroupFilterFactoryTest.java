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

package com.zergclan.wormhole.pipeline.filter;

import com.zergclan.wormhole.data.node.DataGroup;
import com.zergclan.wormhole.metadata.plan.filter.FilterMetaData;
import com.zergclan.wormhole.metadata.plan.filter.FilterType;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class DataGroupFilterFactoryTest {
    
    @Test
    public void assertCreateDataGroupFilters() {
        Map<FilterType, Collection<FilterMetaData>> typedFilterMetaData = createEmptyTypedFilterMetaData();
        Collection<Filter<DataGroup>> dataGroupFilters = DataGroupFilterFactory.createDataGroupFilters(0, typedFilterMetaData);
        assertEquals(0, dataGroupFilters.size());
    }
    
    private Map<FilterType, Collection<FilterMetaData>> createEmptyTypedFilterMetaData() {
        Map<FilterType, Collection<FilterMetaData>> result = new LinkedHashMap<>();
        return result;
    }
}
