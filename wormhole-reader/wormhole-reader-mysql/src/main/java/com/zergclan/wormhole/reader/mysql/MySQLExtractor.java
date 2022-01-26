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

package com.zergclan.wormhole.reader.mysql;

import com.zergclan.wormhole.common.util.StringUtil;
import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.core.metadata.resource.IndexMetadata;
import com.zergclan.wormhole.core.metadata.resource.SchemaMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import com.zergclan.wormhole.extracter.Extractor;
import com.zergclan.wormhole.reader.mysql.domain.AbsJdbcConcatSqlDOM;
import com.zergclan.wormhole.reader.mysql.domain.impl.MysqlConcatSqlDOMImpl;
import com.zergclan.wormhole.reader.mysql.rowmapper.mysql.ColumnMetaDataRowMapper;
import com.zergclan.wormhole.reader.mysql.rowmapper.mysql.TableMetaDataRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collection;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.ArrayList;

/**
 * Extractor for MySQL.
 */
@RequiredArgsConstructor
public final class MySQLExtractor implements Extractor {

    private final JdbcTemplate jdbcTemplate;

    // TODO WTL 根据不同数据库类型获取方式
    private final AbsJdbcConcatSqlDOM jdbcConcatSqlDOM = new MysqlConcatSqlDOMImpl();

    @Override
    public Collection<TableMetadata> extractTables(final SchemaMetadata schemaMetaData) {
        String queryAllTablesSql = jdbcConcatSqlDOM.getQueryAllTablesSql(schemaMetaData.getName());
        return executeSql(queryAllTablesSql, new TableMetaDataRowMapper(schemaMetaData.getDataSourceIdentifier())).orElseGet(ArrayList::new);
    }

    private <T> Optional<List<T>> executeSql(final String executeSql, final RowMapper<T> rowMapper) {
        return Optional.of(jdbcTemplate.query(executeSql, rowMapper));
    }

    @Override
    public Collection<ColumnMetadata> extractColumns(final TableMetadata table) {
        String queryAllColumnsSql = jdbcConcatSqlDOM.getQueryAllColumnsSql(table.getSchema(), table.getName());
        return executeSql(queryAllColumnsSql, new ColumnMetaDataRowMapper(table.getDataSourceIdentifier())).orElseGet(ArrayList::new);
    }

    @Override
    public Collection<IndexMetadata> extractIndexes(final TableMetadata table) {
        // TODO
        return null;
    }

    @Override
    public Collection<Map<String, Object>> extractData(final Map<String, ColumnMetadata> columns) {
        StringJoiner selectColumns = new StringJoiner(", ");
        String tableName = "";
        for (Map.Entry<String, ColumnMetadata> column : columns.entrySet()) {
            if (StringUtil.isBlank(tableName)) {
                tableName = column.getValue().getSchema() + "." + column.getValue().getTable();
            }
            selectColumns.add(column.getValue().getName() + WormholeReaderConstants.SQL_AS + column.getKey());
        }
        String queryDataSql = WormholeReaderConstants.SQL_SELECT + selectColumns + WormholeReaderConstants.SQL_FROM + tableName;
        return jdbcTemplate.queryForList(queryDataSql);
    }
}
