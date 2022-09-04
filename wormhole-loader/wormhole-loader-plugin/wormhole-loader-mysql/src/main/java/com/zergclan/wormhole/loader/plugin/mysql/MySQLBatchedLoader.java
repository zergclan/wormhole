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

package com.zergclan.wormhole.loader.plugin.mysql;

import com.zergclan.wormhole.common.data.BatchedDataGroup;
import com.zergclan.wormhole.common.data.node.BigDecimalDataNode;
import com.zergclan.wormhole.common.data.node.DataGroup;
import com.zergclan.wormhole.common.data.node.DataNode;
import com.zergclan.wormhole.common.data.node.LongDataNode;
import com.zergclan.wormhole.common.data.node.PatternedDataTimeDataNode;
import com.zergclan.wormhole.common.data.result.BatchedLoadResult;
import com.zergclan.wormhole.common.data.result.ErrorDataGroup;
import com.zergclan.wormhole.common.data.result.LoadResultData;
import com.zergclan.wormhole.common.metadata.catched.CachedTargetMetaData;
import com.zergclan.wormhole.common.metadata.database.SupportedDialectType;
import com.zergclan.wormhole.jdbc.datasource.DataSourceManager;
import com.zergclan.wormhole.jdbc.execute.SQLExecutor;
import com.zergclan.wormhole.loader.AbstractBatchedLoader;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Batched loader of MySQL.
 */
@Slf4j
public final class MySQLBatchedLoader extends AbstractBatchedLoader {
    
    @Override
    protected BatchedLoadResult standardLoad(final BatchedDataGroup batchedDataGroup, final CachedTargetMetaData cachedTarget) {
        LoadResultData result = new LoadResultData(batchedDataGroup.getBatchSize());
        try (Connection connection = DataSourceManager.getDataSource(cachedTarget.getDataSource()).getConnection()) {
            for (DataGroup each : batchedDataGroup.getDataGroups()) {
                preFixDataGroup(each, cachedTarget);
                try {
                    loadDataGroup(connection, each, cachedTarget, result);
                } catch (final SQLException ex) {
                    result.addErrorData(new ErrorDataGroup(ex.getSQLState(), ex.getMessage(), each));
                    log.info("MySQL batched loader error, error data: [{}]", each);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("MySQL batched loader standard load success, load result: [{}]", result);
        return new BatchedLoadResult(true, result);
    }
    
    private void preFixDataGroup(final DataGroup dataGroup, final CachedTargetMetaData cachedTarget) {
        Collection<String> ignoreNodes = cachedTarget.getIgnoreNodes();
        for (String each : ignoreNodes) {
            dataGroup.remove(each);
        }
        dataGroup.register(new LongDataNode(cachedTarget.getVersionNode(), cachedTarget.getTaskBatch()));
    }
    
    private void loadDataGroup(final Connection connection, final DataGroup dataGroup, final CachedTargetMetaData cachedTarget, final LoadResultData loadResult) throws SQLException {
        try (ResultSet resultSet = executeSelect(connection, dataGroup, cachedTarget)) {
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
        }
    }
    
    private ResultSet executeSelect(final Connection connection, final DataGroup dataGroup, final CachedTargetMetaData cachedTarget) throws SQLException {
        return SQLExecutor.executeQuery(connection, getExpressionBuilder().buildSelect(), initWhereParameter(cachedTarget, dataGroup));
    }
    
    private Object[] initWhereParameter(final CachedTargetMetaData cachedTarget, final DataGroup dataGroup) {
        Collection<String> uniqueNodeNames = cachedTarget.getUniqueNodes();
        Object[] result = new Object[uniqueNodeNames.size()];
        int index = 0;
        for (String each : uniqueNodeNames) {
            result[index] = initSQLParameter(each, dataGroup);
            index++;
        }
        return result;
    }
    
    private Object initSQLParameter(final String nodeName, final DataGroup dataGroup) {
        DataNode<?> dataNode = dataGroup.getDataNode(nodeName);
        return dataNode instanceof PatternedDataTimeDataNode ? ((PatternedDataTimeDataNode) dataNode).getPatternedValue() : dataNode.getValue();
    }
    
    private boolean executeInsert(final Connection connection, final DataGroup dataGroup, final CachedTargetMetaData cachedTarget) throws SQLException {
        return 1 == SQLExecutor.executeUpdate(connection, getExpressionBuilder().buildInsert(), initInsertParameter(cachedTarget, dataGroup));
    }
    
    private Object[] initInsertParameter(final CachedTargetMetaData cachedTarget, final DataGroup dataGroup) {
        Collection<String> columns = new LinkedList<>(cachedTarget.getDataNodes().keySet());
        columns.add(cachedTarget.getVersionNode());
        Object[] result = new Object[columns.size()];
        int index = 0;
        for (String each : columns) {
            result[index] = initSQLParameter(each, dataGroup);
            index++;
        }
        return result;
    }
    
    private boolean executeUpdate(final Connection connection, final DataGroup dataGroup, final CachedTargetMetaData cachedTarget) throws SQLException {
        return 1 == SQLExecutor.executeUpdate(connection, getExpressionBuilder().buildUpdate(), initUpdateParameter(cachedTarget, dataGroup));
    }
    
    private Object[] initUpdateParameter(final CachedTargetMetaData cachedTarget, final DataGroup dataGroup) {
        Collection<String> columns = new LinkedList<>(cachedTarget.getDataNodes().keySet());
        columns.add(cachedTarget.getVersionNode());
        Collection<String> uniqueNodes = cachedTarget.getUniqueNodes();
        Object[] result = new Object[columns.size() + uniqueNodes.size()];
        int index = 0;
        for (String each : columns) {
            result[index] = initSQLParameter(each, dataGroup);
            index++;
        }
        for (String each : uniqueNodes) {
            result[index] = initSQLParameter(each, dataGroup);
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
    
    @Override
    public String getType() {
        return SupportedDialectType.MYSQL.getName();
    }
}
