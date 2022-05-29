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

package com.zergclan.wormhole.console.application.service.log.impl;

import com.zergclan.wormhole.bootstrap.scheduling.event.TaskCompletedEvent;
import com.zergclan.wormhole.bus.memory.WormholeEventBus;
import com.zergclan.wormhole.console.api.vo.PageQuery;
import com.zergclan.wormhole.console.api.vo.TaskExecutionDetail;
import com.zergclan.wormhole.console.application.domain.log.DataGroupExecutionLog;
import com.zergclan.wormhole.console.application.domain.log.ErrorDataLog;
import com.zergclan.wormhole.console.application.domain.log.PlanExecutionLog;
import com.zergclan.wormhole.console.application.domain.log.TaskExecutionLog;
import com.zergclan.wormhole.console.application.service.log.LogMetricsService;
import com.zergclan.wormhole.console.infra.repository.BaseRepository;
import com.zergclan.wormhole.console.infra.repository.PageData;
import com.zergclan.wormhole.console.infra.util.BeanMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

@Service("logMetricsService")
public class LogMetricsServiceImpl implements LogMetricsService {
    
    @Resource
    private BaseRepository<PlanExecutionLog> planExecutionLogRepository;
    
    @Resource
    private BaseRepository<TaskExecutionLog> taskExecutionLogRepository;
    
    @Resource
    private BaseRepository<DataGroupExecutionLog> dataGroupExecutionLogRepository;
    
    @Resource
    private BaseRepository<ErrorDataLog> errorDataLogRepository;
    
    @Override
    public PageData<PlanExecutionLog> pagePlanExecutionLog(final PageQuery<PlanExecutionLog> pageQuery) {
        PageData<PlanExecutionLog> result = planExecutionLogRepository.listByPage(pageQuery);
        Collection<PlanExecutionLog> items = result.getItems();
        for (PlanExecutionLog each : items) {
            Long planBatch = each.getPlanBatch();
            TaskExecutionLog query = new TaskExecutionLog();
            query.setPlanBatch(planBatch);
            Collection<TaskExecutionLog> list = taskExecutionLogRepository.list(query);
            for (TaskExecutionLog task : list) {
                if ("S".equals(task.getExecutionState())) {
                    each.addSuccessTasks(task.getTaskIdentifier());
                }
                if ("F".equals(task.getExecutionState())) {
                    each.addFailedTasks(task.getTaskIdentifier());
                }
            }
        }
        return result;
    }
    
    @Override
    public PageData<TaskExecutionLog> pageTaskExecutionLog(final PageQuery<TaskExecutionLog> pageQuery) {
        return taskExecutionLogRepository.listByPage(pageQuery);
    }
    
    @Override
    public TaskExecutionDetail getTaskExecutionDetail(final Long taskBatch) {
        TaskExecutionDetail result = new TaskExecutionDetail();
        DataGroupExecutionLog query = new DataGroupExecutionLog();
        query.setTaskBatch(taskBatch);
        DataGroupExecutionLog dataGroupExecutionLog = dataGroupExecutionLogRepository.getOne(query);
        if (null == dataGroupExecutionLog) {
            return result;
        }
        BeanMapper.shallowCopy(dataGroupExecutionLog, result);
        result.setTaskBatch(taskBatch);
        return result;
    }
    
    @Override
    public void add(final ErrorDataLog errorDataLog) {
        errorDataLogRepository.add(errorDataLog);
    }
    
    @Override
    public void add(final PlanExecutionLog planExecutionLog) {
        planExecutionLogRepository.add(planExecutionLog);
    }
    
    @Override
    public void add(final TaskExecutionLog taskExecutionLog) {
        taskExecutionLogRepository.add(taskExecutionLog);
    }

    @Override
    public void add(final DataGroupExecutionLog dataGroupExecutionLog) {
        dataGroupExecutionLogRepository.add(dataGroupExecutionLog);
        TaskExecutionLog query = new TaskExecutionLog();
        query.setTaskBatch(dataGroupExecutionLog.getTaskBatch());
        TaskExecutionLog taskExecutionLog = taskExecutionLogRepository.getOne(query);
        int remainingRow = Math.subtractExact(taskExecutionLog.getRemainingRow(), dataGroupExecutionLog.getTotalRow());
        taskExecutionLog.setRemainingRow(remainingRow);
        taskExecutionLogRepository.edit(taskExecutionLog.getId(), taskExecutionLog);
        if (remainingRow > 0) {
            return;
        }
        WormholeEventBus.post(new TaskCompletedEvent(taskExecutionLog.getTaskIdentifier()));
    }
    
    @Override
    public void syncExecutionLog(final PlanExecutionLog planExecutionLog) {
        Integer id = planExecutionLogRepository.getOne(planExecutionLog).getId();
        planExecutionLog.setId(id);
        planExecutionLogRepository.edit(id, planExecutionLog);
    }
    
    @Override
    public void syncExecutionLog(final TaskExecutionLog taskExecutionLog) {
        Integer id = taskExecutionLogRepository.getOne(taskExecutionLog).getId();
        taskExecutionLog.setId(id);
        taskExecutionLogRepository.edit(id, taskExecutionLog);
    }
    
    @Override
    public PageData<ErrorDataLog> listByPage(final PageQuery<ErrorDataLog> pageQuery) {
        return errorDataLogRepository.listByPage(pageQuery);
    }

}
