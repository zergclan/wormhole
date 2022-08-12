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

package com.zergclan.wormhole.repository.jdbc;

import com.zergclan.wormhole.repository.PersistRepository;

/**
 * JDBC implemented of {@link PersistRepository}.
 */
public final class JDBCPersistRepository implements PersistRepository {
    
    @Override
    public void init() {
    
    }
    
    @Override
    public void create(final String key, final String value) {
    
    }
    
    @Override
    public String retrieve(final String key) {
        return null;
    }
    
    @Override
    public int update(final String key, final String value) {
        return 0;
    }
    
    @Override
    public int delete(final String key) {
        return 0;
    }
    
    @Override
    public void close() {
    
    }
    
    @Override
    public String getType() {
        return "JDBC";
    }
}
