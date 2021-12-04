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

package com.zergclan.wormhole.console.infra.repository;

import com.zergclan.wormhole.console.WormholeETLApplication;
import com.zergclan.wormhole.console.api.vo.PageQuery;
import com.zergclan.wormhole.console.application.domain.entity.DatasourceInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {WormholeETLApplication.class})
public final class BaseRepositorySelectTest {
    
    @Resource
    private BaseRepository<DatasourceInfo> datasourceInfoRepository;
    
    @Test
    public void assertCount() {
        assertEquals(7, datasourceInfoRepository.count());
    }
    
    @Test
    public void assertListAll() {
        assertEquals(7, datasourceInfoRepository.listAll().size());
    }
    
    @Test
    public void assertGet() {
        assertEquals("datasource info for ut test", datasourceInfoRepository.get(3).getDescription());
    }
    
    @Test
    public void assertGetOne() {
        DatasourceInfo uniqueQuery = new DatasourceInfo();
        uniqueQuery.setHost("127.0.0.1");
        uniqueQuery.setPort(3307);
        uniqueQuery.setCatalog("ds_ut");
        uniqueQuery.setUsername("root");
        uniqueQuery.setPassword("123456");
        assertEquals("datasource info for ut test", datasourceInfoRepository.getOne(uniqueQuery).getDescription());
    }
    
    @Test
    public void assertListInIds() {
        Collection<Integer> ids = new LinkedList<>();
        ids.add(3);
        ids.add(4);
        ids.add(5);
        ids.add(6);
        assertEquals(4, datasourceInfoRepository.list(ids).size());
    }
    
    @Test
    public void assertListByPage() {
        PageQuery<DatasourceInfo> pageQuery = new PageQuery<>();
        pageQuery.setPage(2);
        pageQuery.setSize(2);
        DatasourceInfo queryBean = new DatasourceInfo();
        queryBean.setTitle("local_test_data_source");
        pageQuery.setQuery(queryBean);
        PageData<DatasourceInfo> pageData = datasourceInfoRepository.listByPage(pageQuery);
        assertEquals(5, pageData.getTotal());
        assertEquals(3, pageData.getTotalPage());
        assertEquals(2, pageData.getItems().size());
    }
}
