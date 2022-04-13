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

package com.zergclan.wormhole.plugin.api;

import java.util.Collection;

/**
 * Expression builder.
 */
public interface ExpressionBuilder {
    
    /**
     * Wrap column.
     *
     * @param column column
     * @return wrap column
     */
    String wrapColumn(String column);
    
    /**
     * Build select column expression.
     *
     * @param table table
     * @param column column
     * @return select column expression
     */
    String buildSelectColumn(String table, String column);
    
    /**
     * Build select columns expression.
     *
     * @param table table
     * @param columns column
     * @return select columns expression
     */
    String buildSelectColumns(String table, Collection<String> columns);
    
    /**
     * Build insert column.
     *
     * @param column column
     * @return insert column
     */
    String buildInsertColumn(String column);
    
    /**
     * Build insert columns.
     *
     * @param columns columns
     * @return insert columns
     */
    String buildInsertColumns(Collection<String> columns);
    
    /**
     * Build update column.
     *
     * @param column column
     * @return update column
     */
    String buildUpdateColumn(String column);
    
    /**
     * Build update columns.
     *
     * @param columns column
     * @return update columns
     */
    String buildUpdateColumns(Collection<String> columns);
    
    /**
     * Build from table.
     *
     * @param table table
     * @return from table
     */
    String buildFromTable(String table);
    
    /**
     * Build where condition.
     *
     * @param condition condition
     * @return where condition
     */
    String buildWhere(String condition);
}
