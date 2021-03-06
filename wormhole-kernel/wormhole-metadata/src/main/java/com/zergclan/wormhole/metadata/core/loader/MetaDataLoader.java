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

package com.zergclan.wormhole.metadata.core.loader;

import com.zergclan.wormhole.metadata.core.resource.ColumnMetaData;
import com.zergclan.wormhole.metadata.core.resource.IndexMetaData;
import com.zergclan.wormhole.metadata.core.resource.SchemaMetaData;
import com.zergclan.wormhole.metadata.core.resource.TableMetaData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

/**
 * Meta data loader.
 */
public interface MetaDataLoader {

    /**
     * Load {@link SchemaMetaData}.
     *
     * @param dataSourceIdentifier data source identifier
     * @return {@link SchemaMetaData}
     * @throws SQLException exception
     */
    Collection<SchemaMetaData> loadSchemas(String dataSourceIdentifier) throws SQLException;

    /**
     * Load table as {@link TableMetaData}.
     *
     * @param dataSourceIdentifier data source identifier
     * @param schema schema name
     * @return {@link TableMetaData}
     * @throws SQLException exception
     */
    Collection<TableMetaData> loadTables(String dataSourceIdentifier, String schema) throws SQLException;

    /**
     * Load view as  {@link TableMetaData}.
     *
     * @param dataSourceIdentifier data source identifier
     * @param schema schema name
     * @return {@link TableMetaData}
     * @throws SQLException exception
     */
    Collection<TableMetaData> loadViews(String dataSourceIdentifier, String schema) throws SQLException;

    /**
     * Load {@link ColumnMetaData}.
     *
     * @param dataSourceIdentifier data source identifier
     * @param schema schema name
     * @param table table name
     * @return {@link ColumnMetaData}
     * @throws SQLException exception
     */
    Collection<ColumnMetaData> loadColumns(String dataSourceIdentifier, String schema, String table) throws SQLException;

    /**
     * Load primary key as {@link IndexMetaData}.
     *
     * @param dataSourceIdentifier data source identifier
     * @param schema schema name
     * @param table table name
     * @return {@link IndexMetaData}
     * @throws SQLException exception
     */
    Optional<IndexMetaData> getPrimaryKeys(String dataSourceIdentifier, String schema, String table) throws SQLException;

    /**
     * Load index as {@link IndexMetaData}.
     *
     * @param dataSourceIdentifier data source identifier
     * @param schema schema name
     * @param table table name
     * @return {@link IndexMetaData}
     * @throws SQLException exception
     */
    Collection<IndexMetaData> loadIndexes(String dataSourceIdentifier, String schema, String table) throws SQLException;
}
