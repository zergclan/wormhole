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

package com.zergclan.wormhole.common.generator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Snowflake id worker.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SnowflakeIdWorker {

    private static final SnowflakeIdWorker WORKER = new SnowflakeIdWorker();
    
    private static final long START_TIMESTAMP = 1489111610226L;
    
    private static final long WORKER_ID_BITS = 5L;
    
    private static final long DATA_CENTER_ID_BITS = 5L;
    
    private static final long SEQUENCE_BITS = 12L;

    private volatile long sequence;
    
    private volatile long lastTimestamp = -1L;
    
    /**
     * Generate long id.
     *
     * @return id
     */
    public static Long generateId() {
        return WORKER.nextId();
    }
    
    private synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("error : refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        final long sequenceMask = ~(-1L << SEQUENCE_BITS);
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        long dataCenterIdShift = SEQUENCE_BITS + WORKER_ID_BITS;
        long timestampLeftShift = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
        lastTimestamp = timestamp;
        return ((timestamp - START_TIMESTAMP) << timestampLeftShift) | (1 << dataCenterIdShift) | (1 << SEQUENCE_BITS) | sequence;
    }
    
    private long timeGen() {
        return System.currentTimeMillis();
    }
    
    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }
}
