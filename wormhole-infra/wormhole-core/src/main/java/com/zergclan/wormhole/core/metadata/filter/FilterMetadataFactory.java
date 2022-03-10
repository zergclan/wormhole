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

import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.core.config.FilterConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Locale;

/**
 * Simple factory for create {@link FilterMetadata}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilterMetadataFactory {

    /**
     * Get precise instance of {@link FilterMetadata}.
     *
     * @param taskIdentifier task identifier
     * @param filterConfiguration {@link FilterConfiguration}
     * @return {@link FilterMetadata}
     */
    public static FilterMetadata getPreciseInstance(final String taskIdentifier, final FilterConfiguration filterConfiguration) {
        // TODO create filter metadata by type
        FilterType filterType = FilterType.valueOf(filterConfiguration.getType().toUpperCase(Locale.ROOT));
        boolean preState = FilterType.CONCAT_MERGER == filterType || FilterType.DELIMITER_SPLITTER == filterType;
        Validator.preState(preState, "error : create precise filter metadata failed filterType can not be: [%s] task identifier: [%s]", filterType.name(), taskIdentifier);
        switch (filterType) {
            case NOT_NULL:
                return NotNullValidatorMetadata.builder(taskIdentifier, filterConfiguration.getOrder(), filterConfiguration.getProps());
            default:
                return null;
        }
    }
}
