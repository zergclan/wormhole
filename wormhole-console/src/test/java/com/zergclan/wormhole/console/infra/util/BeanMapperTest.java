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

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class BeanMapperTest {
    
    @Test
    public void assertCopyBean() {
        SourceBean sourceBean = new SourceBean(1, "jack");
        TargetBean targetBean = new TargetBean();
        BeanMapper.shallowCopy(sourceBean, targetBean);
        assertEquals("jack", targetBean.getName());
        Map<String, String> mappings = new LinkedHashMap<>();
        mappings.put("name", "username");
        TargetBeanSpecial targetBeanSpecial = new TargetBeanSpecial();
        BeanMapper.shallowCopy(sourceBean, targetBeanSpecial, mappings);
        assertEquals("jack", targetBeanSpecial.getUsername());
    }
    
    @Test
    public void assertBeanToMap() {
        Map<String, Object> sourceMap = new LinkedHashMap<>();
        sourceMap.put("id", 1);
        sourceMap.put("name", "jack");
        TargetBean targetBean = new TargetBean();
        BeanMapper.mapToBean(sourceMap, targetBean);
        assertEquals("jack", targetBean.getName());
        SourceBean sourceBean = new SourceBean(1, "jack");
        Map<String, Object> targetMap = new LinkedHashMap<>();
        BeanMapper.copyToMap(sourceBean, targetMap);
        assertEquals(1, targetMap.get("id"));
        assertEquals("jack", targetMap.get("name"));
    }
    
    @Data
    @RequiredArgsConstructor
    static class SourceBean {
        
        private final Integer id;
        
        private final String name;
    }
    
    @Data
    static class TargetBean {
        
        private Integer id;
        
        private String name;
    }
    
    @Data
    static class TargetBeanSpecial {
        
        private Integer id;
        
        private String username;
    }
}
