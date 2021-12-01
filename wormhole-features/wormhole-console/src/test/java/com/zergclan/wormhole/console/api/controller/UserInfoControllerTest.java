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

package com.zergclan.wormhole.console.api.controller;

import com.zergclan.wormhole.console.WormholeETLApplication;
import com.zergclan.wormhole.console.api.vo.HttpResult;
import com.zergclan.wormhole.console.api.vo.PageVO;
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
@SpringBootTest(classes = {WormholeETLApplication.class})
public final class UserInfoControllerTest {
    
    private static final JsonConverter JSON_CONVERTER = JsonConverter.defaultInstance();
    
    @Resource
    private MockMvc mvc;
    
    @Test
    public void assertAdd() throws Exception {
        final UserInfo userInfo = new UserInfo();
        userInfo.setUsername("admin");
        userInfo.setPassword("admin");
        userInfo.setEmail("jacky7boy@163.com");
        String requestJson = JSON_CONVERTER.toJson(userInfo);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/user").contentType("application/json").content(requestJson)).andReturn();
        HttpResult<String> expectedResult = new HttpResult<String>().toBuilder().code(200).message("SUCCESS").data(null).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }
    
    @Test
    public void assertUpdate() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setUsername("admin");
        userInfo.setPassword("admin");
        userInfo.setEmail("jacky7boy@163.com");
        String requestJson = JSON_CONVERTER.toJson(userInfo);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.PUT, "/user").contentType("application/json").content(requestJson)).andReturn();
        HttpResult<String> expectedResult = new HttpResult<String>().toBuilder().code(200).message("SUCCESS").data(null).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void assertRemove() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(2);
        String requestJson = JSON_CONVERTER.toJson(userInfo);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, "/user").contentType("application/json").content(requestJson)).andReturn();
        HttpResult<String> expectedResult = new HttpResult<String>().toBuilder().code(200).message("SUCCESS").data(null).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void assertGetById() throws Exception {
        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/user/1").contentType("application/json").content("")).andReturn();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setUsername("jack");
        userInfo.setPassword("123456");
        userInfo.setEmail("jacky7boy@163.com");
        userInfo.setStatus(0);
        userInfo.setCreateTime(LocalDateTime.parse("2012-11-19 18:30:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        userInfo.setModifyTime(LocalDateTime.parse("2012-11-19 18:30:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        HttpResult<UserInfo> expectedResult = new HttpResult<UserInfo>().toBuilder().code(200).message("SUCCESS").data(userInfo).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void assertList() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/user/list").contentType("application/json").content("")).andReturn();
        HttpResult<List<UserInfo>> httpResult = JSON_CONVERTER.shallowParse(mvcResult.getResponse().getContentAsString(), HttpResult.class);
        assertEquals(12, httpResult.getData().size());
    }

    @Test
    public void assertPage() throws Exception {
        PageVO<UserInfo> pageVO = new PageVO<>();
        pageVO.setPage(1);
        pageVO.setSize(2);
        pageVO.setQuery(new UserInfo());
        String requestJson = JSON_CONVERTER.toJson(pageVO);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/user/page").contentType("application/json").content(requestJson)).andReturn();
        HttpResult<Object> httpResult = JSON_CONVERTER.shallowParse(mvcResult.getResponse().getContentAsString(), HttpResult.class);
        List<UserInfo> items = (ArrayList) ((LinkedHashMap) httpResult.getData()).get("items");
        assertEquals(2, items.size());
    }
}
