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

package com.zergclan.wormhole.jdbc;

import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.core.metadata.DataSourceMetadata;
import com.zergclan.wormhole.jdbc.core.DataSourceCreator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataSourceManger {

    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

    private static final Map<String, DataSource> CONTAINER = new LinkedHashMap<>();

    /**
     * Get {@link DataSource}.
     *
     * @param metadata {@link DataSourceMetadata}
     * @return {@link DataSource}
     */
    public static DataSource get(final DataSourceMetadata metadata) {
        LOCK.readLock().lock();
        try {
            String identifier = metadata.getIdentifier();
            DataSource dataSource = CONTAINER.get(identifier);
            if (null == dataSource) {
                dataSource = DataSourceCreator.create(metadata);
                CONTAINER.put(identifier, dataSource);
            }
            return dataSource;
        } finally {
            LOCK.readLock().unlock();
        }
    }

    /**
     * Register {@link DataSource}.
     *
     * @param metadata {@link DataSourceMetadata}
     * @return is registered or not
     */
    public static boolean register(final DataSourceMetadata metadata) {
        Validator.notNull(metadata, "error : register jdbc data source arg metadata can not be null");
        LOCK.writeLock().lock();
        try {
            if (CONTAINER.containsKey(metadata.getIdentifier())) {
                return false;
            }
            CONTAINER.put(metadata.getIdentifier(), DataSourceCreator.create(metadata));
            return true;
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    /**
     * Refresh {@link DataSource}.
     *
     * @param metadata {@link DataSourceMetadata}
     * @return is registered or not
     */
    public static boolean refresh(final DataSourceMetadata metadata) {
        Validator.notNull(metadata, "error : refresh jdbc data source arg metadata can not be null");
        try {
            CONTAINER.put(metadata.getIdentifier(), DataSourceCreator.create(metadata));
            return true;
        } finally {
            LOCK.writeLock().unlock();
        }
    }
}
