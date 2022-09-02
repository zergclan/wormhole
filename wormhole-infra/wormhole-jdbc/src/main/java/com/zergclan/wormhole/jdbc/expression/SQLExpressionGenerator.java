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

package com.zergclan.wormhole.jdbc.expression;

import com.zergclan.wormhole.common.metadata.datasource.DataSourceType;
import com.zergclan.wormhole.jdbc.constant.SQLKeywordConstant;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.tool.util.StringUtil;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Builder for SQL.
 */
@RequiredArgsConstructor
public final class SQLExpressionGenerator {
    
    private final DataSourceType dataSourceType;
    
    private final String table;
    
    private final Collection<String> nodeNames;
    
    private final Collection<String> uniqueNodeNames;
    
    private final String conditionSql;
    
    /**
     * Generate insert table.
     *
     * @return insert table
     */
    public String generateInsertTable() {
        return SQLKeywordConstant.INSERT + SQLKeywordConstant.INTO + table;
    }
    
    /**
     * Generate insert columns expression.
     *
     * @return insert columns expression
     */
    public String generateInsertColumns() {
        StringBuilder columnsBuilder = new StringBuilder();
        Iterator<String> iterator = nodeNames.iterator();
        columnsBuilder.append(generateColumnName(iterator.next()));
        while (iterator.hasNext()) {
            columnsBuilder.append(MarkConstant.COMMA).append(generateColumnName(iterator.next()));
        }
        return MarkConstant.LEFT_PARENTHESIS + columnsBuilder + MarkConstant.RIGHT_PARENTHESIS;
    }
    
    /**
     * Generate insert values expression.
     *
     * @return insert values expression
     */
    public String generateInsertValues() {
        StringBuilder valuesBuilder = new StringBuilder(MarkConstant.QUESTION);
        int size = nodeNames.size();
        for (int i = 1; i < size; i++) {
            valuesBuilder.append(MarkConstant.COMMA).append(MarkConstant.QUESTION);
        }
        return SQLKeywordConstant.VALUES + MarkConstant.LEFT_PARENTHESIS + valuesBuilder + MarkConstant.RIGHT_PARENTHESIS;
    }
    
    /**
     * Generate update table expression.
     *
     * @return update table expression
     */
    public String generateUpdateTable() {
        return SQLKeywordConstant.UPDATE + table;
    }
    
    /**
     * Generate update columns expression.
     *
     * @return update columns expression
     */
    public String generateUpdateColumns() {
        StringBuilder columnsBuilder = new StringBuilder();
        Iterator<String> iterator = nodeNames.iterator();
        columnsBuilder.append(generateColumnName(iterator.next())).append(MarkConstant.EQUAL).append(MarkConstant.QUESTION);
        while (iterator.hasNext()) {
            columnsBuilder.append(MarkConstant.COMMA).append(generateColumnName(iterator.next())).append(MarkConstant.EQUAL).append(MarkConstant.QUESTION);
        }
        return SQLKeywordConstant.SET + columnsBuilder;
    }
    
    /**
     * Generate select columns expression.
     *
     * @return select columns expression
     */
    public String generateSelectColumns() {
        String columnsExpression = nodeNames.isEmpty() ? MarkConstant.ASTERISK : StringUtil.join(generateSelectColumnsIterator(), MarkConstant.COMMA);
        return SQLKeywordConstant.SELECT + columnsExpression;
    }
    
    private Iterator<String> generateSelectColumnsIterator() {
        Collection<String> result = new ArrayList<>(nodeNames.size());
        for (String each : nodeNames) {
            result.add(generateColumnNameByTable(each) + SQLKeywordConstant.AS + generateColumnName(each));
        }
        return result.iterator();
    }
    
    /**
     * Generate from table for select expression.
     *
     * @return from table expression
     */
    public String generateFromTable() {
        return SQLKeywordConstant.FROM + table;
    }
    
    /**
     * Generate where expression by condition SQL.
     *
     * @return where expression
     */
    public String generateWhereByConditionSql() {
        return StringUtil.isBlank(conditionSql) ? "" : SQLKeywordConstant.WHERE + conditionSql;
    }
    
    /**
     * Generate where by all equals.
     *
     * @return where expression
     */
    public String generateWhereByAllEquals() {
        StringBuilder equalsBuilder = new StringBuilder();
        Iterator<String> iterator = uniqueNodeNames.iterator();
        equalsBuilder.append(generateColumnNameByTable(iterator.next())).append(MarkConstant.EQUAL).append(MarkConstant.QUESTION);
        while (iterator.hasNext()) {
            equalsBuilder.append(SQLKeywordConstant.AND).append(generateColumnNameByTable(iterator.next())).append(MarkConstant.EQUAL).append(MarkConstant.QUESTION);
        }
        return SQLKeywordConstant.WHERE + equalsBuilder;
    }
    
    private String generateColumnName(final String columnName) {
        return dataSourceType.getQuoteCharacter().wrap(columnName);
    }
    
    private String generateColumnNameByTable(final String columnName) {
        return table + MarkConstant.POINT + generateColumnName(columnName);
    }
}
