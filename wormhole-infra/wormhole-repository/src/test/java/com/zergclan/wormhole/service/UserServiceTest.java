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

import com.zergclan.wormhole.factory.DataSourceModeFactory;
import com.zergclan.wormhole.po.User;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * test user service.
 */
public class UserServiceTest {

    @Test
    public void assertUserSync() {
        //new object
        DataSourceModeFactory dataSourceModeFactory = new DataSourceModeFactory();
        UserService userService = new UserService();

        //get origin database
        DataSource originDataSource = dataSourceModeFactory.getOriginDataSource();

        //get user from origin database
        User originUser = userService.queryUser(originDataSource);
        assertNotNull(originUser);

        //get target database
        DataSource targetDataSource = dataSourceModeFactory.getTargetDataSource();

        //insert user to target database
        boolean flag = userService.insertUser(targetDataSource, originUser);
        assertTrue(flag);

        //get user from target database
        User targetUser = userService.queryUser(targetDataSource);
        assertNotNull(targetUser);

        //validate
        boolean isOne = originUser.getUsername().equals(targetUser.getUsername());
        assertTrue(isOne);
    }
}
