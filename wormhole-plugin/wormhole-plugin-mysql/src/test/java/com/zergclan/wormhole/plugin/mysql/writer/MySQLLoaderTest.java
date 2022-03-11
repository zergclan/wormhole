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

package com.zergclan.wormhole.plugin.mysql.writer;

import com.zergclan.wormhole.core.metadata.resource.DatabaseType;
import com.zergclan.wormhole.jdbc.core.DataSourceModeFactory;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * write data into mysql.
 */
public class MySQLLoaderTest {

    @Test
    public void assertMySQLLoader() {
        final MySQLLoader loader = new MySQLLoader(getDataSource());
        final Collection<Map<String, Object>> loadData = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(16);
        map.put("transInt", 1);
        map.put("transBigint", 1L);
        map.put("transVarchar", "test");
        map.put("transDecimal", new BigDecimal(18.2));
        map.put("transDatetime", new Date());
        map.put("createTime", new Date());
        map.put("modifyTime", new Date());
        loadData.add(map);
    }

    /**
     * get a data source by mode to be the target of sync data.
     * @return DataSource
     */
    private DataSource getDataSource() {
        DataSourceModeFactory dataSourceModeFactory = new DataSourceModeFactory();
        DataSource dataSource = dataSourceModeFactory.getDataSource(DatabaseType.MYSQL);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String createUserTableSqlStr = "CREATE TABLE `target_table` ("
                + "  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',"
                + "  `trans_int` int(1) NOT NULL default 1 COMMENT 'int类型',"
                + "  `trans_bigint` bigint(20) NOT NULL default 2 COMMENT 'bigint类型',"
                + "  `trans_varchar` varchar(32) NOT NULL default 'www' COMMENT 'varchar类型',"
                + "  `trans_decimal` decimal(18,2) NOT NULL COMMENT 'Decimal类型',"
                + "  `trans_datetime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'datetime类型',"
                + "  `create_time` datetime NOT NULL COMMENT '创建时间',"
                + "  `modify_time` datetime NOT NULL COMMENT '更新时间',"
                + "  PRIMARY KEY (`id`)"
                + ") ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT='目标表';"
                + "create unique index UN_target_table on `target_table`(`trans_bigint`,`trans_varchar`);";
        jdbcTemplate.execute(createUserTableSqlStr);

        return dataSource;
    }
    
//    private LoadContent createLoadContent(final Collection<Map<String, Object>> loadData) {
//        JdbcLoadContent jdbcLoadContent = new JdbcLoadContent();
//        jdbcLoadContent.setLoadData(loadData);
//
//        String databaseIdentifier = "MySQL#127.0.0.1:3306";
//        String schema = "target_db";
//        String table = "target_table";
//        String index = "index";
//        Collection<String> columnNames = new ArrayList<>(8);
//        columnNames.add("trans_int");
//        columnNames.add("trans_varchar");
//        IndexMetadata indexMetadata = new IndexMetadata(databaseIdentifier, schema, table, index, true, columnNames);
//        TableMetadata tableMetadata = new TableMetadata(databaseIdentifier, schema, table);
//
//        tableMetadata.registerColumn("id", new ColumnMetadata("12", "target_db", "target_table", "id", "INT(11)", "", false));
//        tableMetadata.registerColumn("transInt", new ColumnMetadata("12", "target_db", "target_table", "trans_int", "int(1)", "", false));
//        tableMetadata.registerColumn("transBigint", new ColumnMetadata("12", "target_db", "target_table", "trans_bigint", "bigint(20)", "", false));
//        tableMetadata.registerColumn("transVarchar", new ColumnMetadata("12", "target_db", "target_table", "trans_varchar", "varchar(32)", "", false));
//        tableMetadata.registerColumn("transDecimal", new ColumnMetadata("12", "target_db", "target_table", "trans_decimal", "decimal(18,2)", "", false));
//        tableMetadata.registerColumn("transDatetime", new ColumnMetadata("12", "target_db", "target_table", "trans_datetime", "datetime", "", false));
//        tableMetadata.registerColumn("createTime", new ColumnMetadata("12", "target_db", "target_table", "create_time", "datetime", "", false));
//        tableMetadata.registerColumn("modifyTime", new ColumnMetadata("12", "target_db", "target_table", "modify_time", "datetime", "", false));
//
//        tableMetadata.registerIndex(indexMetadata);
//
//        jdbcLoadContent.registerTable(tableMetadata);
//        return jdbcLoadContent;
//    }
}
