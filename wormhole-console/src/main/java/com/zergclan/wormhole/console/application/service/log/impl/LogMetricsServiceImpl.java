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

import com.zergclan.wormhole.bootstrap.scheduling.ExecutionState;
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
import java.util.Objects;

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
                if (ExecutionState.RUN.name().equals(task.getExecutionState())) {
                    continue;
                    
                }
                if (ExecutionState.SUCCESS.name().equals(task.getExecutionState())) {
                    each.addSuccessTasks(task.getTaskIdentifier());
                } else {
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
            result.setTaskBatch(taskBatch);
            result.setTotalRow(0);
            result.setInsertRow(0);
            result.setUpdateRow(0);
            result.setErrorRow(0);
            result.setSameRow(0);
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
        Long taskBatch = dataGroupExecutionLog.getTaskBatch();
        Integer batchIndex = dataGroupExecutionLog.getBatchIndex();
        DataGroupExecutionLog queryDataGroup = new DataGroupExecutionLog();
        queryDataGroup.setTaskBatch(taskBatch);
        queryDataGroup.setBatchIndex(batchIndex);
        DataGroupExecutionLog dataGroupLog = dataGroupExecutionLogRepository.getOne(queryDataGroup);
        if (Objects.isNull(dataGroupLog)) {
            dataGroupExecutionLogRepository.add(dataGroupExecutionLog);
            return;
        }
        dataGroupExecutionLogRepository.edit(dataGroupLog.getId(), dataGroupExecutionLog);
        TaskExecutionLog queryTask = new TaskExecutionLog();
        queryTask.setTaskBatch(taskBatch);
        TaskExecutionLog taskExecutionLog = taskExecutionLogRepository.getOne(queryTask);
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
        PlanExecutionLog query = new PlanExecutionLog();
        query.setPlanBatch(planExecutionLog.getPlanBatch());
        Integer id = planExecutionLogRepository.getOne(query).getId();
        planExecutionLog.setId(id);
        planExecutionLogRepository.edit(id, planExecutionLog);
    }
    
    @Override
    public void syncExecutionLog(final TaskExecutionLog taskExecutionLog) {
        TaskExecutionLog query = new TaskExecutionLog();
        query.setTaskBatch(taskExecutionLog.getTaskBatch());
        Integer id = taskExecutionLogRepository.getOne(query).getId();
        taskExecutionLog.setId(id);
        taskExecutionLogRepository.edit(id, taskExecutionLog);
    }
    
    @Override
    public PageData<ErrorDataLog> listByPage(final PageQuery<ErrorDataLog> pageQuery) {
        return errorDataLogRepository.listByPage(pageQuery);
    }

}
