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

import com.zergclan.wormhole.tool.constant.MarkConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Yaml file loader.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class YamlFileLoader {
    
    private static final String WORMHOLE_SERVER_CONFIGURATION_FILE = "wormhole-server.yaml";
    
    private static final String WORMHOLE_TASK_CONFIGURATION_FILE_FORMAT = "wormhole-task-%s.yaml";
    
    /**
     * Load server yaml.
     *
     * @param scenario scenario
     * @return server yaml file
     */
    public static File loadServerYaml(final String scenario) {
        return getResourceYamlFile(PathGenerator.generateWormholeConfigPath(scenario), WORMHOLE_SERVER_CONFIGURATION_FILE);
    }
    
    /**
     * Load task yaml.
     *
     * @param scenario scenario
     * @return task yaml file
     */
    public static Map<String, File> loadTaskYaml(final String scenario) {
        Map<String, File> result = new LinkedHashMap<>();
        result.put(scenario, getResourceYamlFile(PathGenerator.generateWormholeConfigPath(scenario), String.format(WORMHOLE_TASK_CONFIGURATION_FILE_FORMAT, scenario)));
        return result;
    }
    
    private static File getResourceYamlFile(final String classpath, final String fileName) {
        URL url = Objects.requireNonNull(YamlFileLoader.class.getClassLoader().getResource(classpath));
        return new File(url.getPath() + MarkConstant.FORWARD_SLASH + fileName);
    }
}
