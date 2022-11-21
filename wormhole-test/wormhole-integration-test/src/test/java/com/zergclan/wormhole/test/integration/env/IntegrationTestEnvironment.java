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

package com.zergclan.wormhole.test.integration.env;

import com.zergclan.wormhole.tool.constant.MarkConstant;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

/**
 * Integration test environment.
 */
@Getter
public final class IntegrationTestEnvironment {
    
    private static final IntegrationTestEnvironment INSTANCE = new IntegrationTestEnvironment();
    
    private final Collection<String> scenarios = new LinkedList<>();
    
    private final Map<String, DataSourceEnvironment> dataSources = new LinkedHashMap<>();
    
    private IntegrationTestEnvironment() {
        Properties props = loadProperties();
        String runType = props.getProperty(EnvironmentConstant.IT_RUN_TYPE);
        if ("DOCKER".equals(runType)) {
            scenarios.addAll(splitValues(props.getProperty(EnvironmentConstant.IT_ENV_SCENARIOS)));
            dataSources.put(EnvironmentConstant.IT_ENV_SOURCE_ACTUAL_NAME, DataSourceEnvironment.build(props.getProperty(EnvironmentConstant.IT_ENV_SOURCE_DATASOURCE), AssertPart.ACTUAL));
            dataSources.put(EnvironmentConstant.IT_ENV_TARGET_ACTUAL_NAME, DataSourceEnvironment.build(props.getProperty(EnvironmentConstant.IT_ENV_TARGET_DATASOURCE), AssertPart.EXPECTED));
        }
    }
    
    private Collection<String> splitValues(final String values) {
        String[] split = values.split(MarkConstant.COMMA);
        Collection<String> result = new ArrayList<>(split.length);
        result.addAll(Arrays.asList(split));
        return result;
    }
    
    @SneakyThrows(IOException.class)
    private Properties loadProperties() {
        Properties result = new Properties();
        try (InputStream inputStream = IntegrationTestEnvironment.class.getClassLoader().getResourceAsStream("env/it-env.properties")) {
            result.load(inputStream);
        }
        for (String each : System.getProperties().stringPropertyNames()) {
            result.setProperty(each, System.getProperty(each));
        }
        return result;
    }
    
    /**
     * Get instance.
     *
     * @return singleton instance
     */
    public static IntegrationTestEnvironment getInstance() {
        return INSTANCE;
    }
}
