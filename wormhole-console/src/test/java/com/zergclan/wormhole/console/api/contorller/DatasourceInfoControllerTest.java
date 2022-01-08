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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zergclan.wormhole.console.api.vo.HttpResult;
import com.zergclan.wormhole.console.api.vo.PageQuery;
import com.zergclan.wormhole.console.application.domain.entity.DatasourceInfo;
import com.zergclan.wormhole.console.infra.util.JsonConverter;
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
@SpringBootTest
public final class DatasourceInfoControllerTest {
    
    private static final JsonConverter JSON_CONVERTER = JsonConverter.defaultInstance();
    
    @Resource
    private MockMvc mvc;
    
    @Test
    public void assertBase() throws Exception {
        DatasourceInfo datasourceInfo = createDatasourceInfo();
        assertGetById(datasourceInfo);
        assertList();
        assertPage();
        assertAdd(datasourceInfo);
        assertEditById(datasourceInfo);
        assertRemoveById();
    }
    
    private void assertRemoveById() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, "/config/datasource/2").header("token", "wormhole-console-test-token")
                .contentType("application/json").content("")).andReturn();
        HttpResult<String> expectedResult = new HttpResult<String>().toBuilder().code(200).message("SUCCESS").data(null).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }
    
    private void assertEditById(final DatasourceInfo datasourceInfo) throws Exception {
        datasourceInfo.setId(2);
        datasourceInfo.setDescription("test_datasource_info_description_update");
        String requestJson = JSON_CONVERTER.toJson(datasourceInfo);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.PUT, "/config/datasource").header("token", "wormhole-console-test-token")
                .contentType("application/json").content(requestJson)).andReturn();
        HttpResult<String> expectedResult = new HttpResult<String>().toBuilder().code(200).message("SUCCESS").data(null).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }
    
    private void assertAdd(final DatasourceInfo datasourceInfo) throws Exception {
        datasourceInfo.setSchema("test_target_ds");
        String requestJson = JSON_CONVERTER.toJson(datasourceInfo);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/config/datasource").header("token", "wormhole-console-test-token")
                .contentType("application/json").content(requestJson)).andReturn();
        HttpResult<String> expectedResult = new HttpResult<String>().toBuilder().code(200).message("SUCCESS").data(null).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }
    
    @SuppressWarnings("all")
    private void assertPage() throws Exception {
        PageQuery<DatasourceInfo> pageQuery = new PageQuery<>();
        pageQuery.setPage(1);
        pageQuery.setSize(2);
        pageQuery.setQuery(new DatasourceInfo());
        String requestJson = JSON_CONVERTER.toJson(pageQuery);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/config/datasource/page").header("token", "wormhole-console-test-token")
                .contentType("application/json").content(requestJson)).andReturn();
        HttpResult<Object> httpResult = JSON_CONVERTER.shallowParse(mvcResult.getResponse().getContentAsString(), HttpResult.class);
        List<Map<String, Object>> items = (ArrayList) ((LinkedHashMap) httpResult.getData()).get("items");
        assertEquals(1, items.size());
        assertEquals("MySQL#127.0.0.1:3306", items.get(0).get("owner"));
    }
    
    @SuppressWarnings("all")
    private void assertList() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/config/datasource/list").header("token", "wormhole-console-test-token")
                .contentType("application/json").content("")).andReturn();
        HttpResult<List<DatasourceInfo>> httpResult = JSON_CONVERTER.shallowParse(mvcResult.getResponse().getContentAsString(), HttpResult.class);
        assertEquals(1, httpResult.getData().size());
    }
    
    private void assertGetById(final DatasourceInfo datasourceInfo) throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/config/datasource/1").header("token", "wormhole-console-test-token")
                .contentType("application/json").content("")).andReturn();
        HttpResult<DatasourceInfo> expectedResult = new HttpResult<DatasourceInfo>().toBuilder().code(200).message("SUCCESS").data(datasourceInfo).build();
        assertEquals(JSON_CONVERTER.toJson(expectedResult), mvcResult.getResponse().getContentAsString());
    }
    
    private DatasourceInfo createDatasourceInfo() {
        DatasourceInfo datasourceInfo = new DatasourceInfo();
        datasourceInfo.setId(1);
        datasourceInfo.setDatabaseId(1);
        datasourceInfo.setSchema("test_source_ds");
        datasourceInfo.setUsername("root");
        datasourceInfo.setPassword("123456");
        datasourceInfo.setExtendParameters("characterEncoding=UTF-8,serverTimezone=UTC");
        datasourceInfo.setDescription("test_datasource_info_description");
        datasourceInfo.setOperator(0);
        datasourceInfo.setCreateTime(LocalDateTime.parse("2012-12-31 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        datasourceInfo.setModifyTime(LocalDateTime.parse("2012-12-31 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return datasourceInfo;
    }
}
