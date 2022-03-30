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

package com.zergclan.wormhole.pipeline.core.filter;

import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.metadata.core.filter.FilterMetaData;
import com.zergclan.wormhole.pipeline.api.Filter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Simple factory for create {@link Filter}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilterFactory {
    
    /**
     * Create {@link Filter} of {@link DataGroup}.
     *
     * @param filterMetaData {@link FilterMetaData}
     * @return {@link Filter}
     */
    public static Filter<DataGroup> createDataGroupFilter(final FilterMetaData filterMetaData) {
        // TODO create filter for data group
        return null;
    }
}
