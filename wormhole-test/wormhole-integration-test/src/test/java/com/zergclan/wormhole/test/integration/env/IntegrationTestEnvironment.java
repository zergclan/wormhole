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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Properties;

/**
 * Integration test environment.
 */
@Getter
public final class IntegrationTestEnvironment {
    
    private static final IntegrationTestEnvironment INSTANCE = new IntegrationTestEnvironment();
    
    private final Collection<String> scenarios = new LinkedList<>();
    
    private final Collection<DataSourceEnvironment> dataSources = new LinkedList<>();
    
    private IntegrationTestEnvironment() {
        Optional<Properties> properties = loadProperties();
        if (properties.isPresent()) {
            Properties props = properties.get();
            scenarios.addAll(splitValues(props.getProperty(EnvironmentConstant.IT_ENV_SCENARIOS)));
            String source = props.getProperty("it.env.datasource.source");
            String target = props.getProperty("it.env.datasource.target");
            dataSources.add(new DataSourceEnvironment(source));
            if (!source.equals(target)) {
                dataSources.add(new DataSourceEnvironment(target));
            }
        }
    }
    
    private Collection<String> splitValues(final String values) {
        String[] split = values.split(MarkConstant.COMMA);
        Collection<String> result = new ArrayList<>(split.length);
        result.addAll(Arrays.asList(split));
        return result;
    }
    
    private Optional<Properties> loadProperties() {
        String type = System.getProperties().getProperty(EnvironmentConstant.IT_RUN_TYPE, "SKIP");
        if ("NATIVE".equalsIgnoreCase(type)) {
            return Optional.of(loadNativeProperties());
        } else if ("DOCKER".equalsIgnoreCase(type)) {
            return Optional.of(loadRuntimeProperties());
        }
        return Optional.empty();
    }
    
    private Properties loadNativeProperties() {
        Properties result = new Properties();
        try (InputStream inputStream = IntegrationTestEnvironment.class.getClassLoader().getResourceAsStream("env/it-env.properties")) {
            result.load(inputStream);
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }
    
    private Properties loadRuntimeProperties() {
        Properties result = new Properties();
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
