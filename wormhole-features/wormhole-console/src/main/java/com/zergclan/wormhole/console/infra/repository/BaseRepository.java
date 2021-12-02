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

package com.zergclan.wormhole.console.infra.repository;

import com.zergclan.wormhole.console.api.vo.PageQuery;

import java.util.Collection;

/**
 * The root interface from which all repository shall be derived in Wormhole.
 *
 * @param <T> class type of persistent object
 */
public interface BaseRepository<T> {
    
    /**
     * Insert.
     *
     * @param po persistent object
     */
    void insert(T po);
    
    /**
     * Insert batch.
     *
     * @param batch batch persistent object
     */
    void insertBatch(Collection<T> batch);
    
    /**
     * Update by id.
     *
     * @param po persistent object
     * @return update rows
     */
    int updateById(T po);
    
    /**
     * Update.
     *
     * @param po persistent object
     * @return update rows
     */
    int update(T po);
    
    /**
     * Delete by id.
     *
     * @param id id
     */
    void delete(Integer id);
    
    /**
     * Get one by id.
     *
     * @param id id
     * @return persistent object
     */
    T get(Integer id);
    
    /**
     * Get one by query.
     *
     * @param po persistent object
     * @return persistent object
     */
    T getOne(T po);
    
    /**
     * Count all.
     *
     * @return rows
     */
    int count();
    
    /**
     * Count by query.
     *
     * @param po persistent object
     * @return rows
     */
    int countByQuery(T po);
    
    /**
     * List all.
     *
     * @return persistent objects
     */
    Collection<T> listAll();
    
    /**
     * List by query.
     *
     * @param po persistent object
     * @return persistent objects
     */
    Collection<T> listByQuery(T po);
    
    /**
     * page by page query.
     *
     * @param pageQuery page query
     * @return persistent objects
     */
    Collection<T> page(PageQuery<T> pageQuery);
}
