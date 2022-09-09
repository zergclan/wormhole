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

package com.zergclan.wormhole.extractor;

import com.zergclan.wormhole.common.data.DataGroup;
import com.zergclan.wormhole.common.expression.ExpressionProvider;
import com.zergclan.wormhole.common.expression.ExpressionProviderFactory;
import com.zergclan.wormhole.common.metadata.catched.CachedSourceMetaData;
import com.zergclan.wormhole.common.metadata.datasource.WormholeDataSourceMetaData;
import com.zergclan.wormhole.extractor.initializer.ResultSetDataGroupInitializer;
import com.zergclan.wormhole.tool.util.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Completed extractor of JDBC implemented.
 */
public abstract class JDBCCompletedExtractor implements WormholeExtractor<DataGroup> {
    
    private CachedSourceMetaData cachedSource;
    
    private ExpressionProvider expressionProvider;
    
    private ResultSetDataGroupInitializer dataGroupInitializer;
    
    @Override
    public void init(final CachedSourceMetaData cachedSource) {
        this.cachedSource = cachedSource;
        expressionProvider = ExpressionProviderFactory.getInstance(cachedSource);
        dataGroupInitializer = new ResultSetDataGroupInitializer(cachedSource.getDataNodes());
    }
    
    @Override
    public Collection<DataGroup> extract() throws SQLException {
        Collection<DataGroup> result = new LinkedList<>();
        try (Connection connection = createConnection(cachedSource.getDataSource())) {
            ResultSet resultSet = execute(connection, getSelectExpression(cachedSource));
            while (resultSet.next()) {
                result.add(dataGroupInitializer.init(resultSet));
            }
            return result;
        }
    }
    
    private String getSelectExpression(final CachedSourceMetaData cachedSource) {
        String result = cachedSource.getActualSql();
        if (StringUtil.isBlank(result)) {
            return expressionProvider.getSelectExpression();
        }
        return result;
    }
    
    /**
     * Get {@link Connection}.
     *
     * @param dataSourceMetaData {@link WormholeDataSourceMetaData}
     * @return {@link Connection}
     * @throws SQLException SQL exception
     */
    protected abstract Connection createConnection(WormholeDataSourceMetaData dataSourceMetaData) throws SQLException;
    
    /**
     * Execute.
     *
     * @param connection {@link Connection}
     * @param selectExpression select expression
     * @return {@link ResultSet}
     * @throws SQLException SQL exception
     */
    protected abstract ResultSet execute(Connection connection, String selectExpression) throws SQLException;
}
