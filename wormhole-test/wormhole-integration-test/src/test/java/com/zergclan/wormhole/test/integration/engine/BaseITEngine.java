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

package com.zergclan.wormhole.test.integration.engine;

import com.zergclan.wormhole.test.integration.engine.action.ActionExecuteEngine;
import com.zergclan.wormhole.test.integration.env.DataSourceEnvironment;
import com.zergclan.wormhole.test.integration.framework.container.DockerContainerDefinition;
import com.zergclan.wormhole.test.integration.framework.container.DatabaseITContainerManager;
import com.zergclan.wormhole.test.integration.framework.param.WormholeParameterized;
import com.zergclan.wormhole.test.integration.framework.util.TimeSleeper;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base IT engine.
 */
@Getter(AccessLevel.PROTECTED)
public abstract class BaseITEngine {
    
    private final WormholeParameterized parameterized;
    
    private final DatabaseITContainerManager containerManager;
    
    private final ActionExecuteEngine actionExecuteEngine;
    
    public BaseITEngine(final WormholeParameterized parameterized) {
        this.parameterized = parameterized;
        actionExecuteEngine = new ActionExecuteEngine(parameterized);
        containerManager = new DatabaseITContainerManager(new TimeSleeper(50L));
        initEnv();
    }
    
    private void initEnv() {
        for (DataSourceEnvironment each : parameterized.getDataSources()) {
            containerManager.register(new DockerContainerDefinition(parameterized.getScenario(), each.getDatabaseType(), each.getPort()));
        }
        containerManager.start();
    }
}
