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

package com.zergclan.wormhole.console.infra.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.zergclan.wormhole.console.application.domain.entity.UserInfo;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public final class JsonConverterTest {
    
    private static final JsonConverter DEFAULT = JsonConverter.defaultInstance();
    
    private static final JsonConverter IGNORE_NULL = JsonConverter.ignoreNullInstance();
    
    private static final JsonConverter IGNORE_EMPTY = JsonConverter.ignoreEmptyInstance();
    
    @Test
    public void assertToJson() throws JsonProcessingException {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setUsername("");
        assertEquals("{\"id\":1,\"createTime\":null,\"modifyTime\":null,\"username\":\"\",\"password\":null,\"email\":null,\"status\":null}", DEFAULT.toJson(userInfo));
        assertEquals("{\"id\":1,\"username\":\"\"}", IGNORE_NULL.toJson(userInfo));
        assertEquals("{\"id\":1}", IGNORE_EMPTY.toJson(userInfo));
    }
    
    @Test
    public void assertShallowParse() throws JsonProcessingException {
        Collection<String> names = new LinkedList<>();
        names.add("jack");
        names.add("rose");
        names.add("");
        names.add(null);
        assertEquals(4, DEFAULT.shallowParse(DEFAULT.toJson(names), names.getClass()).size());
        assertEquals(4, IGNORE_NULL.shallowParse(IGNORE_NULL.toJson(names), names.getClass()).size());
        assertEquals(4, IGNORE_EMPTY.shallowParse(IGNORE_EMPTY.toJson(names), names.getClass()).size());
    }
    
    @Test
    public void assertDeepParseCollection() throws JsonProcessingException {
        final Collection<UserInfo> userInfos = new LinkedList<>();
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setId(1);
        userInfo1.setUsername("jack");
        userInfo1.setPassword("");
        userInfos.add(userInfo1);
        UserInfo userInfo2 = new UserInfo();
        userInfo2.setId(2);
        userInfo2.setUsername("rose");
        userInfo2.setPassword("");
        userInfos.add(userInfo2);
        JavaType javaType = IGNORE_NULL.buildType(Collection.class, UserInfo.class);
        Collection<UserInfo> defaultResult = DEFAULT.deepParse(DEFAULT.toJson(userInfos), javaType);
        assertEquals("", defaultResult.iterator().next().getPassword());
        Collection<UserInfo> ignoreNullResult = IGNORE_NULL.deepParse(IGNORE_NULL.toJson(userInfos), javaType);
        assertEquals("", ignoreNullResult.iterator().next().getPassword());
        Collection<UserInfo> ignoreEmptyResult = IGNORE_EMPTY.deepParse(IGNORE_EMPTY.toJson(userInfos), javaType);
        assertNull(ignoreEmptyResult.iterator().next().getPassword());
    }
    
    @Test
    public void assertDeepParseMap() throws JsonProcessingException {
        final Map<String, UserInfo> userInfos = new LinkedHashMap<>();
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setId(1);
        userInfo1.setUsername("jack");
        userInfo1.setPassword("");
        userInfos.put("jack", userInfo1);
        UserInfo userInfo2 = new UserInfo();
        userInfo2.setId(2);
        userInfo2.setUsername("rose");
        userInfo2.setPassword("");
        userInfos.put("rose", userInfo2);
        JavaType javaType = IGNORE_NULL.buildType(Map.class, String.class, UserInfo.class);
        Map<String, UserInfo> defaultResult = DEFAULT.deepParse(DEFAULT.toJson(userInfos), javaType);
        assertEquals("", defaultResult.values().iterator().next().getPassword());
        Map<String, UserInfo> ignoreNullResult = IGNORE_NULL.deepParse(IGNORE_NULL.toJson(userInfos), javaType);
        assertEquals("", ignoreNullResult.values().iterator().next().getPassword());
        Map<String, UserInfo> ignoreEmptyResult = IGNORE_EMPTY.deepParse(IGNORE_EMPTY.toJson(userInfos), javaType);
        assertNull(ignoreEmptyResult.values().iterator().next().getPassword());
    }
}
