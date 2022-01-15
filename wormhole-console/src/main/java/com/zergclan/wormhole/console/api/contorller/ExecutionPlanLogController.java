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

package com.zergclan.wormhole.console.api.contorller;

import com.zergclan.wormhole.console.api.vo.HttpResult;
import com.zergclan.wormhole.console.api.vo.PageQuery;
import com.zergclan.wormhole.console.application.domain.entity.ExecutionPlanLog;
import com.zergclan.wormhole.console.application.service.ExecutionPlanLogService;
import com.zergclan.wormhole.console.infra.repository.PageData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Controller of {@link ExecutionPlanLog}.
 */
@RestController
@RequestMapping("/log/execution/plan")
public class ExecutionPlanLogController extends AbstractRestController {
    
    @Resource
    private ExecutionPlanLogService executionPlanLogService;
    
    /**
     * Get {@link ExecutionPlanLog} by id.
     *
     * @param id id
     * @return {@link HttpResult}
     */
    @GetMapping("/{id}")
    public HttpResult<ExecutionPlanLog> getById(@PathVariable(value = "id") final Integer id) {
        return success(executionPlanLogService.getById(id));
    }
    
    /**
     * List {@link ExecutionPlanLog} by {@link PageQuery}.
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageData}
     */
    @PostMapping("/page")
    public HttpResult<PageData<ExecutionPlanLog>> listByPage(@RequestBody final PageQuery<ExecutionPlanLog> pageQuery) {
        return success(executionPlanLogService.listByPage(pageQuery));
    }
}
