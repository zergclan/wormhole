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
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.node.DataNodeMetaData;
import com.zergclan.wormhole.plugin.extractor.AbstractCompletedExtractor;
import com.zergclan.wormhole.plugin.mysql.builder.MySQLExpressionBuilder;
import com.zergclan.wormhole.plugin.mysql.util.JdbcTemplateCreator;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * Completed extractor of MySQL.
 */
public final class MySQLCompletedExtractor extends AbstractCompletedExtractor {
    
    private final MySQLExpressionBuilder expressionBuilder = new MySQLExpressionBuilder();
    
    @Override
    protected String generatorExtractSQl(final String table, final String conditionSql, final Map<String, DataNodeMetaData> dataNodes) {
        String selectColumns = expressionBuilder.buildSelectColumns(table, dataNodes.keySet());
        String fromTable = expressionBuilder.buildFromTable(table);
        String condition = expressionBuilder.buildWhere(conditionSql);
        return selectColumns + fromTable + condition;
    }

    @Override
    protected Collection<DataGroup> doExtract(final DataSourceMetaData dataSource, final Map<String, DataNodeMetaData> dataNodes, final String extractSQl) throws SQLException {
        Collection<DataGroup> result = new LinkedList<>();
        JdbcTemplate jdbcTemplate = JdbcTemplateCreator.create(dataSource);
        ResultSet resultSet = execute(jdbcTemplate.getDataSource().getConnection(), extractSQl);
        while (resultSet.next()) {
            result.add(createDataGroup(resultSet, dataNodes));
        }
        return result;
    }
    
    private ResultSet execute(final Connection connection, final String extractSQl) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(extractSQl)) {
            return preparedStatement.executeQuery();
        }
    }
    
    private DataGroup createDataGroup(final ResultSet resultSet, final Map<String, DataNodeMetaData> dataNodes) {
        DataGroup result = new DataGroup();
        return result;
    }
    
    @Override
    public String getType() {
        return "MySQL";
    }
}
