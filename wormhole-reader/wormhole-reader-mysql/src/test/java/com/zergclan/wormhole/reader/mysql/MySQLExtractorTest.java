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
import com.zergclan.wormhole.DataSourceManager;
import com.zergclan.wormhole.core.metadata.resource.ColumnMetadata;
import com.zergclan.wormhole.core.metadata.resource.SchemaMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import com.zergclan.wormhole.core.metadata.resource.dialect.MySQLDataSourceMetadata;
import com.zergclan.wormhole.extracter.Extractor;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

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
        SchemaMetadata schemaMetaData = new SchemaMetadata("1", "source_db");
        Collection<TableMetadata> tableMetaData = extractor.extractTables(schemaMetaData);
        System.out.println(new Gson().toJson(tableMetaData));
        assertNotNull(tableMetaData);
    }

    /**
     * Extract columns.
     */
//    @Test
    public void extractColumns() {
        Extractor extractor = initExtractor();
        TableMetadata tableMetadata = new TableMetadata("2", "source_db", "source_table", null);
        Collection<ColumnMetadata> columnMetaData = extractor.extractColumns(tableMetadata);
        System.out.println(new Gson().toJson(columnMetaData));
        assertNotNull(columnMetaData);
    }

    /**
     * Extract datum.
     */
//    @Test
    public void extractDatum() {
        Extractor extractor = initExtractor();
        Map<String, ColumnMetadata> map = new LinkedHashMap<>();
        map.put("id", new ColumnMetadata("12", "source_db", "source_table", "id", null, false, null));
        map.put("transInt", new ColumnMetadata("12", "source_db", "source_table", "trans_int", null, false, null));
        map.put("transBigint", new ColumnMetadata("12", "source_db", "source_table", "trans_bigint", null, false, null));
        map.put("transVarchar", new ColumnMetadata("12", "source_db", "source_table", "trans_varchar", null, false, null));
        map.put("transDecimal", new ColumnMetadata("12", "source_db", "source_table", "trans_decimal", null, false, null));
        map.put("transDatetime", new ColumnMetadata("12", "source_db", "source_table", "trans_datetime", "datetime", false, null));
        map.put("createTime", new ColumnMetadata("12", "source_db", "source_table", "create_time", "datetime", false, null));
        Collection<Map<String, Object>> extractDatum = extractor.extractDatum(map);
        System.out.println(new Gson().toJson(extractDatum));
        assertNotNull(extractDatum);
    }

    /**
     * Init extractor.
     *
     * @return Extractor.
     */
    private Extractor initExtractor() {
        int port = 3306;
        String host = "10.168.1.10";
        String username = "root";
        String password = "123456";
        String catalog = "mysql";
        Properties properties = new Properties();
        properties.setProperty("characterEncoding", "utf-8");
        properties.setProperty("useSSL", "false");
        MySQLDataSourceMetadata dataSourceMetadata = new MySQLDataSourceMetadata(host, port, username, password, catalog, properties);
        return new MySQLExtractor(DataSourceManager.register(dataSourceMetadata));
    }

}
