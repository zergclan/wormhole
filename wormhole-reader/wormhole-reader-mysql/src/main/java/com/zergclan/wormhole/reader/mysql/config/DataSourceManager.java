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

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.zergclan.wormhole.common.StringUtil;
import com.zergclan.wormhole.reader.mysql.entity.DataSourceInformation;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dynamically generate mysql data source.
 */
public final class DataSourceManager {

    private static Map<String, JdbcTemplate> dataSourceMap = new ConcurrentHashMap<>();

    protected static JdbcTemplate getJdbcTemplate(final DataSourceInformation dataSourceInfo) throws Exception {
        // XXX The md5 method is deprecated.
        String key = Hashing.md5().newHasher().putString(new Gson().toJson(dataSourceInfo), Charsets.UTF_8).hash().toString();
        JdbcTemplate jdbcTemplate = dataSourceMap.get(key);
        if (null == jdbcTemplate) {
            synchronized (key.intern()) {
                jdbcTemplate = dataSourceMap.get(key);
                if (null == jdbcTemplate) {
                    Map<String, String> conf = new LinkedHashMap<>();
                    conf.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, dataSourceInfo.getDriverClassname());
                    conf.put(DruidDataSourceFactory.PROP_URL, dataSourceInfo.getJdbcUrl());
                    conf.put(DruidDataSourceFactory.PROP_USERNAME, dataSourceInfo.getDbUser());
                    setPassword(conf, dataSourceInfo.getDbPassword());
                    conf.put(DruidDataSourceFactory.PROP_INITIALSIZE, "3");
                    DruidDataSource druidDS = (DruidDataSource) DruidDataSourceFactory.createDataSource(conf);
                    druidDS.setBreakAfterAcquireFailure(true);
                    druidDS.setConnectionErrorRetryAttempts(5);
                    dataSourceMap.put(key, new JdbcTemplate(druidDS));
                    jdbcTemplate = dataSourceMap.get(key);
                }
            }
        }
        return jdbcTemplate;
    }

    private static void setPassword(final Map<String, String> conf, final String password) {
        if (!StringUtil.isBlank(password)) {
            conf.put(DruidDataSourceFactory.PROP_PASSWORD, password);
        }
    }
}
