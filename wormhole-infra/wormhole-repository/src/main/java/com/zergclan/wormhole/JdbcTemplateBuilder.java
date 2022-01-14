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

package com.zergclan.wormhole;

import com.zergclan.wormhole.core.metadata.DataSourceMetadata;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Builder for jdbcTemplate.
 */
public final class JdbcTemplateBuilder {

    /**
     * Build jdbcTemplate by data source metadata.
     *
     * @param dataSourceMetadata Data source metadata.
     * @return JdbcTemplate.
     */
    static JdbcTemplate build(final DataSourceMetadata dataSourceMetadata) {
        String identifier = dataSourceMetadata.getIdentifier();
        JdbcTemplate jdbcTemplate = DataSourceManager.get(identifier);
        if (jdbcTemplate != null) {
            return jdbcTemplate;
        }

        synchronized (identifier) {
            jdbcTemplate = DataSourceManager.get(identifier);
            if (jdbcTemplate != null) {
                return jdbcTemplate;
            }
            DataSource dataSource = DataSourceFactory.createDataSource(dataSourceMetadata);
            jdbcTemplate = new JdbcTemplate(dataSource);
            return jdbcTemplate;
        }

    }

}
