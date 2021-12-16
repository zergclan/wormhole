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

package com.zergclan.wormhole.service;

import com.zergclan.wormhole.po.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * user service.
 */
public class UserService {
    /**
     * query user by data source.
     * @param  dataSource notnull
     * @return User
     */
    public User queryUser(final DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        BeanPropertyRowMapper<User> beanPropertyRowMapper = new BeanPropertyRowMapper(User.class);
        User user = jdbcTemplate.queryForObject("select * from user_info limit 1", beanPropertyRowMapper);
        return user;
    }

    /**
     * insert user by data source.
     * @param  dataSource notnull
     * @param user notnull
     * @return boolean
     */
    public boolean insertUser(final DataSource dataSource, final User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String insertSqlStr = "insert into user_info(username,password,email,is_enable,create_time,modify_time) values ('"
                + user.getUsername() + "','" + user.getPassword() + "','" + user.getEmail() + "','" + user.getIsEnable() + "','"
                + user.getCreateTime() + "','" + user.getModifyTime() + "')";

        jdbcTemplate.execute(insertSqlStr);
        return true;
    }
}
