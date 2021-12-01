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
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest(classes = {WormholeETLApplication.class})
public final class UserInfoControllerTest {
    
    @Resource
    private MockMvc mvc;
    
    @Test
    public void assertAdd() throws Exception {
        String requestJson = "{\"userName\":\"admin\",\"password\":\"admin\",\"email\":\"jacky7boy@163.com\"}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/user").contentType("application/json").content(requestJson)).andReturn();
        String expectedResponse = "{\"code\":200,\"message\":\"SUCCESS\",\"data\":null}";
        assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString());
    }
    
    @Test
    public void assertUpdate() throws Exception {
        String requestJson = "{\"id\":1,\"userName\":\"admin\"}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.PUT, "/user").contentType("application/json").content(requestJson)).andReturn();
        String expectedResponse = "{\"code\":200,\"message\":\"SUCCESS\",\"data\":null}";
        assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString());
    }
    
    @Test
    public void assertGetById() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/user/1").contentType("application/json").content("")).andReturn();
        String expectedResponse = "{\"code\":200,\"message\":\"SUCCESS\",\"data\":{\"id\":1,\"userName\":\"jack\",\"password\":\"123456\",\"email\":\"jacky7boy@163.com\",\"status\":0,"
                + "\"createTime\":\"2012-11-19 18:30:30\",\"modifyTime\":\"2012-11-19 18:30:30\"}}";
        assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString());
    }
}
