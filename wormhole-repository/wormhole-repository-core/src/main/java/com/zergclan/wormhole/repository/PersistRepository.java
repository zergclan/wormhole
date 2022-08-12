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

package com.zergclan.wormhole.repository;

import com.zergclan.wormhole.common.spi.annotation.SingletonSPI;
import com.zergclan.wormhole.common.spi.scene.typed.TypedSPI;

/**
 * The root interface from which all persist repository shall be derived in Wormhole.
 */
@SingletonSPI
public interface PersistRepository extends TypedSPI {
    
    /**
     * Init.
     */
    void init();
    
    /**
     * Create.
     *
     * @param key key
     * @param value value
     */
    void create(String key, String value);
    
    /**
     * Retrieve.
     *
     * @param key key
     * @return value of key
     */
    String retrieve(String key);
    
    /**
     * Update.
     *
     * @param key key
     * @param value value
     * @return rows of updated
     */
    int update(String key, String value);
    
    /**
     * Delete.
     *
     * @param key key
     * @return rows of removed
     */
    int delete(String key);
    
    /**
     * Close.
     */
    void close();
}
