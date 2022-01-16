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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JdbcTemplate for Managing data sources.
 */
public final class JdbcTemplateManager {

    private static final Map<String, JdbcTemplate> JDBC_TEMPLATE_CONTAINER = new ConcurrentHashMap<>();

    /**
     * Get jdbcTemplate by identifier.
     *
     * @param identifier Id.
     * @return JdbcTemplate.
     */
    public static JdbcTemplate get(final String identifier) {
        return JDBC_TEMPLATE_CONTAINER.get(identifier);
    }

    /**
     * Register data source.
     *
     * @param dataSourceMetadata Data source obj.
     * @return JdbcTemplate.
     */
    public static JdbcTemplate register(final DataSourceMetadata dataSourceMetadata) {
        String identifier = dataSourceMetadata.getIdentifier();
        JdbcTemplate jdbcTemplate = get(identifier);
        if (null != jdbcTemplate) {
            return jdbcTemplate;
        }

        synchronized (identifier) {
            jdbcTemplate = get(identifier);
            if (null != jdbcTemplate) {
                return jdbcTemplate;
            }
            jdbcTemplate = JdbcTemplateBuilder.build(dataSourceMetadata);
            JDBC_TEMPLATE_CONTAINER.put(identifier, jdbcTemplate);
            return jdbcTemplate;
        }
    }

    /**
     * Unregister.
     *
     * @param identifier Id.
     * @return Boolean result.
     */
    public static boolean unRegister(final String identifier) {
        JDBC_TEMPLATE_CONTAINER.remove(identifier);
        return true;
    }

}
