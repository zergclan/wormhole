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
import com.zergclan.wormhole.console.application.domain.entity.UserInfo;
import com.zergclan.wormhole.console.application.service.UserInfoService;
import com.zergclan.wormhole.console.infra.repository.BaseRepository;
import com.zergclan.wormhole.console.infra.repository.PageData;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * Implemented Service of {@link UserInfoService}.
 */
@Service(value = "userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
    
    @Resource
    private BaseRepository<UserInfo> userInfoRepository;
    
    @Override
    public void add(@RequestBody final UserInfo userInfo) {
        userInfo.setEnable(0);
        userInfoRepository.add(userInfo);
    }
    
    @Override
    public boolean editById(final Integer id, final UserInfo userInfo) {
        return userInfoRepository.edit(id, userInfo);
    }
    
    @Override
    public boolean removeById(final Integer id) {
        userInfoRepository.remove(id);
        return false;
    }
    
    @Override
    public UserInfo getById(final Integer id) {
        return userInfoRepository.get(id);
    }
    
    @Override
    public Collection<UserInfo> listAll() {
        return userInfoRepository.listAll();
    }
    
    @Override
    public PageData<UserInfo> listByPage(final PageQuery<UserInfo> pageQuery) {
        return userInfoRepository.listByPage(pageQuery);
    }
}
