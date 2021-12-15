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

import com.google.gson.Gson;
import com.zergclan.wormhole.reader.mysql.domain.MetadataDOM;
import com.zergclan.wormhole.reader.mysql.entity.ColumnMetaData;
import com.zergclan.wormhole.reader.mysql.entity.IndexMetaData;
import com.zergclan.wormhole.reader.mysql.entity.TableMetaData;
import com.zergclan.wormhole.repository.config.DataSourceManager;
import com.zergclan.wormhole.repository.entity.DataSourceInformation;
import org.junit.jupiter.api.Assertions;
import java.util.List;

/**
 * Test to get mysql metadata.
 */
public final class MysqlMetadataDOMImplTest {

    private final String dataSourceId = "test";

    /**
     * Test query all tables metadata.
     */
//    @Test
    public void queryAllTables() {
        registerDataSource();
        MetadataDOM h2MetadataDOM = new MysqlMetadataDOMImpl();
        List<TableMetaData> allTables = h2MetadataDOM.queryAllTables(dataSourceId, "mysql");
        System.out.println(new Gson().toJson(allTables));
        Assertions.assertTrue(allTables.size() > 1);
    }

    /**
     * Test query all columns metadata.
     */
//    @Test
    public void queryAllColumns() {
        registerDataSource();
        MetadataDOM h2MetadataDOM = new MysqlMetadataDOMImpl();
        List<ColumnMetaData> allColumns = h2MetadataDOM.queryAllColumns(dataSourceId, "mysql", "user");
        System.out.println(new Gson().toJson(allColumns));
        Assertions.assertTrue(allColumns.size() > 1);
    }

    /**
     * Test query unique index.
     */
//    @Test
    public void queryUniqueIndexByTable() {
        registerDataSource();
        MetadataDOM h2MetadataDOM = new MysqlMetadataDOMImpl();
        List<IndexMetaData> uniqueIndexByTable = h2MetadataDOM.queryUniqueIndexByTable(dataSourceId, "mysql", "user");
        System.out.println(new Gson().toJson(uniqueIndexByTable));
        Assertions.assertTrue(uniqueIndexByTable.size() > 1);
    }

    private void registerDataSource() {
        DataSourceInformation dataSourceInformation = new DataSourceInformation();
        dataSourceInformation.setId(dataSourceId);
        dataSourceInformation.setDbUser("root");
        dataSourceInformation.setDbPassword("koma1993");
        dataSourceInformation.setDbType("mysql");
        dataSourceInformation.setJdbcUrl("jdbc:mysql://192.168.112.132:3306/test01?characterEncoding=utf-8&useSSL=false");
        DataSourceManager.registerDataSource(dataSourceInformation);
    }

}
