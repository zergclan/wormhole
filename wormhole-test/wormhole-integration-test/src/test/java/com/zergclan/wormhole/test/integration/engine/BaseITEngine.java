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

package com.zergclan.wormhole.test.integration.engine;

import com.zergclan.wormhole.plugin.mysql.builder.MySQLExpressionBuilder;
import com.zergclan.wormhole.test.integration.env.DataSourceEnvironment;
import com.zergclan.wormhole.test.integration.framework.container.DockerContainerDefinition;
import com.zergclan.wormhole.test.integration.framework.container.DatabaseITContainerManager;
import com.zergclan.wormhole.test.integration.framework.container.storage.DatabaseITContainer;
import com.zergclan.wormhole.test.integration.framework.data.Dataset;
import com.zergclan.wormhole.test.integration.framework.data.config.DatasetConfigurationLoader;
import com.zergclan.wormhole.test.integration.framework.data.node.ColumnNode;
import com.zergclan.wormhole.test.integration.framework.data.node.DataSourceNode;
import com.zergclan.wormhole.test.integration.framework.data.node.RowsNode;
import com.zergclan.wormhole.test.integration.framework.data.node.TableNode;
import com.zergclan.wormhole.test.integration.framework.param.WormholeParameterized;
import com.zergclan.wormhole.test.integration.framework.util.TimeSleeper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Base IT engine.
 */
@Getter(AccessLevel.PROTECTED)
public abstract class BaseITEngine {
    
    private final TimeSleeper sleeper;
    
    private final String scenario;
    
    private final Collection<DataSourceEnvironment> dataSources;
    
    private final DatabaseITContainerManager containerManager;
    
    private volatile Dataset dataset;
    
    private volatile DataSource source;
    
    private volatile DataSource target;
    
    public BaseITEngine(final WormholeParameterized parameterized) {
        scenario = parameterized.getScenario();
        dataSources = parameterized.getDataSources();
        sleeper = new TimeSleeper(50L);
        containerManager = new DatabaseITContainerManager(sleeper);
    }
    
    protected void postProcess() {
        containerManager.close();
        sleeper.sleep();
    }
    
    protected void preProcess() {
        initEnv();
        initDataSource();
        setData();
    }
    
    private void initEnv() {
        for (DataSourceEnvironment each : dataSources) {
            containerManager.register(new DockerContainerDefinition(scenario, each.getDataSourceType(), each.getPort()));
        }
        containerManager.start();
    }
    
    @SneakyThrows(IOException.class)
    private void initDataSource() {
        dataset = new Dataset(scenario, DatasetConfigurationLoader.load(scenario));
        DatabaseITContainer container = containerManager.getContainer(dataset.getIdentifier());
        source = container.getDataSource("ds_source");
        target = container.getDataSource("ds_target");
    }
    
    @SneakyThrows(SQLException.class)
    private void setData() {
        setSourceData(dataset.getDataSources().get("ds_source"));
        setTargetData(dataset.getDataSources().get("ds_target"));
    }
    
    private void setSourceData(final DataSourceNode sourceDataSourceNode) throws SQLException {
        try (Connection connection = source.getConnection()) {
            Map<String, TableNode> tables = sourceDataSourceNode.getTables();
            for (Entry<String, TableNode> entry : tables.entrySet()) {
                setInitData(entry.getValue(), connection);
            }
        }
    }
    
    private void setTargetData(final DataSourceNode targetDataSourceNode) throws SQLException {
        if (null == targetDataSourceNode) {
            return;
        }
        try (Connection connection = target.getConnection()) {
            Map<String, TableNode> tables = targetDataSourceNode.getTables();
            for (Entry<String, TableNode> entry : tables.entrySet()) {
                setInitData(entry.getValue(), connection);
            }
        }
    }
    
    private void setInitData(final TableNode tableNode, final Connection connection) throws SQLException {
        String insertSQL = initInsertSQL(tableNode);
        Collection<List<Object>> parameters = initInsertParameter(tableNode);
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        for (List<Object> each : parameters) {
            setParameters(preparedStatement, each);
        }
        preparedStatement.executeBatch();
    }
    
    private String initInsertSQL(final TableNode tableNode) {
        String insertExpression = MySQLExpressionBuilder.buildInsertTable(tableNode.getName());
        Collection<String> columnNames = tableNode.getColumns().stream().map(ColumnNode::getName).collect(Collectors.toCollection(LinkedList::new));
        String columnsValuesExpression = MySQLExpressionBuilder.buildInsertColumnsValues(columnNames, 1);
        return insertExpression + columnsValuesExpression;
    }
    
    private Collection<List<Object>> initInsertParameter(final TableNode tableNode) {
        Collection<List<Object>> result = new LinkedList<>();
        Collection<RowsNode> rows = tableNode.getRows();
        for (RowsNode each : rows) {
            result.add(each.getValues());
        }
        return result;
    }
    
    private void setParameters(final PreparedStatement preparedStatement, final List<Object> parameters) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            preparedStatement.setObject(i + 1, parameters.get(i));
        }
        preparedStatement.addBatch();
    }
}
