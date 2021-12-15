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

package com.zergclan.wormhole.reader.mysql.domain.impl;

import com.zergclan.wormhole.reader.mysql.entity.ColumnMetaData;
import com.zergclan.wormhole.reader.mysql.entity.TableMetaData;

/**
 * Get h2 metadata implementation.
 */
public final class H2MetadataDOMImpl extends AbstractJdbcMetadataDOM {

    @Override
    protected String getQueryAllTablesSql(final String dbName) {
        StringBuffer sql = new StringBuffer();
        sql.append("select TABLE_NAME " + TableMetaData.TABLE_NAME + ", TABLE_SCHEMA " + TableMetaData.TABLE_SCHEMA + ", TABLE_COMMENT " + TableMetaData.TABLE_COMMENT)
                .append(" from information_schema.TABLES where TABLE_SCHEMA = '" + dbName + "' and TABLE_TYPE = 'BASE TABLE'");
        return sql.toString();
    }

    @Override
    protected String getQueryAllColumnsSql(final String dbName, final String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("select TABLE_SCHEMA " + ColumnMetaData.TABLE_SCHEMA + ", TABLE_NAME " + ColumnMetaData.TABLE_NAME + ", COLUMN_NAME " + ColumnMetaData.COLUMN_NAME)
                .append(", DATA_TYPE " + ColumnMetaData.DATA_TYPE + ", COLUMN_COMMENT " + ColumnMetaData.COLUMN_COMMENT + ",COLUMN_TYPE " + ColumnMetaData.COLUMN_TYPE)
                .append(" from information_schema.COLUMNS ")
                .append("where TABLE_SCHEMA = '" + dbName + "' and TABLE_NAME = '" + tableName + "'");
        return sql.toString();
    }

    @Override
    protected String getQueryTableIndexSql(final String tableSchema, final String tableName) {
        return "show index from " + tableSchema + "." + tableName + " where Non_unique = 0";
    }
}
