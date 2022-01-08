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
import com.zergclan.wormhole.console.application.domain.entity.TaskInfo;
import com.zergclan.wormhole.console.infra.repository.PageData;

import java.util.Collection;

/**
 * Service interface of {@link TaskInfo}.
 */
public interface TaskInfoService {
    
    /**
     * Add {@link TaskInfo}.
     *
     * @param taskInfo {@link TaskInfo}
     */
    void add(TaskInfo taskInfo);
    
    /**
     * Edit {@link TaskInfo} by id.
     *
     * @param taskInfo {@link TaskInfo}
     * @return is edited or not
     */
    boolean editById(TaskInfo taskInfo);
    
    /**
     * remove {@link TaskInfo} by id.
     *
     * @param id id
     * @return is removed or not
     */
    boolean removeById(Integer id);
    
    /**
     * Get {@link TaskInfo} by id.
     *
     * @param id id
     * @return {@link TaskInfo}
     */
    TaskInfo getById(Integer id);
    
    /**
     * List {@link TaskInfo}.
     *
     * @return {@link TaskInfo}
     */
    Collection<TaskInfo> listAll();
    
    /**
     * Get {@link PageData} by {@link PageQuery}.
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageData}
     */
    PageData<TaskInfo> listByPage(PageQuery<TaskInfo> pageQuery);
}
