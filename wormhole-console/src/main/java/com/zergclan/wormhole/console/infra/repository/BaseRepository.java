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
     * Add.
     *
     * @param po persistent object
     */
    void add(T po);
    
    /**
     * Add batch.
     *
     * @param batch batch persistent object
     */
    void addBatch(Collection<T> batch);
    
    /**
     * Edit by id.
     *
     * @param id id
     * @param po persistent object
     * @return is edited or not
     */
    boolean edit(Integer id, T po);
    
    /**
     * Delete by id.
     *
     * @param id id
     * @return is removed or not
     */
    boolean remove(Integer id);
    
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
     * List all.
     *
     * @return persistent objects
     */
    Collection<T> listAll();
    
    /**
     * List by ids.
     *
     * @param ids ids
     * @return persistent objects
     */
    Collection<T> list(Collection<Integer> ids);
    
    /**
     * page by page query.
     *
     * @param pageQuery page query
     * @return persistent objects
     */
    PageData<T> listByPage(PageQuery<T> pageQuery);
}
