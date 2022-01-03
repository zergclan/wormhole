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

package com.zergclan.wormhole.repository.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zergclan.wormhole.common.StringUtil;
import com.zergclan.wormhole.common.WormholeException;
import com.zergclan.wormhole.repository.entity.DataSourceInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The root interface from which all data source manager objects shall be derived in Wormhole.
 */
public abstract class DataSourceManager {

    private static final Map<String, JdbcTemplate> DATASOURCE_MAP = new ConcurrentHashMap<>();

    /**
     * This method should be implemented by the son class, because it involves business.
     * @param dataSourceId Data source id.
     * @return Boolean result.
     */
    public static Boolean registerDataSource(final String dataSourceId) {
        throw new WormholeException("Not implemented");
    }

    /**
     * Register data source.
     * @param dataSourceInformation Data source information.
     * @return Boolean result.
     */
    public static JdbcTemplate registerDataSource(final DataSourceInformation dataSourceInformation) {
        if (StringUtil.isBlank(dataSourceInformation.getId())) {
            throw new WormholeException("Datasource information is missing ID parameter.");
        }
        String key = dataSourceInformation.getId();
        JdbcTemplate jdbcTemplate = DATASOURCE_MAP.get(key);
        if (null == jdbcTemplate) {
            synchronized (key.intern()) {
                jdbcTemplate = DATASOURCE_MAP.get(key);
                if (null == jdbcTemplate) {
                    HikariConfig config = new HikariConfig();
                    config.setUsername(dataSourceInformation.getDbUser());
                    config.setJdbcUrl(dataSourceInformation.getJdbcUrl());
                    config.setPoolName(dataSourceInformation.getId());
                    config.setDriverClassName(dataSourceInformation.getDriverClassname());
                    String password = dataSourceInformation.getDbPassword();
                    setPassword(config, password);
                    HikariDataSource hikariDataSource = new HikariDataSource(config);
                    jdbcTemplate = new JdbcTemplate(hikariDataSource);
                    testConnection(jdbcTemplate, dataSourceInformation.getTestSql());
                    DATASOURCE_MAP.put(key, jdbcTemplate);
                }
            }
        }

        return jdbcTemplate;
    }

    private static void setPassword(final HikariConfig config, final String password) {
        if (!StringUtil.isBlank(password)) {
            config.setPassword(password);
        }
    }

    private static Boolean testConnection(final JdbcTemplate jdbcTemplate, final String testSql) {
        jdbcTemplate.execute(testSql);
        return true;
    }

    /**
     * Unregister data source.
     * @param dataSourceId Data source id.
     * @return Boolean result.
     */
    public static Boolean unRegisterDataSource(final String dataSourceId) {
        JdbcTemplate jdbcTemplate = DATASOURCE_MAP.get(dataSourceId);
        if (null != jdbcTemplate) {
            DATASOURCE_MAP.remove(dataSourceId);
            HikariDataSource dataSource = (HikariDataSource) jdbcTemplate.getDataSource();
            dataSource.close();
        }
        return true;
    }

    /**
     * Get data source connection.
     * @param dataSourceId Data source id.
     * @return JdbcTemplate.
     */
    public static JdbcTemplate getDataSource(final String dataSourceId) {
        JdbcTemplate jdbcTemplate = DATASOURCE_MAP.get(dataSourceId);
        if (null == jdbcTemplate) {
            throw new WormholeException("Data source connection not obtained");
        }
        return jdbcTemplate;
    }

}
