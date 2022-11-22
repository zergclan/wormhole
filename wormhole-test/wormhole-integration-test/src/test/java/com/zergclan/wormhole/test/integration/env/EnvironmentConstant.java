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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Environment constant.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnvironmentConstant {
    
    public static final String IT_RUN_TYPE = "it.run.type";
    
    public static final String IT_ENV_SCENARIOS = "it.env.scenarios";
    
    public static final String IT_ENV_SOURCE_DATASOURCE = "it.env.source.datasource";
    
    public static final String IT_ENV_TARGET_DATASOURCE = "it.env.target.datasource";
    
    public static final String IT_ENV_SOURCE_ACTUAL_NAME = "actual_source";
    
    public static final String IT_ENV_TARGET_ACTUAL_NAME = "actual_target";
    
    public static final String IT_ENV_TARGET_EXPECTED_NAME = "expected_target";
}
