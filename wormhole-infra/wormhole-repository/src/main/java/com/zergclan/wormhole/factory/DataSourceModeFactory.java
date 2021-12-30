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

package com.zergclan.wormhole.factory;

import com.zergclan.wormhole.core.metadata.DatabaseType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * create factory for data source by h2.
 */
public final class DataSourceModeFactory {

    /**
     * get data source by mode.
     * @param  mode notnull
     * @return DataSource
     */
    public DataSource getDataSource(final DatabaseType mode) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        String url = "jdbc:h2:file:~/.h2/" + uuid + ";AUTO_SERVER=TRUE;MODE=" + mode.getName();
        System.out.println(url);
        dataSource.setUrl(url);
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

    /**
     * get a data source by mode to be the origin of sync data.
     * @return DataSource
     */
    public DataSource getOriginDataSource() {
        DataSource dataSource = getDataSource(DatabaseType.MYSQL);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String createUserTableSqlStr = "CREATE TABLE `user_info` (\n"
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
        String insertUserTableSqlStr = "INSERT INTO `user_info`(`id`, `username`, `password`, `email`, `is_enable`, `create_time`, `modify_time`) "
                + "VALUES (1, 'h2', 'h2', 'test', 1, '2021-12-15 17:38:57', '2021-12-15 17:39:01');";
        jdbcTemplate.execute(insertUserTableSqlStr);

        return dataSource;
    }

    /**
     * get a data source by mode to be the target of sync data.
     * @return DataSource
     */
    public DataSource getTargetDataSource() {
        DataSource dataSource = getDataSource(DatabaseType.MYSQL);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String createUserTableSqlStr = "CREATE TABLE `user_info` (\n"
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

        return dataSource;
    }
}
