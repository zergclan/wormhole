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

package com.zergclan.wormhole.plugin.mysql.builder;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.common.constant.SQLKeywordConstant;
import com.zergclan.wormhole.plugin.api.ExpressionBuilder;

import java.util.Collection;
import java.util.StringJoiner;

/**
 * Implemented {@link ExpressionBuilder} for MySQL.
 */
public final class MySQLExpressionBuilder implements ExpressionBuilder {
    
    @Override
    public String wrapColumn(final String column) {
        return MarkConstant.BACK_QUOTE + column + MarkConstant.BACK_QUOTE;
    }
    
    @Override
    public String buildSelectColumn(final String table, final String column) {
        return table + MarkConstant.POINT + wrapColumn(column) + SQLKeywordConstant.AS + wrapColumn(column);
    }
    
    @Override
    public String buildSelectColumns(final String table, final Collection<String> columns) {
        if (columns.isEmpty()) {
            return SQLKeywordConstant.SELECT + MarkConstant.ASTERISK;
        }
        StringJoiner stringJoiner = new StringJoiner(MarkConstant.COMMA);
        for (String each : columns) {
            stringJoiner.add(buildSelectColumn(table, each));
        }
        return SQLKeywordConstant.SELECT + stringJoiner;
    }
    
    @Override
    public String buildInsertColumn(final String column) {
        return null;
    }
    
    @Override
    public String buildInsertColumns(final Collection<String> columns) {
        return null;
    }
    
    @Override
    public String buildUpdateColumn(final String column) {
        return null;
    }
    
    @Override
    public String buildUpdateColumns(final Collection<String> columns) {
        return null;
    }
    
    @Override
    public String buildFromTable(final String table) {
        return SQLKeywordConstant.FROM + table;
    }
    
    @Override
    public String buildWhere(final String condition) {
        return SQLKeywordConstant.WHERE + condition;
    }
}
