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

package com.zergclan.wormhole.plugin.mysql.loader;

import com.zergclan.wormhole.data.api.node.DataNode;
import com.zergclan.wormhole.data.core.BatchedDataGroup;
import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.data.core.node.BigDecimalDataNode;
import com.zergclan.wormhole.data.core.node.LongDataNode;
import com.zergclan.wormhole.data.core.node.PatternedDataTimeDataNode;
import com.zergclan.wormhole.data.core.result.BatchedLoadResult;
import com.zergclan.wormhole.data.core.result.ErrorDataGroup;
import com.zergclan.wormhole.data.core.result.MysqlLoadResult;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.catched.CachedTargetMetaData;
import com.zergclan.wormhole.metadata.core.resource.DatabaseType;
import com.zergclan.wormhole.plugin.loader.AbstractBatchedLoader;
import com.zergclan.wormhole.plugin.mysql.builder.MySQLExpressionBuilder;
import com.zergclan.wormhole.plugin.mysql.util.JdbcTemplateCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.HashSet;
import java.util.Collection;

/**
 * Batched loader of MySQL.
 */
@Slf4j
public final class MySQLBatchedLoader extends AbstractBatchedLoader<MysqlLoadResult> {

    @Override
    protected BatchedLoadResult<MysqlLoadResult> standardLoad(final BatchedDataGroup batchedDataGroup, final CachedTargetMetaData cachedTarget) {
        MysqlLoadResult mysqlLoadResult = new MysqlLoadResult();
        Collection<DataGroup> dataGroups = batchedDataGroup.getDataGroups();
        for (DataGroup each : dataGroups) {
            preFix(each, cachedTarget);
            try {
                loadDataGroup(each, cachedTarget, mysqlLoadResult);
            } catch (final SQLException ex) {
                mysqlLoadResult.addErrorData(new ErrorDataGroup(ex.getSQLState(), ex.getMessage(), each));
                mysqlLoadResult.incrementErrorRow();
                log.info("MySQL batched loader error, error data: [{}]", each);
            }
        }
        mysqlLoadResult.setTotalRow(dataGroups.size());
        log.info("MySQL batched loader standard load success, load result: [{}]", mysqlLoadResult);
        return new BatchedLoadResult<>(true, mysqlLoadResult);
    }
    
    private void preFix(final DataGroup dataGroup, final CachedTargetMetaData cachedTarget) {
        Collection<String> ignoreNodes = cachedTarget.getIgnoreNodes();
        for (String each : ignoreNodes) {
            dataGroup.remove(each);
        }
        dataGroup.register(new LongDataNode(cachedTarget.getVersionNode(), cachedTarget.getTaskBatch()));
    }
    
    private void loadDataGroup(final DataGroup dataGroup, final CachedTargetMetaData cachedTarget, final MysqlLoadResult loadResult) throws SQLException {
        Connection connection = createConnection(cachedTarget.getDataSource());
        try {
            ResultSet resultSet = executeSelect(connection, dataGroup, cachedTarget);
            if (!resultSet.next()) {
                if (executeInsert(connection, dataGroup, cachedTarget)) {
                    loadResult.incrementInsertRow();
                    return;
                }
            }
            if (!compare(dataGroup, resultSet, cachedTarget)) {
                if (executeUpdate(connection, dataGroup, cachedTarget)) {
                    loadResult.incrementUpdateRow();
                    return;
                }
            }
            loadResult.incrementSameRow();
        } finally {
            connection.close();
        }
    }
    
    private Connection createConnection(final DataSourceMetaData dataSourceMetaData) throws SQLException {
        JdbcTemplate jdbcTemplate = JdbcTemplateCreator.create(dataSourceMetaData);
        DataSource dataSource = jdbcTemplate.getDataSource();
        if (null != dataSource) {
            return dataSource.getConnection();
        }
        throw new SQLTimeoutException();
    }
    
    private ResultSet executeSelect(final Connection connection, final DataGroup dataGroup, final CachedTargetMetaData cachedTarget) throws SQLException {
        String selectSQL = initSelectSQL(cachedTarget);
        String[] parameters = initWhereParameter(cachedTarget);
        PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(i + 1, dataGroup.getDataNode(parameters[i]).getValue());
        }
        return preparedStatement.executeQuery();
    }
    
    private String initSelectSQL(final CachedTargetMetaData cachedTarget) {
        String selectExpression = MySQLExpressionBuilder.buildSelectColumns(cachedTarget.getDataNodes().keySet());
        String fromTableExpression = MySQLExpressionBuilder.buildFromTable(cachedTarget.getTable());
        String whereExpression = MySQLExpressionBuilder.buildAllEqualsWhere(cachedTarget.getUniqueNodes());
        return selectExpression + fromTableExpression + whereExpression;
    }
    
    private String[] initWhereParameter(final CachedTargetMetaData cachedTarget) {
        Collection<String> uniqueNodes = cachedTarget.getUniqueNodes();
        String[] result = new String[uniqueNodes.size()];
        int index = 0;
        for (String each : uniqueNodes) {
            result[index] = each;
            index++;
        }
        return result;
    }
    
    private boolean executeInsert(final Connection connection, final DataGroup dataGroup, final CachedTargetMetaData cachedTarget) throws SQLException {
        String insertSQL = initInsertSQL(cachedTarget);
        String[] parameters = initInsertParameter(cachedTarget);
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        for (int i = 0; i < parameters.length; i++) {
            DataNode<?> dataNode = dataGroup.getDataNode(parameters[i]);
            if (dataNode instanceof PatternedDataTimeDataNode) {
                preparedStatement.setObject(i + 1, ((PatternedDataTimeDataNode) dataNode).getPatternedValue());
            } else {
                preparedStatement.setObject(i + 1, dataNode.getValue());
            }
        }
        return 1 == preparedStatement.executeUpdate();
    }
    
    private String initInsertSQL(final CachedTargetMetaData cachedTarget) {
        String insertExpression = MySQLExpressionBuilder.buildInsertTable(cachedTarget.getTable());
        Collection<String> columns = new HashSet<>(cachedTarget.getDataNodes().keySet());
        columns.add(cachedTarget.getVersionNode());
        String columnsValuesExpression = MySQLExpressionBuilder.buildInsertColumnsValues(columns);
        return insertExpression + columnsValuesExpression;
    }
    
    private String[] initInsertParameter(final CachedTargetMetaData cachedTarget) {
        Collection<String> columns = new HashSet<>(cachedTarget.getDataNodes().keySet());
        columns.add(cachedTarget.getVersionNode());
        String[] result = new String[columns.size()];
        int index = 0;
        for (String each : columns) {
            result[index] = each;
            index++;
        }
        return result;
    }
    
    private boolean compare(final DataGroup dataGroup, final ResultSet resultSet, final CachedTargetMetaData cachedTarget) throws SQLException {
        Collection<String> compareNodes = cachedTarget.getCompareNodes();
        for (String each : compareNodes) {
            Object object = resultSet.getObject(each);
            DataNode<?> dataNode = dataGroup.getDataNode(each);
            if (!compareNode(dataNode, object)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean compareNode(final DataNode<?> dataNode, final Object object) {
        if (dataNode instanceof PatternedDataTimeDataNode) {
            return ((PatternedDataTimeDataNode) dataNode).getPatternedValue().equals(String.valueOf(object));
        }
        if (dataNode instanceof BigDecimalDataNode) {
            return 0 == ((BigDecimalDataNode) dataNode).getValue().compareTo(new BigDecimal(String.valueOf(object)));
        }
        return String.valueOf(dataNode.getValue()).equals(String.valueOf(object));
    }
    
    private boolean executeUpdate(final Connection connection, final DataGroup dataGroup, final CachedTargetMetaData cachedTarget) throws SQLException {
        String updateSQL = initUpdateSQL(cachedTarget);
        String[] parameters = initUpdateParameter(cachedTarget);
        PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
        for (int i = 0; i < parameters.length; i++) {
            DataNode<?> dataNode = dataGroup.getDataNode(parameters[i]);
            if (dataNode instanceof PatternedDataTimeDataNode) {
                preparedStatement.setObject(i + 1, ((PatternedDataTimeDataNode) dataNode).getPatternedValue());
            } else {
                preparedStatement.setObject(i + 1, dataNode.getValue());
            }
        }
        return 1 == preparedStatement.executeUpdate();
    }
    
    private String initUpdateSQL(final CachedTargetMetaData cachedTarget) {
        String updateExpression = MySQLExpressionBuilder.buildUpdateTable(cachedTarget.getTable());
        Collection<String> columns = new HashSet<>(cachedTarget.getDataNodes().keySet());
        columns.add(cachedTarget.getVersionNode());
        String setColumnsExpression = MySQLExpressionBuilder.buildSetColumns(columns);
        String whereExpression = MySQLExpressionBuilder.buildAllEqualsWhere(cachedTarget.getUniqueNodes());
        return updateExpression + setColumnsExpression + whereExpression;
    }
    
    private String[] initUpdateParameter(final CachedTargetMetaData cachedTarget) {
        Collection<String> columns = new HashSet<>(cachedTarget.getDataNodes().keySet());
        columns.add(cachedTarget.getVersionNode());
        Collection<String> uniqueNodes = cachedTarget.getUniqueNodes();
        String[] result = new String[columns.size() + uniqueNodes.size()];
        int index = 0;
        for (String each : columns) {
            result[index] = each;
            index++;
        }
        for (String each : uniqueNodes) {
            result[index] = each;
            index++;
        }
        return result;
    }
    
    @Override
    public String getType() {
        return DatabaseType.MYSQL.getName();
    }
}
