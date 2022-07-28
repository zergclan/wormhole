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

package com.zergclan.wormhole.test.integration.engine.precise;

import com.zergclan.wormhole.bootstrap.engine.WormholeExecutionEngine;
import com.zergclan.wormhole.config.core.yaml.loader.WormholeConfigurationLoader;
import com.zergclan.wormhole.test.integration.engine.BaseITEngine;
import com.zergclan.wormhole.test.integration.engine.WormholeEngineExecutor;
import com.zergclan.wormhole.test.integration.framework.param.ParameterizedArrayFactory;
import com.zergclan.wormhole.test.integration.framework.param.WormholeParameterized;
import com.zergclan.wormhole.test.integration.framework.util.PathGenerator;
import lombok.SneakyThrows;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Precise IT engine.
 */
@RunWith(Parameterized.class)
public final class PreciseITEngineTest extends BaseITEngine {
    
    private volatile WormholeExecutionEngine executionEngine;
    
    public PreciseITEngineTest(final WormholeParameterized parameterized) {
        super(parameterized);
    }
    
    @Parameters(name = "{0}")
    public static Collection<WormholeParameterized> getParameters() {
        return ParameterizedArrayFactory.getInstance().getParameterizedArray();
    }
    
    @Test
    public void assertPreciseCases() {
        preProcess();
        testData();
        // executeProcess();
        postProcess();
    }
    
    @SneakyThrows(SQLException.class)
    private void testData() {
        System.out.println("testDataset");
        try (Connection connection = getSource().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ds_source.t_user");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                System.out.println(id + "---" + username + "---" + password);
            }
        }
    }
    
    private void executeProcess() {
        startWormholeExecutionEngine();
        triggerPlanExecute();
        assertResult();
    }
    
    @SneakyThrows({IOException.class, SQLException.class})
    private void startWormholeExecutionEngine() {
        String classPath = PathGenerator.generateWormholeConfigClasspath(getScenario());
        executionEngine = WormholeExecutionEngine.getInstance(WormholeConfigurationLoader.load(classPath));
        new Thread(new WormholeEngineExecutor(executionEngine)).start();
    }
    
    private void triggerPlanExecute() {
        executionEngine.trigger(getScenario() + "_plan");
    }
    
    private void assertResult() {
        // TODO wait call back of plan completed event
    }
}
