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

package com.zergclan.wormhole.tool.concurrent;

import com.zergclan.wormhole.tool.util.SystemUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Manager of {@link ExecutorService}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExecutorServiceManager {
    
    private static final ExecutorService SCHEDULING_EXECUTOR;

    private static final ExecutorService COMPUTING_EXECUTOR;

    static {
        final int availableProcessors = SystemUtil.getAvailableProcessors();
        SCHEDULING_EXECUTOR = ExecutorServiceFactory.newSingleThreadExecutor("plan", 128);
        COMPUTING_EXECUTOR = ExecutorServiceFactory.newFixedThreadExecutor(availableProcessors, 2 * availableProcessors, "task", 1024);
    }
    
    /**
     * Get {@link ExecutorService} for scheduling.
     *
     * @return {@link ExecutorService}
     */
    public static ExecutorService getSchedulingExecutor() {
        return SCHEDULING_EXECUTOR;
    }
    
    /**
     * Get {@link ExecutorService} for computing.
     *
     * @return {@link ExecutorService}
     */
    public static ExecutorService getComputingExecutor() {
        return COMPUTING_EXECUTOR;
    }
}
