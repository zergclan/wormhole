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

package com.zergclan.wormhole.web.api.controller;

import com.zergclan.wormhole.web.WormholeETLApplication;
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
public final class LoginControllerTest {
    
    @Resource
    private MockMvc mvc;
    
    @Test
    public void assertLoginSuccess() throws Exception {
        String requestSuccessJson = "{\"loginName\":\"admin\",\"password\":\"admin\",\"loginType\":0}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/login").contentType("application/json").content(requestSuccessJson)).andReturn();
        assertEquals("{\"code\":200,\"message\":\"SUCCESS\",\"data\":\"S\"}", mvcResult.getResponse().getContentAsString());
        
    }
    
    @Test
    public void assertLoginUnauthorized() throws Exception {
        String requestJson = "{\"loginName\":\"admin\",\"password\":\"admin\",\"loginType\":1}";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/login").contentType("application/json").content(requestJson)).andReturn();
        assertEquals("{\"code\":401,\"message\":\"UNAUTHORIZED\",\"data\":\"F\"}", mvcResult.getResponse().getContentAsString());
        
    }
}
