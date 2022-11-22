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

package com.zergclan.wormhole.common.configuration;

import com.zergclan.wormhole.common.configuration.initializer.WormholeConfigurationInitializer;
import com.zergclan.wormhole.common.configuration.yaml.YamlTaskConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlWormholeConfiguration;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.tool.util.YamlUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Loader of {@link WormholeConfiguration}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WormholeConfigurationLoader {
    
    private static final String WORMHOLE_SERVER_CONFIGURATION_FILE = "wormhole-server.yaml";
    
    private static final String WORMHOLE_TASK_CONFIGURATION_FILE_PREFIX = "wormhole-task-";
    
    private static final String WORMHOLE_TASK_CONFIGURATION_FILE_SUFFIX = "yaml";
    
    private static final WormholeConfigurationInitializer INITIALIZER = new WormholeConfigurationInitializer();
    
    /**
     * Load {@link WormholeConfiguration}.
     *
     * @param classpath classpath
     * @return {@link WormholeConfiguration}
     * @throws IOException IO exception
     */
    public static WormholeConfiguration load(final String classpath) throws IOException {
        YamlWormholeConfiguration yamlConfiguration = YamlUtil.unmarshal(getResourceYamlFile(classpath, WORMHOLE_SERVER_CONFIGURATION_FILE), YamlWormholeConfiguration.class);
        Map<String, YamlTaskConfiguration> tasks = new LinkedHashMap<>();
        Set<String> taskNames = yamlConfiguration.getPlans().entrySet().stream().flatMap(entry -> entry.getValue().getTasks().stream()).collect(Collectors.toCollection(LinkedHashSet::new));
        for (String each : taskNames) {
            tasks.put(each, YamlUtil.unmarshal(getResourceYamlFile(classpath, formatYAMLFileName(WORMHOLE_TASK_CONFIGURATION_FILE_PREFIX + each)), YamlTaskConfiguration.class));
        }
        yamlConfiguration.setTasks(tasks);
        return INITIALIZER.init(yamlConfiguration);
    }
    
    /**
     * Load {@link WormholeConfiguration}.
     *
     * @param serverYamlFile server yaml file
     * @param taskYamlFiles task yaml files
     * @return {@link WormholeConfiguration}
     * @throws IOException IO exception
     */
    public static WormholeConfiguration load(final File serverYamlFile, final Map<String, File> taskYamlFiles) throws IOException {
        YamlWormholeConfiguration yamlConfiguration = YamlUtil.unmarshal(serverYamlFile, YamlWormholeConfiguration.class);
        Map<String, YamlTaskConfiguration> tasks = new LinkedHashMap<>();
        for (Map.Entry<String, File> entry : taskYamlFiles.entrySet()) {
            tasks.put(entry.getKey(), YamlUtil.unmarshal(entry.getValue(), YamlTaskConfiguration.class));
        }
        yamlConfiguration.setTasks(tasks);
        return INITIALIZER.init(yamlConfiguration);
    }
    
    @SneakyThrows(URISyntaxException.class)
    private static File getResourceYamlFile(final String classpath, final String fileName) {
        String realFile = classpath + MarkConstant.FORWARD_SLASH + fileName;
        URL url = WormholeConfigurationLoader.class.getResource(realFile);
        return null == url ? new File(realFile) : new File(url.toURI().getPath());
    }
    
    private static String formatYAMLFileName(final String fileName) {
        return fileName + MarkConstant.POINT + WORMHOLE_TASK_CONFIGURATION_FILE_SUFFIX;
    }
}
