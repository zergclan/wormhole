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

package com.zergclan.wormhole.jdbc.api;

import com.zergclan.wormhole.core.metadata.resource.ColumnMetaData;
import com.zergclan.wormhole.core.metadata.resource.IndexMetaData;
import com.zergclan.wormhole.core.metadata.resource.SchemaMetaData;
import com.zergclan.wormhole.core.metadata.resource.TableMetaData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public interface MetaDataLoader {

    /**
     * Load Schemas.
     *
     * @return {@link SchemaMetaData}
     * @throws SQLException exception
     */
    Collection<SchemaMetaData> loadSchemas() throws SQLException;

    /**
     * Load tables.
     *
     * @param schema schema name
     * @return {@link TableMetaData}
     * @throws SQLException exception
     */
    Collection<TableMetaData> loadTables(String schema) throws SQLException;

    /**
     * Load views.
     *
     * @param schema schema name
     * @return {@link TableMetaData}
     * @throws SQLException exception
     */
    Collection<TableMetaData> loadViews(String schema) throws SQLException;

    /**
     * Load Columns.
     *
     * @param schema schema name
     * @param table table name
     * @return {@link ColumnMetaData}
     * @throws SQLException exception
     */
    Collection<ColumnMetaData> loadColumns(String schema, String table) throws SQLException;

    /**
     * Load PrimaryKeys.
     *
     * @param schema schema name
     * @param table table name
     * @return {@link IndexMetaData}
     * @throws SQLException exception
     */
    Optional<IndexMetaData> getPrimaryKeys(String schema, String table) throws SQLException;

    /**
     * Load Indexes.
     *
     * @param schema schema name
     * @param table table name
     * @return {@link IndexMetaData}
     * @throws SQLException exception
     */
    Collection<IndexMetaData> loadIndexes(String schema, String table) throws SQLException;
}
