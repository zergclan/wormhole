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

import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.core.metadata.resource.IndexMetadata;
import com.zergclan.wormhole.core.metadata.resource.SchemaMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public interface MetadataLoader {

    /**
     * Load Schemas.
     *
     * @param connection {@link Connection}
     * @return {@link SchemaMetadata}
     * @throws SQLException exception
     */
    Collection<SchemaMetadata> loadSchemas(Connection connection) throws SQLException;

    /**
     * Load Schemas.
     *
     * @param connection {@link Connection}
     * @param schema schema name
     * @return {@link TableMetadata}
     * @throws SQLException exception
     */
    Collection<TableMetadata> loadTables(Connection connection, String schema) throws SQLException;

    /**
     * Load Columns.
     *
     * @param connection {@link Connection}
     * @param schema schema name
     * @param table table name
     * @return {@link ColumnMetadata}
     * @throws SQLException exception
     */
    Collection<ColumnMetadata> loadColumns(Connection connection, String schema, String table) throws SQLException;

    /**
     * Load Indexes.
     *
     * @param connection {@link Connection}
     * @param schema schema name
     * @param table table name
     * @return {@link IndexMetadata}
     * @throws SQLException exception
     */
    Collection<IndexMetadata> loadIndexes(Connection connection, String schema, String table) throws SQLException;
}
