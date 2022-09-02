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

import com.zergclan.wormhole.common.expression.ExpressionBuilder;
import com.zergclan.wormhole.common.metadata.catched.CachedMetaData;
import com.zergclan.wormhole.common.metadata.catched.CachedTargetMetaData;
import com.zergclan.wormhole.common.metadata.datasource.DataSourceType;
import com.zergclan.wormhole.jdbc.expression.SQLExpressionGenerator;
import com.zergclan.wormhole.tool.util.StringUtil;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Load expression builder of MySQL.
 */
public final class MySQLLoadExpressionBuilder implements ExpressionBuilder {
    
    private SQLExpressionGenerator generator;
    
    @Override
    public void init(final CachedMetaData cachedMetaData) {
        if (cachedMetaData instanceof CachedTargetMetaData) {
            CachedTargetMetaData targetMetaData = (CachedTargetMetaData) cachedMetaData;
            DataSourceType dataSourceType = targetMetaData.getDataSource().getDataSourceType();
            String table = targetMetaData.getTable();
            Collection<String> nodeNames = initNodeNames(targetMetaData);
            Collection<String> uniqueNodeNames = targetMetaData.getUniqueNodes();
            generator = new SQLExpressionGenerator(dataSourceType, table, nodeNames, uniqueNodeNames, null);
        }
        throw new UnsupportedOperationException();
    }
    
    private Collection<String> initNodeNames(final CachedTargetMetaData targetMetaData) {
        Collection<String> result = new LinkedList<>(targetMetaData.getDataNodes().keySet());
        String versionNode = targetMetaData.getVersionNode();
        if (!StringUtil.isBlank(versionNode)) {
            result.add(versionNode);
        }
        return result;
    }
    
    @Override
    public String buildSelect() {
        return generator.generateSelectColumns() + generator.generateFromTable() + generator.generateWhereByAllEquals();
    }
    
    @Override
    public String buildInsert() {
        return generator.generateInsertTable() + generator.generateInsertColumns() + generator.generateInsertValues();
    }
    
    @Override
    public String buildUpdate() {
        return generator.generateUpdateTable() + generator.generateUpdateColumns() + generator.generateWhereByAllEquals();
    }
    
    @Override
    public String getType() {
        return "MySQL@target";
    }
}
