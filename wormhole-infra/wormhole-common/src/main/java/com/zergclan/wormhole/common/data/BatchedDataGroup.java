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

package com.zergclan.wormhole.common.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Batched of {@link DataGroup}.
 */
@RequiredArgsConstructor
@Getter
public final class BatchedDataGroup {
    
    private final String planIdentifier;
    
    private final Long planBatch;
    
    private final String taskIdentifier;
    
    private final Long taskBatch;
    
    private final String ownerIdentifier;
    
    private final int batchIndex;
    
    private final int batchSize;
    
    private final Collection<DataGroup> dataGroups;
    
    private final Collection<DataGroup> errors = new LinkedList<>();
    
    /**
     * Remove {@link DataGroup}.
     *
     * @param dataGroup {@link DataGroup}
     * @return is removed or not
     */
    public boolean remove(final DataGroup dataGroup) {
        return errors.add(dataGroup);
    }
    
    /**
     * Clear errors.
     */
    public void clearErrors() {
        dataGroups.removeAll(errors);
    }
}
