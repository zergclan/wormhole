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

import com.zergclan.wormhole.console.api.vo.PageVO;
import com.zergclan.wormhole.console.application.domain.entity.UserInfo;
import com.zergclan.wormhole.console.application.service.UserInfoService;
import com.zergclan.wormhole.console.infra.repository.PageData;
import com.zergclan.wormhole.console.infra.repository.PageQuery;
import com.zergclan.wormhole.console.infra.repository.impl.UserInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implemented Service of {@link UserInfoService}.
 */
@Service(value = "userInfoService")
public final class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;
    
    @Override
    public void add(@RequestBody final UserInfo userInfo) {
        userInfo.setStatus(0);
        LocalDateTime now = LocalDateTime.now();
        userInfo.setCreateTime(now);
        userInfo.setModifyTime(now);
        userInfoMapper.insert(userInfo);
    }
    
    @Override
    public boolean edit(final UserInfo userInfo) {
        userInfo.setModifyTime(LocalDateTime.now());
        return userInfoMapper.updateById(userInfo) == 1;
    }

    @Override
    public void remove(final UserInfo userInfo) {
        userInfoMapper.delete(userInfo.getId());
    }

    @Override
    public UserInfo getById(final Integer id) {
        return userInfoMapper.get(id);
    }

    @Override
    public List<UserInfo> listAll() {
        Collection<UserInfo> userInfos = userInfoMapper.listAll();
        return null == userInfos ? new ArrayList<>() : new ArrayList<>(userInfos);
    }

    @Override
    public PageData<UserInfo> listByPage(final PageVO<UserInfo> pageVO) {
        Integer page = pageVO.getPage();
        Integer size = pageVO.getSize();
        UserInfo query = pageVO.getQuery();
        PageData<UserInfo> result = new PageData<>(page, size);
        int total = userInfoMapper.countByQuery(query);
        if (0 == total) {
            return result.initData(total, new ArrayList<>());
        }
        PageQuery<UserInfo> pageQuery = new PageQuery<>(page, size);
        pageQuery.setQuery(query);
        return result.initData(total, userInfoMapper.page(pageQuery));
    }
}
