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
import com.zergclan.wormhole.console.application.domain.entity.ExecutionTaskLog;
import com.zergclan.wormhole.console.application.service.ExecutionTaskLogService;
import com.zergclan.wormhole.console.infra.repository.BaseRepository;
import com.zergclan.wormhole.console.infra.repository.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Implemented Service of {@link ExecutionTaskLogService}.
 */
@Service(value = "executionTaskLogService")
public class ExecutionTaskLogServiceImpl implements ExecutionTaskLogService {
    
    @Resource
    private BaseRepository<ExecutionTaskLog> executionTaskLogRepository;
    
    @Override
    public ExecutionTaskLog getById(final Integer id) {
        return executionTaskLogRepository.get(id);
    }
    
    @Override
    public PageData<ExecutionTaskLog> listByPage(final PageQuery<ExecutionTaskLog> pageQuery) {
        return executionTaskLogRepository.listByPage(pageQuery);
    }
}
