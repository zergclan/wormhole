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

package com.zergclan.wormhole.plugin.mysql.old.reader.domain;

import com.zergclan.wormhole.common.util.StringUtil;
import com.zergclan.wormhole.plugin.mysql.old.reader.WormholeReaderConstants;

/**
 * Concat query metadata sql abstract class.
 */
public abstract class AbsJdbcConcatSqlDOM {

    /**
     * Get query all tables sql.
     * @param schema Schema.
     * @return Query sql.
     */
    public abstract String getQueryAllTablesSql(String schema);

    /**
     * Get query all columns sql.
     * @param schema Schema.
     * @param tableName TableName.
     * @return Query sql.
     */
    public abstract String getQueryAllColumnsSql(String schema, String tableName);

    /**
     * Get query table index sql.
     * @param schema Schema.
     * @param tableName TableName.
     * @return Query sql.
     */
    public abstract String getQueryTableIndexSql(String schema, String tableName);

    /**
     * Concat sql.
     *
     * @param selectColumn   Select columns.
     * @param fromTable      Select tableName.
     * @param whereCondition Select where condition.
     * @return Select sql.
     */
    protected String concatSql(final String selectColumn, final String fromTable, final String whereCondition) {
        StringBuilder sql = new StringBuilder(WormholeReaderConstants.SQL_SELECT + selectColumn + WormholeReaderConstants.SQL_FROM + fromTable);
        if (!StringUtil.isBlank(whereCondition)) {
            sql.append(WormholeReaderConstants.SQL_WHERE).append(whereCondition);
        }
        return sql.toString();
    }
}
