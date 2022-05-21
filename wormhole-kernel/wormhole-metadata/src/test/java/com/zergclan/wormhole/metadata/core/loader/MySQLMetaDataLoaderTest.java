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

import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.resource.ColumnMetaData;
import com.zergclan.wormhole.metadata.core.resource.IndexMetaData;
import com.zergclan.wormhole.metadata.core.resource.SchemaMetaData;
import com.zergclan.wormhole.metadata.core.resource.TableMetaData;
import com.zergclan.wormhole.metadata.core.resource.dialect.H2DataSourceMetaData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class MySQLMetaDataLoaderTest {

    private String dataSourceIdentifier;

    private MetaDataLoader metaDataLoader;

    @BeforeAll
    public void init() throws SQLException {
        DataSourceMetaData dataSourceMetaData = new H2DataSourceMetaData("org.h2.Driver",
                "jdbc:h2:file:~/.h2/test;AUTO_SERVER=TRUE;MODE=mysql",
                "root", "root");
        dataSourceIdentifier = dataSourceMetaData.getIdentifier();
        DataSource dataSource = DataSourceCreator.create(dataSourceMetaData);
        metaDataLoader = new MySQLMetaDataLoader(dataSource.getConnection());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String createUserTableSqlStr = "CREATE TABLE IF NOT EXISTS `test` (\n"
                + "    `id` INT(11) AUTO_INCREMENT COMMENT '主键',\n"
                + "    `username` VARCHAR(64) NOT NULL COMMENT '用户名',\n"
                + "    `password` VARCHAR(64) NOT NULL COMMENT '用户密码',\n"
                + "    `email` VARCHAR(64) NOT NULL COMMENT '用户邮箱',\n"
                + "    `is_enable` tinyint(4) NOT NULL COMMENT '0未激活，1激活',\n"
                + "    `create_time` datetime(0) NOT NULL COMMENT '创建时间',\n"
                + "    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',\n"
                + "    PRIMARY KEY (`id`)\n"
                + ") ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '用户信息表';";
        jdbcTemplate.execute(createUserTableSqlStr);
        String insertUserTableSqlStr = "REPLACE  INTO `test`(`id`, `username`, `password`, `email`, `is_enable`, `create_time`, `modify_time`) "
                + "VALUES (1, 'h2', 'h2', 'test', 1, '2021-12-15 17:38:57', '2021-12-15 17:39:01');";
        jdbcTemplate.execute(insertUserTableSqlStr);
    }

    @Test
    public void assertLoadSchemas() throws SQLException {
        Collection<SchemaMetaData> collection = metaDataLoader.loadSchemas(dataSourceIdentifier);
        System.out.println("schemas:");
        for (SchemaMetaData schemaMetadata : collection) {
            System.out.println(schemaMetadata.getName());
        }
    }

    @Test
    public void assertLoadTables() throws SQLException {
        Collection<TableMetaData> collection = metaDataLoader.loadTables(dataSourceIdentifier, "TEST");

        System.out.println("tables:");
        for (TableMetaData tableMetadata : collection) {
            System.out.println(tableMetadata.getName());
        }
    }

    @Test
    public void assertLoadViews() throws SQLException {
        Collection<TableMetaData> collection = metaDataLoader.loadViews(dataSourceIdentifier, "TEST");

        System.out.println("views:");
        for (TableMetaData tableMetadata : collection) {
            System.out.println(tableMetadata.getName());
        }
    }

    @Test
    public void assertLoadColumns() throws SQLException {
        Collection<ColumnMetaData> collection = metaDataLoader.loadColumns(dataSourceIdentifier, "TEST", "TEST");

        System.out.println("tables:");
        for (ColumnMetaData columnMetadata : collection) {
            System.out.println(columnMetadata.getName());
        }
    }

    @Test
    public void assertGetPrimaryKeys() throws SQLException {
        Optional<IndexMetaData> optional = metaDataLoader.getPrimaryKeys(dataSourceIdentifier, "TEST", "TEST");

        System.out.println("PrimaryKey:");
        if (optional.isPresent()) {
            System.out.println(optional.get().getName());
        }
    }

    @Test
    public void assertLoadIndexes() throws SQLException {
        Collection<IndexMetaData> collection = metaDataLoader.loadIndexes(dataSourceIdentifier, "TEST", "TEST");

        System.out.println("index:");
        for (IndexMetaData columnMetadata : collection) {
            System.out.println(columnMetadata.getName());
        }
    }

}
