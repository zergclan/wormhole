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

package com.zergclan.wormhole.common.concurrent;

import com.zergclan.wormhole.common.WormholeException;
import com.zergclan.wormhole.common.util.Validator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple factory for create WormholeExecutorService.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExecutorServiceFactory {
    
    private static final long DEFAULT_KEEP_ALIVE_TIME = 30 * 60 * 1000L;
    
    private static final ExecutorRejectedHandler DEFAULT_REJECTED_HANDLER = task -> {
        throw new WormholeException("error : executor rejected");
    };
    
    /**
     * The newly created single-threaded {@link ExecutorService}.
     *
     * @param namePrefix name prefix of thread in thread pool executor
     * @param workQueueSize work queue size
     * @return wormhole executor service instance
     */
    public static ExecutorService newSingleThreadExecutor(final String namePrefix, final int workQueueSize) {
        return newSingleThreadExecutor(namePrefix, workQueueSize, DEFAULT_KEEP_ALIVE_TIME, DEFAULT_REJECTED_HANDLER);
    }
    
    /**
     * The newly created single-threaded {@link ExecutorService}.
     *
     * @param namePrefix name prefix of thread in thread pool executor
     * @param workQueueSize work queue size
     * @param keepAliveTime keep alive time
     * @return wormhole executor service instance
     */
    public static ExecutorService newSingleThreadExecutor(final String namePrefix, final int workQueueSize, final Long keepAliveTime) {
        return newSingleThreadExecutor(namePrefix, workQueueSize, keepAliveTime, DEFAULT_REJECTED_HANDLER);
    }
    
    /**
     * The newly created single-threaded WormholeExecutorService.
     *
     * @param namePrefix name prefix of thread in thread pool executor
     * @param workQueueSize work queue size
     * @param keepAliveTime keep alive time
     * @param handler wormhole rejected handler
     * @return wormhole executor service instance
     */
    public static ExecutorService newSingleThreadExecutor(final String namePrefix, final int workQueueSize, final Long keepAliveTime, final ExecutorRejectedHandler handler) {
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
        return newFixedThreadExecutor(coreSize, maxSize, namePrefix, workQueueSize, DEFAULT_KEEP_ALIVE_TIME);
    }
    
    /**
     * The newly created fixed-size-threaded WormholeExecutorService.
     *
     * @param coreSize core size of threads
     * @param maxSize Maximum size of threads
     * @param namePrefix name prefix of thread in thread pool executor
     * @param workQueueSize work queue size
     * @param keepAliveTime keep alive time
     * @return wormhole executor service instance
     */
    public static ExecutorService newFixedThreadExecutor(final int coreSize, final int maxSize, final String namePrefix, final int workQueueSize, final long keepAliveTime) {
        return newFixedThreadExecutor(coreSize, maxSize, namePrefix, workQueueSize, keepAliveTime, DEFAULT_REJECTED_HANDLER);
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
    public static ExecutorService newFixedThreadExecutor(final int coreSize, final int maxSize, final String namePrefix, final int workQueueSize, final long keepAliveTime,
                                                         final ExecutorRejectedHandler handler) {
        return newFixedThreadExecutor(coreSize, maxSize, namePrefix, workQueueSize, keepAliveTime, new DefaultThreadFactory(namePrefix), handler);
    }
    
    /**
     * The newly created fixed-size-threaded WormholeExecutorService.
     *
     * @param coreSize core size of threads
     * @param maxSize Maximum size of threads
     * @param namePrefix name prefix of thread in thread pool executor
     * @param workQueueSize work queue size
     * @param keepAliveTime keep alive time
     * @param threadFactory thread factory
     * @param handler wormhole rejected handler
     * @return wormhole executor service instance
     */
    public static ExecutorService newFixedThreadExecutor(final int coreSize, final int maxSize, final String namePrefix, final int workQueueSize, final long keepAliveTime,
                                                         final ThreadFactory threadFactory, final ExecutorRejectedHandler handler) {
        return new ExecutorBuilder().corePoolSize(coreSize).maxPoolSize(maxSize).namePrefix(namePrefix).workQueueSize(workQueueSize).keepAliveTime(keepAliveTime).threadFactory(threadFactory)
                .handler(handler).build();
    }

    /**
     * Builder for {@link ExecutorService}.
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
            if (null == timeUnit) {
                timeUnit = TimeUnit.MILLISECONDS;
            }
            if (null == namePrefix) {
                namePrefix = "default";
            }
            if (null == workQueue) {
                workQueue = new ArrayBlockingQueue<>(workQueueSize);
            }
            if (null == threadFactory) {
                threadFactory = new DefaultThreadFactory(namePrefix);
            }
            return new ExecutorService(new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUnit, workQueue, threadFactory), handler);
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
        
        @SuppressWarnings("all")
        @Override
        public Thread newThread(final Runnable runnable) {
            Validator.notNull(runnable, "error : new thread runnable is null");
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
