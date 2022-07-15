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

package com.zergclan.wormhole.test.integration.demo;

import org.testcontainers.containers.MySQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DemoTest {
    
    /**
     * Test.
     *
     * @throws SQLException {@link SQLException}
     */
    public void test() throws SQLException {
        MySQLContainer mysql = new MySQLContainer("mysql:5.7").withUsername("root").withPassword("root").withDatabaseName("source_ds");
        mysql.start();
        final String databaseName = mysql.getDatabaseName();
        final String jdbcUrl = mysql.getJdbcUrl();
        final String username = mysql.getUsername();
        final String password = mysql.getPassword();
        final List portBindings = mysql.getPortBindings();
        System.out.println(jdbcUrl);
        System.out.println(databaseName);
        System.out.println(portBindings);
        System.out.println(username);
        System.out.println(password);
        Connection datasource = createDatasource(jdbcUrl, username, password);
        final Statement statement = datasource.createStatement();
        statement.execute("CREATE table docker_test (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY (`id`))");
        System.out.println("aaa");
    }
    
    private static Connection createDatasource(final String jdbcUrl, final String username, final String password) throws SQLException {
        return DriverManager.getConnection(jdbcUrl + "?allowPublicKeyRetrieval=true&serverTimezone=UTC&useSSL=false", username, password);
    }
}
