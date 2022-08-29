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

package com.zergclan.wormhole.test.integration.framework.data.config;

import com.zergclan.wormhole.tool.util.YamlUtil;
import com.zergclan.wormhole.test.integration.framework.util.PathGenerator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Dataset configuration loader.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DatasetConfigurationLoader {
    
    /**
     * Load {@link DatasetConfiguration}.
     *
     * @param scenario scenario
     * @return {@link DatasetConfiguration}
     * @throws IOException IO exception
     */
    public static DatasetConfiguration load(final String scenario) throws IOException {
        return YamlUtil.unmarshal(loadFile(scenario), DatasetConfiguration.class);
    }
    
    private static File loadFile(final String scenario) throws IOException {
        String path = PathGenerator.generateDatasetPath(scenario) + "/dataset.yaml";
        URL url = DatasetConfigurationLoader.class.getClassLoader().getResource(path);
        if (null == url) {
            throw new IOException();
        }
        return new File(url.getFile());
    }
}
