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
import com.zergclan.wormhole.common.util.StringUtil;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringJoiner;

/**
 * Expression builder for MySQL.
 */
public final class MySQLExpressionBuilder {
    
    /**
     * Build select columns expression.
     *
     * @param columns columns
     * @return select columns expression
     */
    public static String buildSelectColumns(final Collection<String> columns) {
        if (columns.isEmpty()) {
            return SQLKeywordConstant.SELECT + MarkConstant.ASTERISK;
        }
        StringJoiner stringJoiner = new StringJoiner(MarkConstant.COMMA);
        for (String each : columns) {
            stringJoiner.add(wrapColumn(each));
        }
        return SQLKeywordConstant.SELECT + stringJoiner;
    }
    
    /**
     * Build select columns expression.
     *
     * @param table table
     * @param columns columns
     * @return select columns expression
     */
    public static String buildSelectColumns(final String table, final Collection<String> columns) {
        if (columns.isEmpty()) {
            return SQLKeywordConstant.SELECT + MarkConstant.ASTERISK;
        }
        StringJoiner stringJoiner = new StringJoiner(MarkConstant.COMMA);
        for (String each : columns) {
            stringJoiner.add(buildASColumn(table, each));
        }
        return SQLKeywordConstant.SELECT + stringJoiner;
    }
    
    private static String buildASColumn(final String table, final String column) {
        return table + MarkConstant.POINT + wrapColumn(column) + SQLKeywordConstant.AS + wrapColumn(column);
    }
    
    private static String wrapColumn(final String column) {
        return wrap(column, MarkConstant.BACK_QUOTE, MarkConstant.BACK_QUOTE);
    }
    
    private static String wrap(final String inner, final String left, final String right) {
        return left + inner + right;
    }
    
    /**
     * Build from table expression.
     *
     * @param table table
     * @return from table expression
     */
    public static String buildFromTable(final String table) {
        return SQLKeywordConstant.FROM + table;
    }
    
    /**
     * Build where expression.
     *
     * @param condition condition sql
     * @return where expression
     */
    public static String buildConditionWhere(final String condition) {
        return StringUtil.isBlank(condition) ? "" : SQLKeywordConstant.WHERE + condition;
    }
    
    /**
     * Build where expression.
     *
     * @param columns condition sql
     * @return where expression
     */
    public static String buildAllEqualsWhere(final Collection<String> columns) {
        if (columns.isEmpty()) {
            return "";
        }
        StringBuilder equalsBuilder = new StringBuilder();
        Iterator<String> iterator = columns.iterator();
        equalsBuilder.append(wrapColumn(iterator.next())).append(MarkConstant.EQUAL).append(MarkConstant.QUESTION);
        while (iterator.hasNext()) {
            equalsBuilder.append(SQLKeywordConstant.AND).append(wrapColumn(iterator.next())).append(MarkConstant.EQUAL).append(MarkConstant.QUESTION);
        }
        return buildConditionWhere(equalsBuilder.toString());
    }
    
    /**
     * Build insert table expression.
     *
     * @param table table
     * @return insert table expression
     */
    public static String buildInsertTable(final String table) {
        return SQLKeywordConstant.INSERT + SQLKeywordConstant.INTO + table;
    }
    
    /**
     * Build insert columns values expression.
     *
     * @param columns columns
     * @return insert columns values expression
     */
    public static String buildInsertColumnsValues(final Collection<String> columns) {
        return buildInsertColumnsValues(columns, 1);
    }
    
    /**
     * Build insert columns values expression.
     *
     * @param columns columns
     * @param batchSize batch size
     * @return insert columns values expression
     */
    public static String buildInsertColumnsValues(final Collection<String> columns, final int batchSize) {
        if (columns.isEmpty()) {
            return "";
        }
        StringBuilder columnsBuilder = new StringBuilder();
        StringBuilder valuesBuilder = new StringBuilder();
        Iterator<String> iterator = columns.iterator();
        columnsBuilder.append(wrapColumn(iterator.next()));
        valuesBuilder.append(MarkConstant.QUESTION);
        while (iterator.hasNext()) {
            columnsBuilder.append(MarkConstant.COMMA).append(wrapColumn(iterator.next()));
            valuesBuilder.append(MarkConstant.COMMA).append(MarkConstant.QUESTION);
        }
        String columnsExpression = wrap(columnsBuilder.toString(), MarkConstant.LEFT_PARENTHESIS, MarkConstant.RIGHT_PARENTHESIS);
        String valuesExpression = wrap(valuesBuilder.toString(), MarkConstant.LEFT_PARENTHESIS, MarkConstant.RIGHT_PARENTHESIS);
        if (batchSize > 1) {
            StringBuilder batchValuesBuilder = new StringBuilder(valuesExpression);
            for (int i = 1; i < batchSize; i++) {
                batchValuesBuilder.append(MarkConstant.COMMA).append(valuesExpression);
            }
            valuesExpression = batchValuesBuilder.toString();
        }
        return columnsExpression + SQLKeywordConstant.VALUES + valuesExpression;
    }
    
    /**
     * Build update table expression.
     *
     * @param table table
     * @return update table expression
     */
    public static String buildUpdateTable(final String table) {
        return SQLKeywordConstant.UPDATE + table;
    }
    
    /**
     * Build set columns expression.
     *
     * @param columns columns
     * @return set columns expression
     */
    public static String buildSetColumns(final Collection<String> columns) {
        StringBuilder setColumnsBuilder = new StringBuilder();
        Iterator<String> iterator = columns.iterator();
        setColumnsBuilder.append(iterator.next()).append(MarkConstant.EQUAL).append(MarkConstant.QUESTION);
        while (iterator.hasNext()) {
            setColumnsBuilder.append(MarkConstant.COMMA).append(iterator.next()).append(MarkConstant.EQUAL).append(MarkConstant.QUESTION);
        }
        return SQLKeywordConstant.SET + setColumnsBuilder;
    }
}
