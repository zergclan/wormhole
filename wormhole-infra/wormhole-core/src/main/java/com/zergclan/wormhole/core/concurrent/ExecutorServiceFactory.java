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

package com.zergclan.wormhole.core.concurrent;

import com.zergclan.wormhole.common.WormholeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple factory for create WormholeExecutorService.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExecutorServiceFactory {
    
    /**
     * The newly created single-threaded WormholeExecutorService.
     *
     * @param namePrefix name prefix of thread in thread pool executor
     * @param workQueueSize work queue size
     * @param keepAliveTime keep alive time
     * @param handler wormhole rejected handler
     * @return wormhole executor service instance
     */
    public static ExecutorService newSingleThreadExecutor(final String namePrefix, final int workQueueSize, final int keepAliveTime, final ExecutorRejectedHandler handler) {
        return new ExecutorBuilder().corePoolSize(1).maxPoolSize(1).namePrefix(namePrefix).keepAliveTime(keepAliveTime).workQueueSize(workQueueSize).handler(handler).build();
    }
    
    /**
     * The newly created fixed-size-threaded WormholeExecutorService.
     *
     * @param coreSize core size of threads
     * @param maxSize Maximum size of threads
     * @param namePrefix name prefix of thread in thread pool executor
     * @param workQueueSize work queue size
     * @return wormhole executor service instance
     */
    public static ExecutorService newFixedThreadExecutor(final int coreSize, final int maxSize, final String namePrefix, final int workQueueSize) {
        return new ExecutorBuilder().corePoolSize(coreSize).maxPoolSize(maxSize).namePrefix(namePrefix).workQueueSize(workQueueSize).build();
    }
    
    /**
     * The newly created fixed-size-threaded WormholeExecutorService.
     *
     * @param coreSize core size of threads
     * @param maxSize Maximum size of threads
     * @param namePrefix name prefix of thread in thread pool executor
     * @param workQueueSize work queue size
     * @param keepAliveTime keep alive time
     * @param handler wormhole rejected handler
     * @return wormhole executor service instance
     */
    public static ExecutorService newFixedThreadExecutor(final int coreSize, final int maxSize, final String namePrefix, final int workQueueSize, final int keepAliveTime,
                                                         final ExecutorRejectedHandler handler) {
        return new ExecutorBuilder().corePoolSize(coreSize).maxPoolSize(maxSize).namePrefix(namePrefix).keepAliveTime(keepAliveTime).workQueueSize(workQueueSize).handler(handler).build();
    }

    /**
     * Builder for WormholeExecutorService.
     */
    private static class ExecutorBuilder {

        private int corePoolSize;

        private int maxPoolSize;

        private Long keepAliveTime;

        private TimeUnit timeUnit;

        private int workQueueSize;

        private BlockingQueue<Runnable> workQueue;

        private String namePrefix;

        private ThreadFactory threadFactory;

        private ExecutorRejectedHandler handler;
        
        ExecutorBuilder corePoolSize(final Integer corePoolSize) {
            this.corePoolSize = corePoolSize;
            return this;
        }
        
        ExecutorBuilder maxPoolSize(final Integer maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }
        
        ExecutorBuilder keepAliveTime(final long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
            return this;
        }
        
        ExecutorBuilder workQueueSize(final int workQueueSize) {
            this.workQueueSize = workQueueSize;
            return this;
        }
        
        ExecutorBuilder namePrefix(final String namePrefix) {
            this.namePrefix = namePrefix;
            return this;
        }
        
        ExecutorBuilder threadFactory(final ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }
        
        ExecutorBuilder handler(final ExecutorRejectedHandler handler) {
            this.handler = handler;
            return this;
        }
        
        /**
         * Build instance of WormholeExecutorService.
         *
         * @return new instance of WormholeExecutorService
         */
        private ExecutorService build() {
            if (null == keepAliveTime) {
                // FIXME refer to HikariConfig.MAX_LIFETIME adjustment when test completed.
                keepAliveTime = 30 * 60 * 1000L;
            }
            if (null == timeUnit) {
                timeUnit = TimeUnit.MILLISECONDS;
            }
            if (null == namePrefix) {
                namePrefix = "default";
            }
            if (null == handler) {
                handler = new DefaultExecutorRejectedHandler();
            }
            if (null == threadFactory) {
                threadFactory = new DefaultThreadFactory(namePrefix);
            }
            return new ExecutorService(new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUnit, new ArrayBlockingQueue<>(workQueueSize), threadFactory), handler);
        }
    }

    /**
     * Default implemented {@link ExecutorRejectedHandler} for WormholeExecutorService.
     */
    private static class DefaultExecutorRejectedHandler implements ExecutorRejectedHandler {

        @Override
        public <V> Future<V> handle(PromisedTask<V> task) {
            throw new WormholeException("error : promised task");
        }
    }

    /**
     * Default thread factory for WormholeExecutorService.
     */
    private static class DefaultThreadFactory implements ThreadFactory {
        
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        
        private static final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);
        
        private final ThreadGroup group;
        
        private final String namePrefix;
        
        DefaultThreadFactory(final String namePrefix) {
            this.namePrefix = "wormhole-" + namePrefix + "-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
            SecurityManager securityManager = System.getSecurityManager();
            this.group = (null != securityManager) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            
        }
        
        public Thread newThread(final Runnable runnable) {
            Thread result = new Thread(group, runnable, namePrefix + THREAD_NUMBER.getAndIncrement());
            if (result.isDaemon()) {
                result.setDaemon(false);
            }
            if (Thread.NORM_PRIORITY != result.getPriority()) {
                result.setPriority(Thread.NORM_PRIORITY);
            }
            return result;
        }
    }
}
