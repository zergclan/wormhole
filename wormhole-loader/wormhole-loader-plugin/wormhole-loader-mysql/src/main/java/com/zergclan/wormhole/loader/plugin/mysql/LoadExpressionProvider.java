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

import com.zergclan.wormhole.common.expression.ExpressionProvider;
import com.zergclan.wormhole.common.metadata.catched.CachedMetaData;
import com.zergclan.wormhole.common.metadata.catched.CachedTargetMetaData;
import com.zergclan.wormhole.common.metadata.database.DatabaseType;
import com.zergclan.wormhole.jdbc.expression.SQLExpressionGenerator;
import com.zergclan.wormhole.tool.util.StringUtil;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Expression provider of Loader.
 */
@Getter
public final class LoadExpressionProvider implements ExpressionProvider {
    
    private String insertExpression;
    
    private String updateExpression;
    
    private String selectExpression;
    
    @Override
    public void init(final CachedMetaData cachedMetaData) {
        if (cachedMetaData instanceof CachedTargetMetaData) {
            SQLExpressionGenerator generator = initSQLExpressionGenerator((CachedTargetMetaData) cachedMetaData);
            insertExpression = generator.generateInsertTable() + generator.generateInsertColumns() + generator.generateInsertValues();
            updateExpression = generator.generateUpdateTable() + generator.generateUpdateColumns() + generator.generateWhereByAllEquals();
            selectExpression = generator.generateSelectColumns() + generator.generateFromTable() + generator.generateWhereByAllEquals();
            return;
        }
        throw new UnsupportedOperationException();
    }
    
    private SQLExpressionGenerator initSQLExpressionGenerator(final CachedTargetMetaData targetMetaData) {
        DatabaseType databaseType = targetMetaData.getDataSource().getDatabaseType();
        String table = targetMetaData.getTable();
        Collection<String> nodeNames = initNodeNames(targetMetaData);
        Collection<String> uniqueNodeNames = targetMetaData.getUniqueNodes();
        return SQLExpressionGenerator.build(databaseType, table, nodeNames, uniqueNodeNames);
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
    public String getType() {
        return "load#MySQL";
    }
}
