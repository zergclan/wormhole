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

import com.zergclan.wormhole.reader.mysql.domain.MetadataDOM;
import com.zergclan.wormhole.reader.mysql.entity.ColumnMetaData;
import com.zergclan.wormhole.reader.mysql.entity.IndexMetaData;
import com.zergclan.wormhole.reader.mysql.entity.TableMetaData;
import com.zergclan.wormhole.reader.mysql.rowmapper.ColumnMetaDataRowMapper;
import com.zergclan.wormhole.reader.mysql.rowmapper.IndexMetaDataRowMapper;
import com.zergclan.wormhole.reader.mysql.rowmapper.TableMetaDataRowMapper;
import com.zergclan.wormhole.repository.config.DataSourceManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;

/**
 * Get metadata by jdbc.
 */
public abstract class AbstractJdbcMetadataDOM implements MetadataDOM {

    protected abstract String getQueryAllTablesSql(String tableSchema);

    protected abstract String getQueryAllColumnsSql(String tableSchema, String tableName);

    protected abstract String getQueryTableIndexSql(String tableSchema, String tableName);

    /**
     * Query all tables metadata.
     * @param dataSourceId Registered data source id.
     * @param tableSchema The database to be queried.
     * @return Meta information for all tables.
     */
    @Override
    public List<TableMetaData> queryAllTables(final String dataSourceId, final String tableSchema) {
         // XXX: The MYSQL used now can get SQL according to the database type in the future.
        String queryAllTablesSql = getQueryAllTablesSql(tableSchema);
        return executeSql(dataSourceId, queryAllTablesSql, new TableMetaDataRowMapper());
    }

    /**
     * Query all columns metadata.
     * @param dataSourceId Registered data source id.
     * @param tableSchema The database to be queried.
     * @param tableName The tableName to be queried.
     * @return Meta information for all columns.
     */
    @Override
    public List<ColumnMetaData> queryAllColumns(final String dataSourceId, final String tableSchema, final String tableName) {
        //XXX: The MYSQL used now can get SQL according to the database type in the future.
        String queryAllColumnsSql = getQueryAllColumnsSql(tableSchema, tableName);
        return executeSql(dataSourceId, queryAllColumnsSql, new ColumnMetaDataRowMapper());
    }

    /**
     * Query unique index by table.
     * @param dataSourceId Registered data source id.
     * @param tableSchema The database to be queried.
     * @param tableName The tableName to be queried.
     * @return Meta information for table indexes.
     */
    @Override
    public List<IndexMetaData> queryUniqueIndexByTable(final String dataSourceId, final String tableSchema, final String tableName) {
        String queryTableIndexSql = getQueryTableIndexSql(tableSchema, tableName);
        List<IndexMetaData> indexMetaDataList = executeSql(dataSourceId, queryTableIndexSql, new IndexMetaDataRowMapper());
        indexMetaDataList.stream().forEach(indexMetaData -> {
            indexMetaData.setTableSchema(tableSchema);
        });
        return indexMetaDataList;

    }

    private <T> List<T> executeSql(final String dataSourceId, final String executeSql, final RowMapper<T> rowMapper) {
        JdbcTemplate jdbcTemplate = DataSourceManager.getDataSource(dataSourceId);
        return jdbcTemplate.query(executeSql, rowMapper);
    }

}
