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

package com.zergclan.wormhole.jdbc.loader;

import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.core.metadata.resource.IndexMetadata;
import com.zergclan.wormhole.core.metadata.resource.SchemaMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

@RequiredArgsConstructor
public class MySQLMetadataLoader implements MetadataLoader {
    
    private final static String QUERY_SCHEMA_SQL = "SELECT SCHEMA_NAME AS  FROM information_schema.SCHEMATA;";
    
    private final static String QUERY_TABLE_SQL = "SELECT TABLE_NAME, TABLE_SCHEMA, TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = `%s`;";
    
    private final static String QUERY_COLUMN_SQL = "";
    
    private final static String QUERY_INDEX_SQL = "";
    
    private final String dataSourceIdentifier;
    
    @Override
    public Collection<SchemaMetadata> loadSchemas(final Connection connection) throws SQLException {
        ResultSet resultSet = connection.prepareStatement(QUERY_SCHEMA_SQL).executeQuery();
        Collection<SchemaMetadata> result = new LinkedList<>();
        while (resultSet.next()) {
            result.add(new SchemaMetadata(dataSourceIdentifier, resultSet.getString(1)));
        }
        return result;
    }
    
    @Override
    public Collection<TableMetadata> loadTables(final Connection connection, final String schema) throws SQLException {
        ResultSet resultSet = connection.prepareStatement(String.format(QUERY_TABLE_SQL, schema)).executeQuery();
        Collection<TableMetadata> result = new LinkedList<>();
        while (resultSet.next()) {
            result.add(new TableMetadata(dataSourceIdentifier, schema, resultSet.getString(1)));
        }
        return result;
    }
    
    @Override
    public Collection<ColumnMetadata> loadColumns(final Connection connection, final String schema, final String table) {
        return null;
    }
    
    @Override
    public Collection<IndexMetadata> loadIndex(final Connection connection, final String schema, final String table) {
        return null;
    }
}
