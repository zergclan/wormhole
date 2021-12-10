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
import com.zergclan.wormhole.console.application.domain.entity.UserInfo;
import com.zergclan.wormhole.console.infra.util.JsonConverter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
public final class UserInfoControllerTest {
    
    private static final JsonConverter JSON_CONVERTER = JsonConverter.defaultInstance();
    
    @Resource
    private MockMvc mvc;
    
    @Test
    public void assertUserInfoController() throws Exception {
        assertGetById();
        assertList();
        assertPage();
        assertAdd();
        assertUpdate();
        assertRemove();
    }
    
    private void assertList() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/user/list").header("token", "wormhole-console-test-token")
                .contentType("application/json").content("")).andReturn();
        HttpResult<List<UserInfo>> httpResult = JSON_CONVERTER.shallowParse(mvcResult.getResponse().getContentAsString(), HttpResult.class);
        assertEquals(1, httpResult.getData().size());
    }
    
    private void assertPage() throws Exception {
        PageQuery<UserInfo> pageQuery = new PageQuery<>();
        pageQuery.setPage(1);
        pageQuery.setSize(2);
        pageQuery.setQuery(new UserInfo());
        String requestJson = JSON_CONVERTER.toJson(pageQuery);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/user/page").header("token", "wormhole-console-test-token")
                .contentType("application/json").content(requestJson)).andReturn();
        HttpResult<Object> httpResult = JSON_CONVERTER.shallowParse(mvcResult.getResponse().getContentAsString(), HttpResult.class);
        List<UserInfo> items = (ArrayList) ((LinkedHashMap) httpResult.getData()).get("items");
        assertEquals(1, items.size());
    }
    
    private void assertGetById() throws Exception {
        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/user/1").header("token", "wormhole-console-test-token")
                .contentType("application/json").content("")).andReturn();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setUsername("root_test");
        userInfo.setPassword("123456");
        userInfo.setEmail("example@163.com");
        userInfo.setEnable(0);
        userInfo.setCreateTime(LocalDateTime.parse("2012-11-19 18:30:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        userInfo.setModifyTime(LocalDateTime.parse("2012-11-19 18:30:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        HttpResult<UserInfo> expectedResult = new HttpResult<UserInfo>().toBuilder().code(200).message("SUCCESS").data(userInfo).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }
    
    private void assertAdd() throws Exception {
        final UserInfo userInfo = new UserInfo();
        userInfo.setUsername("root_test2");
        userInfo.setPassword("root_test2");
        userInfo.setEmail("example@163.com");
        String requestJson = JSON_CONVERTER.toJson(userInfo);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/user").header("token", "wormhole-console-test-token")
                .contentType("application/json").content(requestJson)).andReturn();
        HttpResult<String> expectedResult = new HttpResult<String>().toBuilder().code(200).message("SUCCESS").data(null).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }
    
    private void assertUpdate() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("admin_test2");
        userInfo.setPassword("admin_test2");
        userInfo.setEmail("example@163.com");
        String requestJson = JSON_CONVERTER.toJson(userInfo);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.PUT, "/user/2").header("token", "wormhole-console-test-token")
                .contentType("application/json").content(requestJson)).andReturn();
        HttpResult<String> expectedResult = new HttpResult<String>().toBuilder().code(200).message("SUCCESS").data(null).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }
    
    private void assertRemove() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, "/user/2").header("token", "wormhole-console-test-token")
                .contentType("application/json").content("")).andReturn();
        HttpResult<String> expectedResult = new HttpResult<String>().toBuilder().code(200).message("SUCCESS").data(null).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }
}
