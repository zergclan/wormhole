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

package com.zergclan.wormhole.jdbc.core;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.core.metadata.resource.IndexMetadata;
import com.zergclan.wormhole.core.metadata.resource.SchemaMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import com.zergclan.wormhole.jdbc.api.MetadataLoader;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public class ProxyMetaDataLoadTest {

    @Test
    public void assertLoadSchemas() throws SQLException {
        try (Connection connection = getConnection()) {
            MetadataLoader metaDataLoader = new ProxyMetadataLoad(connection);
            Collection<SchemaMetadata> collection = metaDataLoader.loadSchemas();

            System.out.println("schemas:");
            for (SchemaMetadata schemaMetadata : collection) {
                System.out.println(schemaMetadata.getName());
            }
        }

    }

    @Test
    public void assertLoadTables() throws SQLException {
        try (Connection connection = getConnection()) {
            MetadataLoader metaDataLoader = new ProxyMetadataLoad(connection);
            Collection<TableMetadata> collection = metaDataLoader.loadTables("slzx");

            System.out.println("tables:");
            for (TableMetadata tableMetadata : collection) {
                System.out.println(tableMetadata.getName());
            }
        }
    }

    @Test
    public void assertLoadViews() throws SQLException {
        try (Connection connection = getConnection()) {
            MetadataLoader metaDataLoader = new ProxyMetadataLoad(connection);
            Collection<TableMetadata> collection = metaDataLoader.loadViews("slzx");

            System.out.println("views:");
            for (TableMetadata tableMetadata : collection) {
                System.out.println(tableMetadata.getName());
            }
        }
    }

    @Test
    public void assertLoadColumns() throws SQLException {
        try (Connection connection = getConnection()) {
            MetadataLoader metaDataLoader = new ProxyMetadataLoad(connection);
            Collection<ColumnMetadata> collection = metaDataLoader.loadColumns("slzx", "source_class");

            System.out.println("tables:");
            for (ColumnMetadata columnMetadata : collection) {
                System.out.println(columnMetadata.getName());
            }
        }
    }

    @Test
    public void assertGetPrimaryKeys() throws SQLException {
        try (Connection connection = getConnection()) {
            MetadataLoader metaDataLoader = new ProxyMetadataLoad(connection);
            Optional<IndexMetadata> optional = metaDataLoader.getPrimaryKeys("slzx", "source_class");

            System.out.println("PrimaryKey:");
            if (optional.isPresent()) {
                System.out.println(optional.get().getName());
            }
        }
    }

    @Test
    public void assertLoadIndexes() throws SQLException {
        try (Connection connection = getConnection()) {
            MetadataLoader metaDataLoader = new ProxyMetadataLoad(connection);
            Collection<IndexMetadata> collection = metaDataLoader.loadIndexes("slzx", "source_class");

            System.out.println("index:");
            for (IndexMetadata columnMetadata : collection) {
                System.out.println(columnMetadata.getName());
            }
        }
    }

    private Connection getConnection() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/slzx?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai");
        config.setUsername("slzx");
        config.setPassword("slzx");

//        config.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//        config.setJdbcUrl("jdbc:oracle:thin:@192.168.0.145:1522:shanglian");
//        config.setUsername("shangliantest");
//        config.setPassword("klfh0923147IHlfh");
        return (new HikariDataSource(config)).getConnection();
    }
}
