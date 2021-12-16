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
import com.zergclan.wormhole.console.application.domain.entity.DatabaseInfo;
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
public final class DatabaseInfoControllerTest {

    private static final JsonConverter JSON_CONVERTER = JsonConverter.defaultInstance();

    @Resource
    private MockMvc mvc;

    @Test
    public void assertBase() throws Exception {
        DatabaseInfo databaseInfo = createDatabaseInfo();
        assertGetById(databaseInfo);
        assertList();
        assertPage();
        assertAdd(databaseInfo);
        assertEditById(databaseInfo);
        assertRemoveById();
    }

    private DatabaseInfo createDatabaseInfo() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setId(1);
        databaseInfo.setTitle("database_info_test_title");
        databaseInfo.setHost("127.0.0.1");
        databaseInfo.setPort(3306);
        databaseInfo.setUsername("root");
        databaseInfo.setPassword("123456");
        databaseInfo.setDescription("database_info_test_description");
        databaseInfo.setType(0);
        databaseInfo.setOperator(0);
        databaseInfo.setCreateTime(LocalDateTime.parse("2012-12-31 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        databaseInfo.setModifyTime(LocalDateTime.parse("2012-12-31 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return databaseInfo;
    }

    private void assertGetById(final DatabaseInfo databaseInfo) throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/database/1").header("token", "wormhole-console-test-token")
                .contentType("application/json").content("")).andReturn();
        HttpResult<DatabaseInfo> expectedResult = new HttpResult<DatabaseInfo>().toBuilder().code(200).message("SUCCESS").data(databaseInfo).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }

    private void assertList() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/database/list").header("token", "wormhole-console-test-token")
                .contentType("application/json").content("")).andReturn();
        HttpResult<List<DatabaseInfo>> httpResult = JSON_CONVERTER.shallowParse(mvcResult.getResponse().getContentAsString(), HttpResult.class);
        assertEquals(1, httpResult.getData().size());
    }

    private void assertPage() throws Exception {
        PageQuery<DatabaseInfo> pageQuery = new PageQuery<>();
        pageQuery.setPage(1);
        pageQuery.setSize(2);
        pageQuery.setQuery(new DatabaseInfo());
        String requestJson = JSON_CONVERTER.toJson(pageQuery);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/database/page").header("token", "wormhole-console-test-token")
                .contentType("application/json").content(requestJson)).andReturn();
        HttpResult<Object> httpResult = JSON_CONVERTER.shallowParse(mvcResult.getResponse().getContentAsString(), HttpResult.class);
        List<DatabaseInfo> items = (ArrayList) ((LinkedHashMap) httpResult.getData()).get("items");
        assertEquals(1, items.size());
    }

    private void assertAdd(final DatabaseInfo databaseInfo) throws Exception {
        databaseInfo.setTitle("database_info_test_title1");
        databaseInfo.setPort(3307);
        String requestJson = JSON_CONVERTER.toJson(databaseInfo);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/database").header("token", "wormhole-console-test-token")
                .contentType("application/json").content(requestJson)).andReturn();
        HttpResult<String> expectedResult = new HttpResult<String>().toBuilder().code(200).message("SUCCESS").data(null).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }

    private void assertEditById(final DatabaseInfo databaseInfo) throws Exception {
        databaseInfo.setId(2);
        databaseInfo.setDescription("database_info_description_update");
        String requestJson = JSON_CONVERTER.toJson(databaseInfo);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.PUT, "/database").header("token", "wormhole-console-test-token")
                .contentType("application/json").content(requestJson)).andReturn();
        HttpResult<String> expectedResult = new HttpResult<String>().toBuilder().code(200).message("SUCCESS").data(null).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }

    private void assertRemoveById() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, "/database/2").header("token", "wormhole-console-test-token")
                .contentType("application/json").content("")).andReturn();
        HttpResult<String> expectedResult = new HttpResult<String>().toBuilder().code(200).message("SUCCESS").data(null).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }
}
