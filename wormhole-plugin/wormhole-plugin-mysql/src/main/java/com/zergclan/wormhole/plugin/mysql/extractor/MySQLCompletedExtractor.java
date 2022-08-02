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

package com.zergclan.wormhole.plugin.mysql.extractor;

import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.data.core.node.DataNodeBuilder;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.datasource.dialect.DatabaseType;
import com.zergclan.wormhole.metadata.core.plan.node.DataNodeMetaData;
import com.zergclan.wormhole.plugin.extractor.AbstractCompletedExtractor;
import com.zergclan.wormhole.plugin.mysql.builder.MySQLExpressionBuilder;
import com.zergclan.wormhole.plugin.mysql.util.JdbcTemplateCreator;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Completed extractor of MySQL.
 */
public final class MySQLCompletedExtractor extends AbstractCompletedExtractor {
    
    @Override
    protected String generatorExtractSQl(final String table, final String conditionSql, final Map<String, DataNodeMetaData> dataNodes) {
        String selectColumns = MySQLExpressionBuilder.buildSelectColumns(table, dataNodes.keySet());
        String fromTable = MySQLExpressionBuilder.buildFromTable(table);
        String condition = MySQLExpressionBuilder.buildConditionWhere(conditionSql);
        return selectColumns + fromTable + condition;
    }

    @Override
    protected Collection<DataGroup> doExtract(final DataSourceMetaData dataSource, final Map<String, DataNodeMetaData> dataNodes, final String extractSQl) throws SQLException {
        Connection connection = createConnection(dataSource);
        return execute(connection, dataNodes, extractSQl);
    }
    
    private Connection createConnection(final DataSourceMetaData dataSourceMetaData) throws SQLException {
        JdbcTemplate jdbcTemplate = JdbcTemplateCreator.create(dataSourceMetaData);
        DataSource dataSource = jdbcTemplate.getDataSource();
        if (null != dataSource) {
            return dataSource.getConnection();
        }
        throw new SQLTimeoutException();
    }
    
    private Collection<DataGroup> execute(final Connection connection, final Map<String, DataNodeMetaData> dataNodes, final String extractSQl) throws SQLException {
        Collection<DataGroup> result = new LinkedList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(extractSQl)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(createDataGroup(resultSet, dataNodes));
            }
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
        return DatabaseType.MYSQL.getName();
    }
}
