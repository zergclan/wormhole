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
import com.zergclan.wormhole.console.application.domain.entity.DatasourceInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.LinkedList;

@SpringBootTest(classes = {WormholeETLApplication.class})
public final class BaseRepositoryAddTest {
    
    private static final String EXTEND_PARAMETERS = "{\"useSSL\":\"false\",\"useUnicode\":\"true\",\"characterEncoding\":\"UTF-8\",\"serverTimezone\":\"UTC\"}";
    
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/test_ds?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    
    @Resource
    private BaseRepository<DatasourceInfo> datasourceInfoRepository;
    
    @Test
    public void assertAdd() {
        datasourceInfoRepository.add(createDatasourceInfo(0));
    }
    
    private DatasourceInfo createDatasourceInfo(final int index) {
        DatasourceInfo result = new DatasourceInfo();
        result.setTitle("title as name");
        result.setGenre(0);
        result.setHost("127.0.0.1");
        result.setPort(3306);
        result.setCatalogue("test_ds" + index);
        result.setUsername("root");
        result.setPassword("123456");
        result.setExtendParameters(EXTEND_PARAMETERS);
        result.setUrl(URL);
        result.setDescription("test ds description");
        result.setOperator(0);
        result.setEnable(0);
        return result;
    }
    
    @Test
    public void assertAddBatch() {
        Collection<DatasourceInfo> datasourceInfos = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            datasourceInfos.add(createDatasourceInfo(i));
        }
        datasourceInfoRepository.addBatch(datasourceInfos);
    }
}
