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

package com.zergclan.wormhole.console.infra.config.yaml;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * YAML target configuration.
 */
@Getter
@Setter
public final class YamlTargetConfiguration implements YamlConfiguration {
    
    private String dataSource;
    
    private String table;
    
    private Set<String> uniqueNodes = new LinkedHashSet<>();
    
    private Set<String> compareNodes = new LinkedHashSet<>();
    
    private Set<String> ignoreNodes = new LinkedHashSet<>();
    
    private Map<String, YamlDataNodeConfiguration> dataNodes = new LinkedHashMap<>();
}