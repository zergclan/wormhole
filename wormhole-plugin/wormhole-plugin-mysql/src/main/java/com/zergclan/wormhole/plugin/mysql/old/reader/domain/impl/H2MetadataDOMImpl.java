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

package com.zergclan.wormhole.plugin.mysql.old.reader.domain.impl;

import com.zergclan.wormhole.plugin.mysql.old.reader.domain.AbsJdbcConcatSqlDOM;

/**
 * Get h2 metadata implementation.
 */
public final class H2MetadataDOMImpl extends AbsJdbcConcatSqlDOM {

    @Override
    public String getQueryAllTablesSql(final String schema) {
        String selectColumn = "TABLE_NAME, TABLE_SCHEMA, TABLE_COMMENT";
        String fromTable = "information_schema.TABLES";
        String whereCondition = "TABLE_SCHEMA = '" + schema + "' and TABLE_TYPE = 'BASE TABLE'";
        return concatSql(selectColumn, fromTable, whereCondition);
    }

    @Override
    public String getQueryAllColumnsSql(final String schema, final String tableName) {
        String selectColumn = "TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT,COLUMN_TYPE";
        String fromTable = "information_schema.COLUMNS";
        String whereCondition = "TABLE_SCHEMA = '" + schema + "' and TABLE_NAME = '" + tableName + "'";
        return concatSql(selectColumn, fromTable, whereCondition);
    }

    @Override
    public String getQueryTableIndexSql(final String schema, final String tableName) {
        return "show index from " + schema + "." + tableName + " where Non_unique = 0";
    }
}
