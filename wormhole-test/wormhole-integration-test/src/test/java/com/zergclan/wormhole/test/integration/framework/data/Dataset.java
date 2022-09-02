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

package com.zergclan.wormhole.test.integration.framework.data;

import com.zergclan.wormhole.common.metadata.database.DatabaseType;
import com.zergclan.wormhole.common.metadata.datasource.DataSourceTypeFactory;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.test.integration.framework.data.config.DataSourceConfiguration;
import com.zergclan.wormhole.test.integration.framework.data.config.DatasetConfiguration;
import com.zergclan.wormhole.test.integration.framework.data.node.DataSourceNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public final class Dataset {
    
    private final String scenario;
    
    private final DatabaseType databaseType;
    
    private final int port;
    
    private final Map<String, DataSourceNode> dataSources;
    
    public Dataset(final String scenario, final DatasetConfiguration datasetConfiguration) {
        this.scenario = scenario;
        databaseType = DataSourceTypeFactory.getInstance(datasetConfiguration.getDatabaseType());
        port = datasetConfiguration.getPort();
        dataSources = initDataSources(datasetConfiguration.getDataSources());
    }
    
    private Map<String, DataSourceNode> initDataSources(final Map<String, DataSourceConfiguration> dataSources) {
        Map<String, DataSourceNode> result = new LinkedHashMap<>();
        for (Map.Entry<String, DataSourceConfiguration> entry : dataSources.entrySet()) {
            result.put(entry.getKey(), new DataSourceNode(entry.getValue()));
        }
        return result;
    }
    
    /**
     * Get identifier.
     *
     * @return identifier
     */
    public String getIdentifier() {
        return scenario + MarkConstant.COLON + databaseType + MarkConstant.COLON + port;
    }
}
