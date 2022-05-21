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

package com.zergclan.wormhole.console.application.service.log;

import com.zergclan.wormhole.console.api.vo.PageQuery;
import com.zergclan.wormhole.console.application.domain.entity.DatasourceInfo;
import com.zergclan.wormhole.console.application.domain.entity.PlanInfo;
import com.zergclan.wormhole.console.application.domain.log.ErrorDataLog;
import com.zergclan.wormhole.console.infra.repository.PageData;

/**
 * Service interface of {@link ErrorDataLog}.
 */
public interface ErrorDataLogService {
    
    /**
     * Add {@link ErrorDataLog}.
     *
     * @param errorDataLog {@link ErrorDataLog}
     */
    void add(ErrorDataLog errorDataLog);
    
    /**
     * remove {@link PlanInfo} by id.
     *
     * @param id id
     * @return is removed or not
     */
    boolean removeById(Integer id);
    
    /**
     * List {@link DatasourceInfo} by {@link PageQuery}.
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageData}
     */
    PageData<ErrorDataLog> listByPage(PageQuery<ErrorDataLog> pageQuery);
}
