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

package com.zergclan.wormhole.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Util tools for YAML.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class YamlUtil {
    
    /**
     * Unmarshal YAML.
     *
     * @param yamlFile YAML file
     * @param classType class type
     * @param <T> class type of configuration
     * @return implemented of yaml configuration
     * @throws IOException IO exception
     */
    public static <T> T unmarshal(final File yamlFile, final Class<T> classType) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(yamlFile);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream)) {
            return new Yaml().loadAs(inputStreamReader, classType);
        }
    }
}
