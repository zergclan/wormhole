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
import com.zergclan.wormhole.console.api.vo.LoginResult;
import com.zergclan.wormhole.console.api.vo.LoginVO;
import com.zergclan.wormhole.console.infra.util.JsonConverter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.Resource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
public final class LoginControllerTest {
    
    private static final JsonConverter JSON_CONVERTER = JsonConverter.defaultInstance();
    
    @Resource
    private MockMvc mvc;
    
    @Test
    public void assertLoginSuccess() throws Exception {
        LoginVO loginVO = new LoginVO();
        loginVO.setLoginName("root_test");
        loginVO.setPassword("123456");
        loginVO.setLoginType(0);
        String requestJson = JSON_CONVERTER.toJson(loginVO);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/security/login").contentType("application/json").content(requestJson)).andReturn();
        HttpResult<Map<String, String>> actualLogin = JSON_CONVERTER.shallowParse(mvcResult.getResponse().getContentAsString(), HttpResult.class);
        assertEquals(200, actualLogin.getCode());
        assertEquals("SUCCESS", actualLogin.getMessage());
        String token = actualLogin.getData().get("token");
        mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/security/logout").header("token", token).contentType("application/json")
                .content(requestJson)).andReturn();
        HttpResult<String> actualLogout = JSON_CONVERTER.shallowParse(mvcResult.getResponse().getContentAsString(), HttpResult.class);
        assertEquals(200, actualLogout.getCode());
        assertEquals("SUCCESS", actualLogout.getMessage());
    }
    
    @Test
    public void assertLoginUnauthorizedUsername() throws Exception {
        LoginVO loginVO = new LoginVO();
        loginVO.setLoginName("root_test");
        loginVO.setPassword("pwd_error");
        loginVO.setLoginType(0);
        String requestJson = JSON_CONVERTER.toJson(loginVO);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/security/login").contentType("application/json").content(requestJson)).andReturn();
        LoginResult loginResult = new LoginResult();
        loginResult.setToken(null);
        HttpResult<LoginResult> expectedResult = new HttpResult<LoginResult>().toBuilder().code(401).message("UNAUTHORIZED").data(loginResult).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }
    
    @Test
    public void assertLoginUnauthorizedPassword() throws Exception {
        LoginVO loginVO = new LoginVO();
        loginVO.setLoginName("root_error");
        loginVO.setPassword("123456");
        loginVO.setLoginType(0);
        String requestJson = JSON_CONVERTER.toJson(loginVO);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/security/login").contentType("application/json").content(requestJson)).andReturn();
        LoginResult loginResult = new LoginResult();
        loginResult.setToken(null);
        HttpResult<LoginResult> expectedResult = new HttpResult<LoginResult>().toBuilder().code(401).message("UNAUTHORIZED").data(loginResult).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }
}
