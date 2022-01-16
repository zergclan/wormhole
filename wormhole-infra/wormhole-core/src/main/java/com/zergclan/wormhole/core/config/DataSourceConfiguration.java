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

package com.zergclan.wormhole.core.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Data source configuration.
 */
@RequiredArgsConstructor
@Getter
public final class DataSourceConfiguration implements Serializable {
    
    private static final long serialVersionUID = 1663604199838185645L;

    private final String host;

    private final int port;

    private final String databaseTypeName;

    private final String username;

    private final String password;

    private final String catalog;

    private final Properties parameters;

    private final Map<String, SchemaConfiguration> schemas = new LinkedHashMap<>();
}
