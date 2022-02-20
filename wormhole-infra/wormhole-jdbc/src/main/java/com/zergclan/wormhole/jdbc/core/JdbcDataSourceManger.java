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

package com.zergclan.wormhole.jdbc.core;

import com.zergclan.wormhole.jdbc.api.DataSourceManger;
import com.zergclan.wormhole.common.util.Validator;
import com.zergclan.wormhole.core.metadata.DataSourceMetadata;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class JdbcDataSourceManger implements DataSourceManger<DataSource> {

    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

    private static final Map<String, DataSource> CONTAINER = new ConcurrentHashMap<>();

    @Override
    public Optional<DataSource> get(final String identifier) {
        Validator.notNull(identifier, "error : get jdbc data source arg identifier can not be null");
        LOCK.readLock().lock();
        try {
            DataSource dataSource = CONTAINER.get(identifier);
            return null == dataSource ? Optional.empty() : Optional.of(dataSource);
        } finally {
            LOCK.readLock().unlock();
        }
    }

    @Override
    public boolean register(final DataSourceMetadata metadata) {
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

    @Override
    public boolean refresh(final DataSourceMetadata metadata) {
        Validator.notNull(metadata, "error : refresh jdbc data source arg metadata can not be null");
        try {
            CONTAINER.put(metadata.getIdentifier(), DataSourceCreator.create(metadata));
            return true;
        } finally {
            LOCK.writeLock().unlock();
        }
    }
}
