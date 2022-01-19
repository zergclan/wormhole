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

package com.zergclan.wormhole.writer.mysql;

import com.zergclan.wormhole.core.metadata.resource.DatabaseType;
import com.zergclan.wormhole.factory.DataSourceModeFactory;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * write data into mysql.
 */
public class MySQLLoaderTest {

    @Test
    public void assertMySQLLoader() {

        MySQLLoader loader = new MySQLLoader(getJdbcTemplate());
        loader.setTargetTable("target_table");
        loader.setSelectSql(getSelectSql());
        loader.setInsertSql(getInsertSql());
        loader.setUpdateSql(getUpdateSql());

        Map<String, Object> map = new HashMap<>(16);
        map.put("transInt", 1);
        map.put("transBigint", 1L);
        map.put("transVarchar", "test");
        map.put("transDecimal", new BigDecimal(18.2));
        map.put("transDatetime", new Date());

        loader.loaderData(map);
    }

    /**
     * get a data source by mode to be the target of sync data.
     * @return DataSource
     */
    private JdbcTemplate getJdbcTemplate() {
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
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='目标表';"
                + "create unique index UN_target_table on `target_table`(`trans_bigint`,`trans_varchar`);";
        jdbcTemplate.execute(createUserTableSqlStr);

        return jdbcTemplate;
    }

    private String getSelectSql() {
        return " select count(*) from target_table where trans_bigint = ? and trans_varchar = ? ";
    }

    private String getInsertSql() {
        return " insert into  target_table(trans_int,trans_bigint,trans_varchar,trans_decimal,trans_datetime,create_time,modify_time) "
                 + " values(?,?,?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP) ";
    }

    private String getUpdateSql() {
        return " update target_table set trans_int = ?, trans_decimal = ?, trans_datetime = ? where trans_bigint = ? and  trans_varchar = ? ";
    }
    
}
