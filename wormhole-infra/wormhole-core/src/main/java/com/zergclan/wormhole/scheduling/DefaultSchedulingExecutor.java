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

package com.zergclan.wormhole.scheduling;

import com.zergclan.wormhole.core.concurrent.ExecutorService;
import com.zergclan.wormhole.extracter.Extractor;
import com.zergclan.wormhole.loader.Loader;
import lombok.RequiredArgsConstructor;

/**
 * Default scheduling executor.
 */
@RequiredArgsConstructor
public class DefaultSchedulingExecutor implements SchedulingExecutor {
    
    private final Extractor extractor;
    
    private final Loader loader;
    
    private final ExecutorService executorService;
    
    /**
     * Execute.
     */
    public void execute() {
        // TODO Initial configuration
        
        // TODO Extract data extractor.extractDatum()
        // Collection<Map<String, Object>> Datum = extractor.extractDatum();
    
        // TODO transform Datum executorService.submit()
        
        // TODO Loader data
    }
}
