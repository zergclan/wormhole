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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Container path generator.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PathGenerator {
    
    private static final String DELIMITER = "/";
    
    private static final String ROOT_PATH = "evn/scenario";
    
    private static final String DATA_PATH = "data";
    
    private static final String INIT_SQL_PATH = "init-sql";
    
    /**
     * Generate dataset path.
     *
     * @param scenario scenario
     * @return dataset path
     */
    public static String generateDatasetPath(final String scenario) {
        return String.join("/", getBasicPath(scenario), DATA_PATH, INIT_SQL_PATH);
    }
    
    /**
     * Generate init SQL path.
     *
     * @param scenario scenario
     * @param databaseType database type
     * @return init SQL path
     */
    public static String generateInitSqlPath(final String scenario, final String databaseType) {
        return String.join("/", getBasicPath(scenario), DATA_PATH, INIT_SQL_PATH, databaseType.toLowerCase());
    }
    
    private static String getBasicPath(final String scenario) {
        return ROOT_PATH + DELIMITER + scenario;
    }
}
