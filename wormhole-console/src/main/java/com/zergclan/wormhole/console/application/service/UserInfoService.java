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

package com.zergclan.wormhole.console.application.service;

import com.zergclan.wormhole.console.api.vo.PageQuery;
import com.zergclan.wormhole.console.application.domain.entity.UserInfo;
import com.zergclan.wormhole.console.infra.repository.PageData;

import java.util.Collection;

/**
 * Service interface of {@link UserInfo}.
 */
public interface UserInfoService {

    /**
     * Add {@link UserInfo}.
     *
     * @param userInfo {@link UserInfo}
     */
    void add(UserInfo userInfo);

    /**
     * Edit {@link UserInfo} by id.
     *
     * @param id id
     * @param userInfo {@link UserInfo}
     * @return is updated or not
     */
    boolean editById(Integer id, UserInfo userInfo);
    
    /**
     * Remove {@link UserInfo} by id.
     *
     * @param id id
     * @return is removed or not
     */
    boolean removeById(Integer id);

    /**
     * Get {@link UserInfo} by id.
     *
     * @param id id
     * @return {@link UserInfo}
     */
    UserInfo getById(Integer id);

    /**
     * Get list of {@link UserInfo}.
     *
     * @return {@link UserInfo}
     */
    Collection<UserInfo> listAll();

    /**
     * Get {@link PageData} by {@link PageQuery}.
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageData}
     */
    PageData<UserInfo> listByPage(PageQuery<UserInfo> pageQuery);
}
