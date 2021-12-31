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
import com.zergclan.wormhole.console.application.domain.entity.PlanInfo;
import com.zergclan.wormhole.console.application.domain.entity.PlanTaskLinking;
import com.zergclan.wormhole.console.application.domain.entity.TaskInfo;
import com.zergclan.wormhole.console.application.service.PlanInfoService;
import com.zergclan.wormhole.console.infra.repository.BaseRepository;
import com.zergclan.wormhole.console.infra.repository.PageData;
import com.zergclan.wormhole.context.scheduling.SchedulingManager;
import com.zergclan.wormhole.context.scheduling.plan.PlanDefinition;
import com.zergclan.wormhole.context.scheduling.plan.PlanSchedulingManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Implemented Service of {@link PlanInfoService}.
 */
@Service(value = "planInfoService")
public class PlanInfoServiceImpl implements PlanInfoService {
    
    @Resource
    private BaseRepository<PlanInfo> planInfoRepository;
    
    @Resource
    private BaseRepository<PlanTaskLinking> planTaskLinkingRepository;
    
    @Resource
    private BaseRepository<TaskInfo> taskInfoRepository;
    
    @Override
    public void add(final PlanInfo planInfo) {
        planInfoRepository.add(planInfo);
    }
    
    @Override
    public boolean editById(final PlanInfo planInfo) {
        return planInfoRepository.edit(planInfo.getId(), planInfo);
    }
    
    @Override
    public boolean removeById(final Integer id) {
        return planInfoRepository.remove(id);
    }
    
    @Override
    public PlanInfo getById(final Integer id) {
        return planInfoRepository.get(id);
    }
    
    @Override
    public Collection<PlanInfo> listAll() {
        return planInfoRepository.listAll();
    }
    
    @Override
    public PageData<PlanInfo> listByPage(final PageQuery<PlanInfo> pageQuery) {
        return planInfoRepository.listByPage(pageQuery);
    }
    
    @Override
    public void triggerById(final Integer id) {
        PlanInfo planInfo = planInfoRepository.get(id);
        Optional<Collection<Integer>> taskIds = listTaskIds(planInfo);
        if (taskIds.isPresent()) {
            PlanDefinition planDefinition = initPlanDefinition(planInfo);
            SchedulingManager<PlanDefinition> schedulingManager = new PlanSchedulingManager();
            schedulingManager.register(planDefinition);
        }
    }

    private PlanDefinition initPlanDefinition(final PlanInfo planInfo) {
        // TODO
        return new PlanDefinition(planInfo.getCode(), new LinkedList<>());
    }

    private Collection<String> initTaskCodes(final Collection<TaskInfo> taskInfos) {
        Collection<String> result = new LinkedList<>();
        for (TaskInfo each : taskInfos) {
            result.add(each.getCode());
        }
        return result;
    }
    
    private Optional<Collection<Integer>> listTaskIds(final PlanInfo planInfo) {
        PlanTaskLinking query = new PlanTaskLinking();
        query.setPlanId(planInfo.getId());
        Collection<PlanTaskLinking> planTaskLinking = planTaskLinkingRepository.list(query);
        return planTaskLinking.isEmpty() ? Optional.empty() : Optional.of(initTaskIds(planTaskLinking));
    }
    
    private Collection<Integer> initTaskIds(final Collection<PlanTaskLinking> planTaskLinking) {
        Collection<Integer> result = new LinkedList<>();
        for (PlanTaskLinking each : planTaskLinking) {
            result.add(each.getTaskId());
        }
        return result;
    }
}
