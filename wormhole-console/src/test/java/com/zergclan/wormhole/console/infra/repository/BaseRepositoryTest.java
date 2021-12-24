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

package com.zergclan.wormhole.console.infra.repository;

import com.zergclan.wormhole.console.application.domain.entity.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
public final class BaseRepositoryTest {

    @Resource
    private BaseRepository<UserInfo> userInfoRepository;

    @Test
    public void assertBaseRepository() {
        Collection<UserInfo> userInfoBatch = createUserInfoBatch();
        userInfoRepository.addBatch(userInfoBatch);
        Collection<UserInfo> userInfos = userInfoRepository.listAll();
        assertEquals(userInfos.size(), userInfoRepository.count());
        Collection<Integer> ids = new LinkedList<>();
        for (UserInfo each : userInfos) {
            ids.add(each.getId());
        }
        Collection<UserInfo> list = userInfoRepository.list(ids);
        assertEquals(userInfos.size(), list.size());
    }

    private Collection<UserInfo> createUserInfoBatch() {
        Collection<UserInfo> result = new LinkedList<>();
        result.add(createUserInfo(2));
        result.add(createUserInfo(3));
        return result;
    }

    private UserInfo createUserInfo(final int index) {
        UserInfo result = new UserInfo();
        result.setUsername("test_username" + index);
        result.setPassword("123456");
        result.setEmail("test_email@163.com");
        result.setEnable(1);
        return result;
    }
}
