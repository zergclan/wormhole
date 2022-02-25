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

package com.zergclan.wormhole.jdbc.loader;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetadataLoaderImpl {
    
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = initDataSource();
        Connection connection = dataSource.getConnection();
    
        // schemaMetadataLoader
        // tableMetadataLoader
        // columnMetadataLoader
        // indexMetadataLoader
        
        
        
        
        
        String sql = "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
    
//        DatabaseMetaData metaData = connection.getMetaData();
//        ResultSet schemas = metaData.getSchemas();
//        ResultSet catalogs = metaData.getCatalogs();
        printSchemas(resultSet);
        
        //ResultSet catalogs = metaData.getCatalogs();
    
    
    }
    
    private static void printSchemas(final ResultSet schemas) throws SQLException  {
        while (schemas.next()) {
            String string = schemas.getString(1);
            System.out.println(string);
        }
    }
    
    private static DataSource initDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/ds");
        config.setUsername("root");
        config.setPassword("123456");
        return new HikariDataSource(config);
    }
}
