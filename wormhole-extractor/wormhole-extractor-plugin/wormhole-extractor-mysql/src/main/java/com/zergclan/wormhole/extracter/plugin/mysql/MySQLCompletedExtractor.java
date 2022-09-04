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

package com.zergclan.wormhole.extracter.plugin.mysql;

import com.zergclan.wormhole.common.data.node.DataGroup;
import com.zergclan.wormhole.common.data.node.DataNodeBuilder;
import com.zergclan.wormhole.common.metadata.database.SupportedDialectType;
import com.zergclan.wormhole.common.metadata.datasource.WormholeDataSourceMetaData;
import com.zergclan.wormhole.common.metadata.plan.node.DataNodeMetaData;
import com.zergclan.wormhole.extractor.AbstractCompletedExtractor;
import com.zergclan.wormhole.jdbc.datasource.DataSourceManager;
import com.zergclan.wormhole.jdbc.execute.SQLExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Completed extractor of MySQL.
 */
public final class MySQLCompletedExtractor extends AbstractCompletedExtractor {
    
    @Override
    protected Collection<DataGroup> doExtract(final WormholeDataSourceMetaData dataSource, final Map<String, DataNodeMetaData> dataNodes, final String extractSQl) throws SQLException {
        try (Connection connection = createConnection(dataSource)) {
            return execute(connection, dataNodes, extractSQl);
        }
    }
    
    private Connection createConnection(final WormholeDataSourceMetaData dataSourceMetaData) throws SQLException {
        return DataSourceManager.getDataSource(dataSourceMetaData).getConnection();
    }
    
    private Collection<DataGroup> execute(final Connection connection, final Map<String, DataNodeMetaData> dataNodes, final String extractSQl) throws SQLException {
        Collection<DataGroup> result = new LinkedList<>();
        ResultSet resultSet = SQLExecutor.executeQuery(connection, extractSQl);
        while (resultSet.next()) {
            result.add(createDataGroup(resultSet, dataNodes));
        }
        return result;
    }
    
    private DataGroup createDataGroup(final ResultSet resultSet, final Map<String, DataNodeMetaData> dataNodes) throws SQLException {
        DataGroup result = new DataGroup();
        Iterator<Map.Entry<String, DataNodeMetaData>> iterator = dataNodes.entrySet().iterator();
        Map.Entry<String, DataNodeMetaData> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            result.register(DataNodeBuilder.build(resultSet.getObject(entry.getKey()), entry.getValue()));
        }
        return result;
    }
    
    @Override
    public String getType() {
        return SupportedDialectType.MYSQL.getName();
    }
}
