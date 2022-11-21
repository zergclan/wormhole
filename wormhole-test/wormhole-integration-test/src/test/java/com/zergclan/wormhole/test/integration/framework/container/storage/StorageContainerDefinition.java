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

package com.zergclan.wormhole.test.integration.framework.container.storage;

import com.zergclan.wormhole.common.metadata.database.DatabaseType;
import com.zergclan.wormhole.common.metadata.datasource.DataSourceTypeFactory;
import com.zergclan.wormhole.test.integration.env.DataSourceEnvironment;
import com.zergclan.wormhole.test.integration.env.AssertPart;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Container definition.
 */
@RequiredArgsConstructor
@Getter
public final class StorageContainerDefinition {
    
    private final String scenario;
    
    private final DatabaseType databaseType;
    
    private final String containerIdentifier;
    
    private final String dockerImageName;
    
    private final AssertPart assertPart;
    
    private final String databaseName;
    
    /**
     * Build.
     *
     * @param scenario scenario
     * @param databaseName database name
     * @param dataSourceEnvironment data source environment
     * @return {@link StorageContainerDefinition}
     */
    public static StorageContainerDefinition build(final String scenario, final String databaseName, final DataSourceEnvironment dataSourceEnvironment) {
        DatabaseType databaseType = DataSourceTypeFactory.getInstance(dataSourceEnvironment.getDatabaseType());
        String containerIdentifier = initContainerIdentifier(scenario, databaseType.getType(), databaseName);
        String dockerImageName = dataSourceEnvironment.getDockerImageName();
        return new StorageContainerDefinition(scenario, databaseType, containerIdentifier, dockerImageName, dataSourceEnvironment.getAssertPart(), databaseName);
    }
    
    private static String initContainerIdentifier(final String scenario, final String databaseType, final String databaseName) {
        return scenario + MarkConstant.COLON + databaseType + MarkConstant.COLON + databaseName;
    }
}
