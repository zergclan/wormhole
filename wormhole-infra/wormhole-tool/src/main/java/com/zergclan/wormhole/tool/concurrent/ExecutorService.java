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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Executor service of Wormhole.
 */
@RequiredArgsConstructor
public final class ExecutorService extends AbstractExecutorService {

    private final ThreadPoolExecutor threadPoolExecutor;
    
    private final ExecutorRejectedHandler handler;
    
    /**
     * Submit {@link ProcessTask} to {@link ExecutorService}.
     *
     * @param processTask {@link ProcessTask}
     */
    public void submit(final ProcessTask processTask) {
        threadPoolExecutor.submit(processTask);
    }

    /**
     * Submit {@link PromiseTask} to {@link ExecutorService}.
     *
     * @param promiseTask {@link ProcessTask}
     * @param <V> class type of future result
     * @return future result
     */
    public <V> Future<V> submit(final PromiseTask<V> promiseTask) {
        return threadPoolExecutor.submit(promiseTask);
    }

    @Override
    public void shutdown() {
        threadPoolExecutor.shutdown();
    }
    
    @Override
    @NonNull
    public List<Runnable> shutdownNow() {
        return threadPoolExecutor.shutdownNow();
    }
    
    @Override
    public boolean isShutdown() {
        return threadPoolExecutor.isShutdown();
    }
    
    @Override
    public boolean isTerminated() {
        return threadPoolExecutor.isTerminated();
    }
    
    @Override
    public boolean awaitTermination(final long timeout, @NonNull final TimeUnit unit) throws InterruptedException {
        return threadPoolExecutor.awaitTermination(timeout, unit);
    }
    
    @Override
    public void execute(@NonNull final Runnable command) {
        threadPoolExecutor.submit(command);
    }
}
