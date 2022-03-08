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

package com.zergclan.wormhole.core.metadata.task;

import com.zergclan.wormhole.core.api.metadata.Metadata;
import com.zergclan.wormhole.core.metadata.node.FilterMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * Task metadata.
 */
@RequiredArgsConstructor
@Getter
public final class TaskMetadata implements Metadata {
    
    private final String identifier;
    
    private final int order;
    
    private final int batchSize;
    
    private final SourceMetadata source;
    
    private final TargetMetadata target;

    private final Collection<FilterMetadata> filters;
}
