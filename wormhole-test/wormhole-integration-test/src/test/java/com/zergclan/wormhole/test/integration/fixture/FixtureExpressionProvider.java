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

package com.zergclan.wormhole.test.integration.fixture;

import com.zergclan.wormhole.common.expression.ExpressionProvider;
import com.zergclan.wormhole.common.metadata.catched.CachedMetaData;
import com.zergclan.wormhole.common.metadata.database.DatabaseType;
import com.zergclan.wormhole.jdbc.expression.SQLExpressionGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * Expression provider of fixture.
 */
@RequiredArgsConstructor
@Getter
public final class FixtureExpressionProvider implements ExpressionProvider {
    
    private final String insertExpression;
    
    public FixtureExpressionProvider(final DatabaseType databaseType, final String tableName, final Collection<String> nodeNames) {
        SQLExpressionGenerator generator = SQLExpressionGenerator.build(databaseType, tableName, nodeNames);
        insertExpression = generator.generateInsertTable() + generator.generateInsertColumns() + generator.generateInsertValues();
    }
    
    @Override
    public void init(final CachedMetaData cachedMetaData) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getUpdateExpression() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getSelectExpression() {
        throw new UnsupportedOperationException();
    }
}
