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

package com.zergclan.wormhole.test.integration.framework.container;

import com.zergclan.wormhole.test.integration.framework.container.storage.DatabaseITContainer;
import com.zergclan.wormhole.test.integration.framework.util.TimeSleeper;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public final class DatabaseITContainerManager {
    
    private final TimeSleeper sleeper;
    
    private final Map<String, DatabaseITContainer> databasesContainers = new LinkedHashMap<>();
    
    /**
     * Register.
     *
     * @param containerDefinition {@link DatabaseITContainer}
     */
    public void register(final DockerContainerDefinition containerDefinition) {
        databasesContainers.put(containerDefinition.getIdentifier(), DockerITContainerBuilder.newStorageContainer(containerDefinition));
    }
    
    /**
     * Get container.
     *
     * @param identifier identifier
     * @return {@link DatabaseITContainer}
     */
    public DatabaseITContainer getContainer(final String identifier) {
        return databasesContainers.get(identifier);
    }
    
    /**
     * Start.
     */
    public void start() {
        for (DatabaseITContainer each : databasesContainers.values()) {
            each.start();
            sleeper.sleep();
        }
    }
    
    /**
     * Close.
     */
    public void close() {
        for (DatabaseITContainer each : databasesContainers.values()) {
            each.close();
            sleeper.sleep();
        }
    }
}
