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

package com.zergclan.wormhole.console.application.context;

import com.zergclan.wormhole.bootstrap.engine.WormholeExecutionEngine;
import com.zergclan.wormhole.bus.api.EventListener;
import com.zergclan.wormhole.common.exception.WormholeException;
import com.zergclan.wormhole.console.infra.config.WormholeConfigurationLoader;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Container of {@link ApplicationContext}.
 */
@Component
public final class SpringContextContainer implements ApplicationContextAware {
    
    private static ApplicationContext container;
    
    private final WormholeEngineExecutor wormholeEngineExecutor = new WormholeEngineExecutor();
    
    @Override
    public void setApplicationContext(final @Nullable ApplicationContext applicationContext) {
        container = applicationContext;
        startUp();
    }
    
    private void startUp() {
        wormholeEngineExecutor.register(container.getBeansOfType(EventListener.class));
        new Thread(wormholeEngineExecutor).start();
    }
    
    /**
     * Get {@link WormholeExecutionEngine}.
     *
     * @return {@link WormholeExecutionEngine}
     */
    public WormholeExecutionEngine getExecutionEngine() {
        return wormholeEngineExecutor.getWormholeExecutionEngine();
    }
    
    /**
     * Get bean from {@link ApplicationContext}.
     *
     * @param name bean name
     * @param clazz clazz
     * @param <T> class type of bean
     * @return bean
     */
    public static <T> T getBean(final String name, final Class<T> clazz) {
        checkApplicationContext();
        return container.getBean(name, clazz);
    }
    
    private static void checkApplicationContext() {
        if (container == null) {
            throw new IllegalStateException("error : spring application context not init");
        }
    }
    
    @Getter
    private static final class WormholeEngineExecutor implements Runnable {
        
        private final WormholeExecutionEngine wormholeExecutionEngine;
        
        WormholeEngineExecutor() {
            try {
                wormholeExecutionEngine = WormholeExecutionEngine.getInstance(WormholeConfigurationLoader.load());
            } catch (SQLException | IOException e) {
                throw new WormholeException(e);
            }
        }
        
        private void register(final Map<String, EventListener> listeners) {
            wormholeExecutionEngine.register(listeners);
        }
        
        @Override
        public void run() {
            wormholeExecutionEngine.execute();
        }
    }
}
