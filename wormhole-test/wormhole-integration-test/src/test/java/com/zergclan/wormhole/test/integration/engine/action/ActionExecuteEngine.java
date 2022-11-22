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
import com.zergclan.wormhole.test.integration.fixture.FixtureWormholeEngineExecutor;
import com.zergclan.wormhole.test.integration.framework.assertion.AssertActionDefinitionLoader;
import com.zergclan.wormhole.test.integration.framework.assertion.definition.AssertStepDefinition;
import com.zergclan.wormhole.test.integration.framework.param.WormholeParameterized;
import com.zergclan.wormhole.test.integration.framework.util.YamlFileLoader;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

/**
 * Action execute engine.
 */
public final class ActionExecuteEngine {
    
    private final WormholeExecutionEngine executionEngine;
    
    private final Collection<AssertStepDefinition> assertStep;
    
    private final ActionStepExecutor actionStepExecutor;
    
    private final ActionStepAsserter actionStepAsserter;
    
    public ActionExecuteEngine(final WormholeParameterized parameterized) {
        assertStep = AssertActionDefinitionLoader.load(parameterized.getScenario()).getAssertStep();
        executionEngine = initWormholeExecutionEngine(parameterized);
        actionStepExecutor = new ActionStepExecutor(executionEngine);
        actionStepAsserter = new ActionStepAsserter(executionEngine);
    }
    
    @SneakyThrows({IOException.class, SQLException.class})
    private WormholeExecutionEngine initWormholeExecutionEngine(final WormholeParameterized parameterized) {
        File serverYaml = YamlFileLoader.loadServerYaml(parameterized.getScenario());
        Map<String, File> taskYaml = YamlFileLoader.loadTaskYaml(parameterized.getScenario());
        return WormholeExecutionEngine.getInstance(WormholeConfigurationLoader.load(serverYaml, taskYaml));
    }
    
    /**
     * Do assert action.
     */
    public void doAssertAction() {
        new Thread(new FixtureWormholeEngineExecutor(executionEngine)).start();
        for (AssertStepDefinition each : assertStep) {
            actionStepExecutor.executeStep(each);
            actionStepAsserter.assertStep(each);
        }
    }
}
