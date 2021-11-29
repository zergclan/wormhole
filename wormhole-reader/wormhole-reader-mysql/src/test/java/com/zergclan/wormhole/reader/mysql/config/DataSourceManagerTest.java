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

package com.zergclan.wormhole.reader.mysql.config;

import com.zergclan.wormhole.reader.mysql.entity.DataSourceInformation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Test datasource manager.
 */
@Slf4j
public final class DataSourceManagerTest {
    
    /**
     * Test get jdbcTemplate.
     * @throws Exception Get jdbcTemplate Exception.
     */
//    @Test
    public void getJdbcTemplate() throws Exception {
        JdbcTemplate jdbcTemplate = getTestJdbcTemplate();
        Assertions.assertEquals(1, jdbcTemplate.queryForObject("select 1", Integer.class));
    }

    /**
     * Get test JdbcTemplate.
     * @return JdbcTemplate.
     * @throws Exception Get jdbcTemplate exception.
     */
    public static JdbcTemplate getTestJdbcTemplate() throws Exception {
        DataSourceInformation dataSourceInformation = new DataSourceInformation();
        dataSourceInformation.setIp("192.168.112.130");
        dataSourceInformation.setPort("3306");
        dataSourceInformation.setDbUser("root");
        dataSourceInformation.setDbPassword("koma1993");
        dataSourceInformation.setDbName("test01");
        return DataSourceManager.getJdbcTemplate(dataSourceInformation);
    }
}
