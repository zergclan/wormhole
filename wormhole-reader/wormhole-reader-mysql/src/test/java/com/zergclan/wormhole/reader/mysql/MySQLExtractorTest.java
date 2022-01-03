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

import com.google.gson.Gson;
import com.zergclan.wormhole.core.metadata.ColumnMetaData;
import com.zergclan.wormhole.core.metadata.SchemaMetaData;
import com.zergclan.wormhole.core.metadata.TableMetaData;
import com.zergclan.wormhole.extracter.Extractor;
import com.zergclan.wormhole.repository.config.DataSourceManager;
import com.zergclan.wormhole.repository.entity.DataSourceInformation;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mysql extractor test.
 */
public final class MySQLExtractorTest {


    /**
     * Extract tables.
     */
//    @Test
    public void extractTables() {
        Extractor extractor = initExtractor();
        SchemaMetaData schemaMetaData = new SchemaMetaData("1", "source_db");
        Collection<TableMetaData> tableMetaData = extractor.extractTables(schemaMetaData);
        System.out.println(new Gson().toJson(tableMetaData));
        assertNotNull(tableMetaData);
    }

    /**
     * Extract columns.
     */
//    @Test
    public void extractColumns() {
        Extractor extractor = initExtractor();
        TableMetaData tableMetaData = new TableMetaData("2", "source_db", "source_table", null);
        Collection<ColumnMetaData> columnMetaData = extractor.extractColumns(tableMetaData);
        System.out.println(new Gson().toJson(columnMetaData));
        assertNotNull(columnMetaData);
    }

    /**
     * Extract datum.
     */
//    @Test
    public void extractDatum() {
        Extractor extractor = initExtractor();
        Map<String, ColumnMetaData> map = new LinkedHashMap<>();
        map.put("id", new ColumnMetaData("12", "source_db", "source_table", "id", null, false, null));
        map.put("transInt", new ColumnMetaData("12", "source_db", "source_table", "trans_int", null, false, null));
        map.put("transBigint", new ColumnMetaData("12", "source_db", "source_table", "trans_bigint", null, false, null));
        map.put("transVarchar", new ColumnMetaData("12", "source_db", "source_table", "trans_varchar", null, false, null));
        map.put("transDecimal", new ColumnMetaData("12", "source_db", "source_table", "trans_decimal", null, false, null));
        map.put("transDatetime", new ColumnMetaData("12", "source_db", "source_table", "trans_datetime", "datetime", false, null));
        map.put("createTime", new ColumnMetaData("12", "source_db", "source_table", "create_time", "datetime", false, null));
        Collection<Map<String, Object>> extractDatum = extractor.extractDatum(map);
        System.out.println(new Gson().toJson(extractDatum));
        assertNotNull(extractDatum);
    }

    /**
     * Init extractor.
     *
     * @return
     */
    private Extractor initExtractor() {
        DataSourceInformation dataSourceInformation = new DataSourceInformation();
        dataSourceInformation.setId("test");
        dataSourceInformation.setDbUser("root");
        dataSourceInformation.setDbPassword("123456");
        dataSourceInformation.setDbType("mysql");
        dataSourceInformation.setJdbcUrl("jdbc:mysql://10.168.1.10:3306?characterEncoding=utf-8&useSSL=false");
        return new MySQLExtractor(DataSourceManager.registerDataSource(dataSourceInformation));
    }

}
