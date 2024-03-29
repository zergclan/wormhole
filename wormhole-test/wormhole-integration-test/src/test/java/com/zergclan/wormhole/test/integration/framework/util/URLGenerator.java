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

package com.zergclan.wormhole.test.integration.framework.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class URLGenerator {
    
    private static final String DEFAULT_MYSQL_JDBC_URL_SUFFIX = "?useSSL=false&characterEncoding=utf-8&serverTimezone=UTC&useServerPrepStmts=true&useLocalSessionState=true";
    
    /**
     * Generate JDBC url.
     *
     * @param databaseType database type
     * @param port port
     * @return JDBC url
     */
    public static String generateJDBCUrl(final String databaseType, final int port) {
        if ("MySQL".equalsIgnoreCase(databaseType)) {
            return "jdbc:mysql://localhost:" + port + DEFAULT_MYSQL_JDBC_URL_SUFFIX;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
