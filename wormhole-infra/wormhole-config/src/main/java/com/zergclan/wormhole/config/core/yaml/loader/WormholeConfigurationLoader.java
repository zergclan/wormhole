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

package com.zergclan.wormhole.config.core.yaml.loader;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.config.core.WormholeConfiguration;
import com.zergclan.wormhole.config.core.yaml.YamlTaskConfiguration;
import com.zergclan.wormhole.config.core.yaml.YamlWormholeConfiguration;
import com.zergclan.wormhole.config.core.yaml.swapper.YamlWormholeConfigurationSwapper;
import com.zergclan.wormhole.config.core.yaml.util.YamlUtil;
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
 * {@link WormholeConfiguration} loader.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WormholeConfigurationLoader {
    
    private static final String WORMHOLE_SERVER_CONFIGURATION_FILE = "wormhole-server.yaml";
    
    private static final String WORMHOLE_TASK_CONFIGURATION_FILE_PREFIX = "wormhole-task-";
    
    private static final String WORMHOLE_TASK_CONFIGURATION_FILE_SUFFIX = "yaml";
    
    private static final YamlWormholeConfigurationSwapper SWAPPER = new YamlWormholeConfigurationSwapper();
    
    /**
     * Load {@link WormholeConfiguration}.
     *
     * @param classpath classpath
     * @return {@link WormholeConfiguration}
     * @throws IOException IO exception
     */
    public static WormholeConfiguration load(final String classpath) throws IOException {
        return SWAPPER.swapToTarget(loadYamlWormholeConfiguration(classpath));
    }
    
    private static YamlWormholeConfiguration loadYamlWormholeConfiguration(final String classpath) throws IOException {
        YamlWormholeConfiguration result = YamlUtil.unmarshal(getResourceYamlFile(classpath, WORMHOLE_SERVER_CONFIGURATION_FILE), YamlWormholeConfiguration.class);
        Map<String, YamlTaskConfiguration> tasks = new LinkedHashMap<>();
        Set<String> taskNames = result.getPlans().entrySet().stream().flatMap(entry -> entry.getValue().getTasks().stream()).collect(Collectors.toCollection(LinkedHashSet::new));
        for (String each : taskNames) {
            tasks.put(each, loadYamlTaskConfiguration(classpath, each));
        }
        result.setTasks(tasks);
        return result;
    }
    
    private static YamlTaskConfiguration loadYamlTaskConfiguration(final String classpath, final String taskName) throws IOException {
        return YamlUtil.unmarshal(getResourceYamlFile(classpath, formatYAMLFileName(WORMHOLE_TASK_CONFIGURATION_FILE_PREFIX + taskName)), YamlTaskConfiguration.class);
    }
    
    @SneakyThrows(URISyntaxException.class)
    private static File getResourceYamlFile(final String classpath, final String fileName) {
        URL url = WormholeConfigurationLoader.class.getResource(classpath + MarkConstant.FORWARD_SLASH + fileName);
        return null == url ? new File(classpath) : new File(url.toURI().getPath());
    }
    
    private static String formatYAMLFileName(final String fileName) {
        return fileName + MarkConstant.POINT + WORMHOLE_TASK_CONFIGURATION_FILE_SUFFIX;
    }
}
