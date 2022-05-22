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
import com.zergclan.wormhole.console.api.vo.TaskExecutionDetail;
import com.zergclan.wormhole.console.application.domain.entity.DatasourceInfo;
import com.zergclan.wormhole.console.application.domain.log.ErrorDataLog;
import com.zergclan.wormhole.console.application.domain.log.PlanExecutionLog;
import com.zergclan.wormhole.console.application.domain.log.TaskExecutionLog;
import com.zergclan.wormhole.console.infra.repository.PageData;

public interface LogMetricsService {
    
    /**
     * List {@link PlanExecutionLog} by {@link PageQuery}.
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageData}
     */
    PageData<PlanExecutionLog> pagePlanExecutionLog(PageQuery<PlanExecutionLog> pageQuery);
    
    /**
     * List {@link TaskExecutionLog} by {@link PageQuery}.
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageData}
     */
    PageData<TaskExecutionLog> pageTaskExecutionLog(PageQuery<TaskExecutionLog> pageQuery);
    
    /**
     * Get {@link TaskExecutionDetail}.
     *
     * @param taskBatch taskBatch
     * @return {@link TaskExecutionDetail}
     */
    TaskExecutionDetail getTaskExecutionDetail(String taskBatch);
    
    /**
     * Add {@link ErrorDataLog}.
     *
     * @param errorDataLog {@link ErrorDataLog}
     */
    void add(ErrorDataLog errorDataLog);
    
    /**
     * List {@link DatasourceInfo} by {@link PageQuery}.
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageData}
     */
    PageData<ErrorDataLog> listByPage(PageQuery<ErrorDataLog> pageQuery);
}
