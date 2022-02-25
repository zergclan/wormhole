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

package com.zergclan.wormhole.jdbc.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class JdbcUtil {
//    static String sql = "INSERT INTO t_order (order_id, user_id, order_name, type_char, type_smallint, type_boolean, type_enum, type_date, type_time, type_timestamp, type_decimal) " +
//            "VALUES (?, ?, ?, ?, ?, ?, CAST( ? AS season ), ?, ?, ?, ?);";
    
    static String sql = "INSERT INTO t_order (type_decimal) VALUES (?);";
    
    public static void main(String[] args) throws Exception {
//        String type = "proxy";
//        String database = "bmsql";

//        String type = "mysql";
//        String database = "benchmarksql";

        String type = "postgresql";
        String database = "benchmarksql";
    
//        String type = "mysql";
//        String database = "ds";
        
        
        final Connection connection = createDataSource(type, database).getConnection();
        execute(connection);
//        executeUpdate(connection);
//        executeBatch(connection);
    }
    
    private static void execute(final Connection connection) throws Exception  {
        // mysql
        //String sql = "SET NAMES 'utf8mb4'";
//        String sql = "SET CHARACTER SET 'utf8mb4';";
        // pg
//        String sql = "SET CHARACTER SET 'utf8mb4';";
//        String sql = "SET CLIENT_ENCODING TO 'UTF8';";
        String sql = "SET NAMES 'UTF8';";
        
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            final int row = preparedStatement.executeUpdate();
            System.out.println(row);
        }
    }
    
    private static void executeUpdate(final Connection connection) throws Exception {
        List<SQLValue> values = createValues(1);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (SQLValue each : values) {
                preparedStatement.setObject(each.getIndex(), each.getValue());
            }
            int rows = preparedStatement.executeUpdate();
            System.out.println(rows);
        }
    }
    
    private static void executeBatch(final Connection connection) throws Exception {
        Collection<List<SQLValue>> valueList = createValueList();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (List<SQLValue> each : valueList) {
                addBatch(preparedStatement, each);
            }
            int[] ints = preparedStatement.executeBatch();
            System.out.println(ints.length);
        }
    }
    
    private static Collection<List<SQLValue>> createValueList() throws Exception {
        Collection<List<SQLValue>> result = new LinkedList<>();
        result.add(createValues(1));
//        result.add(createValues(6));
        return result;
    }
    
    private static List<SQLValue> createValues(final int index) throws Exception {
        List<SQLValue> result = new LinkedList<>();
        result.add(createValue("999.01", "decimal", 1));
//        result.add(createValue(String.valueOf(index), "long", 1));
//        result.add(createValue("1", "int", 2));
//        result.add(createValue("pro_order", "String", 3));
//        result.add(createValue("S", "char", 4));
//        result.add(createValue("100", "smallint", 5));
//        result.add(createValue("true", "boolean", 6));
//        result.add(createValue("summer", "enum#season", 7));
//        result.add(createValue("2021-01-01", "Date", 8));
//        result.add(createValue("12:30:30", "time", 9));
//        result.add(createValue("2021-01-01 12:30:30", "timestamp", 10));
//        result.add(createValue("100.00", "decimal", 11));
        return result;
    }
    
    private static SQLValue createValue(final String value, final String type, final int index) throws Exception {
        return new SQLValue(value, type, index);
    }
    
    private static void addBatch(PreparedStatement preparedStatement, List<SQLValue> values) throws Exception {
        for (SQLValue each : values) {
            preparedStatement.setObject(each.getIndex(), each.getValue());
        }
        preparedStatement.addBatch();
    }
    
    static DataSource createDataSource(String type, String database) {
        HikariConfig config = new HikariConfig();
        if ("proxy".equals(type)) {
            config.setDriverClassName("org.postgresql.Driver");
            config.setJdbcUrl("jdbc:postgresql://127.0.0.1:3307/" + database);
            config.setUsername("root");
            config.setPassword("root");
        } else if ("mysql".equals(type)) {
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/" + database + "?serverTimezone=UTC&useSSL=false");
            config.setUsername("root");
            config.setPassword("123456");
        } else if ("postgresql".equals(type)) {
            config.setDriverClassName("org.postgresql.Driver");
            config.setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/" + database);
            config.setUsername("houyang");
            config.setPassword("123456");
        } else {
        
        }
        return new HikariDataSource(config);
    }
}
