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

package com.zerg.wormhole.web.contorller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class DiscoveryControllerTest {
    
    private static MockMvc mockMvc;
    
    @BeforeAll
    static void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(DiscoveryController.class).build();
    }
    
    @Test
    public void assertStatus() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/status").contentType("application/json").content("")).andExpect(MockMvcResultMatchers.status()
                .isOk()).andReturn();
        assertEquals("UP", mvcResult.getResponse().getContentAsString());
    }
}
