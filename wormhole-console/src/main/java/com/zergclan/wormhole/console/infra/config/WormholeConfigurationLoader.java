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

package com.zergclan.wormhole.console.infra.config;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.config.core.WormholeConfiguration;
import com.zergclan.wormhole.config.core.swapper.YamlWormholeConfigurationSwapper;
import com.zergclan.wormhole.config.core.yaml.YamlTaskConfiguration;
import com.zergclan.wormhole.config.core.yaml.YamlWormholeConfiguration;
import com.zergclan.wormhole.console.infra.util.YamlUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
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
    
    private static final String CONFIGURATION_PATH_PREFIX;
    
    private static final String WORMHOLE_SERVER_CONFIGURATION_FILE = "wormhole-server.yaml";
    
    private static final String WORMHOLE_TASK_CONFIGURATION_FILE_PREFIX = "wormhole-task-";
    
    private static final String WORMHOLE_TASK_CONFIGURATION_FILE_SUFFIX = "yaml";
    
    private static final YamlWormholeConfigurationSwapper SWAPPER;
    
    static {
        SWAPPER = new YamlWormholeConfigurationSwapper();
        CONFIGURATION_PATH_PREFIX = System.getProperty("user.dir") + "/src/main/resources/conf";
    }
    
    /**
     * Load {@link WormholeConfiguration}.
     *
     * @return {@link WormholeConfiguration}
     * @throws IOException IO exception
     */
    public static WormholeConfiguration load() throws IOException {
        return SWAPPER.swapToTarget(loadYamlWormholeConfiguration());
    }
    
    private static YamlWormholeConfiguration loadYamlWormholeConfiguration() throws IOException {
        YamlWormholeConfiguration result = YamlUtil.unmarshal(loadFile(WORMHOLE_SERVER_CONFIGURATION_FILE), YamlWormholeConfiguration.class);
        Map<String, YamlTaskConfiguration> tasks = new LinkedHashMap<>();
        Set<String> taskNames = result.getPlans().entrySet().stream().flatMap(entry -> entry.getValue().getTasks().stream()).collect(Collectors.toCollection(LinkedHashSet::new));
        for (String each : taskNames) {
            tasks.put(each, loadYamlTaskConfiguration(each));
        }
        result.setTasks(tasks);
        return result;
    }
    
    private static YamlTaskConfiguration loadYamlTaskConfiguration(final String taskName) throws IOException {
        File file = loadFile(WORMHOLE_TASK_CONFIGURATION_FILE_PREFIX + taskName + MarkConstant.POINT + WORMHOLE_TASK_CONFIGURATION_FILE_SUFFIX);
        return YamlUtil.unmarshal(file, YamlTaskConfiguration.class);
    }
    
    private static File loadFile(final String fileName) {
        return new File(CONFIGURATION_PATH_PREFIX + MarkConstant.FORWARD_SLASH + fileName);
    }
}
