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

package com.zergclan.wormhole.test.integration.engine.action;

import com.zergclan.wormhole.bootstrap.engine.WormholeExecutionEngine;
import com.zergclan.wormhole.common.configuration.WormholeConfigurationLoader;
import com.zergclan.wormhole.test.integration.framework.param.WormholeParameterized;
import com.zergclan.wormhole.test.integration.framework.util.PathGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.sql.SQLException;

public final class WormholeAssertAction {
    
    private final WormholeExecutionEngine executionEngine;
    
    private final WormholeActionAsserter actionAsserter = new WormholeActionAsserter();
    
    public WormholeAssertAction(final WormholeParameterized parameterized) {
        executionEngine = initWormholeExecutionEngine(parameterized);
        new Thread(new FixtureWormholeEngineExecutor(executionEngine)).start();
    }
    
    @SneakyThrows({IOException.class, SQLException.class})
    private WormholeExecutionEngine initWormholeExecutionEngine(final WormholeParameterized parameterized) {
        String classPath = PathGenerator.generateWormholeConfigPath(parameterized.getScenario());
        return WormholeExecutionEngine.getInstance(WormholeConfigurationLoader.load(classPath));
    }
    
    /**
     * Do assert action.
     */
    public void doAssertAction() {
        // TODO
    }
    
    @RequiredArgsConstructor
    private static final class FixtureWormholeEngineExecutor implements Runnable {
        
        private final WormholeExecutionEngine wormholeExecutionEngine;
        
        @Override
        public void run() {
            wormholeExecutionEngine.execute();
        }
    }
}
