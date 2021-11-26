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

import com.zergclan.wormhole.reader.mysql.domain.MysqlMetadataDOM;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * get mysql metadata Implementation.
 */
public final class MysqlMetadataDOMImpl implements MysqlMetadataDOM {

    @Override
    public List<Map<String, Object>> queryAllTables(final JdbcTemplate jt, final String dbName) {
        return jt.queryForList("select TABLE_NAME, TABLE_TYPE, TABLE_SCHEMA, TABLE_COMMENT from information_schema.TABLES where TABLE_SCHEMA = ?", dbName);
    }

    @Override
    public List<Map<String, Object>> queryAllColumns(final JdbcTemplate jt, final String dbName, final String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("select C.TABLE_SCHEMA,C.TABLE_NAME,C.COLUMN_NAME,C.DATA_TYPE, ")
                .append(" if(C.delimiter_index = 0, C.ps_val, substr(C.ps_val, 1, C.delimiter_index - 1)) COLUMN_LENGTH,")
                .append(" if(C.delimiter_index = 0, '', substr(C.ps_val, C.delimiter_index + 1)) COLUMN_SCALE,")
                .append(" C.COLUMN_COMMENT from (")
                .append(" select TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT,")
                .append(" replace(replace(replace(COLUMN_TYPE, DATA_TYPE, ''), '(', ''), ')', '') ps_val,")
                .append(" locate(',',replace(replace(replace(COLUMN_TYPE, DATA_TYPE, ''), '(', ''), ')', '')) delimiter_index")
                .append(" from information_schema.COLUMNS where TABLE_SCHEMA = ? and TABLE_NAME = ?) C");
        return jt.queryForList(sql.toString(), dbName, tableName);
    }
}
