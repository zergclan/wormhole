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

package com.zergclan.wormhole.console.application.service.impl;

import com.zergclan.wormhole.console.api.vo.PageQuery;
import com.zergclan.wormhole.console.application.domain.entity.ExecutionPlanLog;
import com.zergclan.wormhole.console.application.service.ExecutionPlanLogService;
import com.zergclan.wormhole.console.infra.repository.BaseRepository;
import com.zergclan.wormhole.console.infra.repository.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Implemented Service of {@link ExecutionPlanLogService}.
 */
@Service(value = "executionPlanLogService")
public class ExecutionPlanLogServiceImpl implements ExecutionPlanLogService {
    
    @Resource
    private BaseRepository<ExecutionPlanLog> executionPlanLogRepository;
    
    @Override
    public ExecutionPlanLog getById(final Integer id) {
        return executionPlanLogRepository.get(id);
    }
    
    @Override
    public PageData<ExecutionPlanLog> listByPage(final PageQuery<ExecutionPlanLog> pageQuery) {
        return executionPlanLogRepository.listByPage(pageQuery);
    }
}
