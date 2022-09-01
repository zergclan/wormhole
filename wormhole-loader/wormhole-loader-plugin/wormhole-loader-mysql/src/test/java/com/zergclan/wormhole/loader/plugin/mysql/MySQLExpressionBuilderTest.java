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

package com.zergclan.wormhole.loader.plugin.mysql;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MySQLExpressionBuilderTest {
    
    @Test
    public void assertBuildSelectColumns() {
        String expectedSelectColumns = "SELECT `id`,`name`";
        Collection<String> columns = new LinkedList<>();
        columns.add("id");
        columns.add("name");
        assertEquals(expectedSelectColumns, MySQLExpressionBuilder.buildSelectColumns(columns));
    }
    
    @Test
    public void assertBuildSelectAsColumns() {
        String expectedSelectColumns = "SELECT t_user.`id` AS `id`,t_user.`name` AS `name`";
        Collection<String> columns = new LinkedList<>();
        columns.add("id");
        columns.add("name");
        assertEquals(expectedSelectColumns, MySQLExpressionBuilder.buildSelectColumns("t_user", columns));
    }
    
    @Test
    public void assertBuildFromTable() {
        String expectedFromTable = " FROM t_user";
        assertEquals(expectedFromTable, MySQLExpressionBuilder.buildFromTable("t_user"));
    }
    
    @Test
    public void assertBuildConditionWhere() {
        String expectedWhere = " WHERE id = 1";
        assertEquals(expectedWhere, MySQLExpressionBuilder.buildConditionWhere("id = 1"));
    }
    
    @Test
    public void assertBuildAllEqualsWhere() {
        String expectedWhere = " WHERE `id`=? AND `name`=?";
        Collection<String> columns = new LinkedList<>();
        columns.add("id");
        columns.add("name");
        assertEquals(expectedWhere, MySQLExpressionBuilder.buildAllEqualsWhere(columns));
    }
    
    @Test
    public void assertBuildInsertTable() {
        String expectedInsertTable = "INSERT INTO t_user";
        assertEquals(expectedInsertTable, MySQLExpressionBuilder.buildInsertTable("t_user"));
    }
    
    @Test
    public void assertBuildInsertColumnsValues() {
        String expectedInsertColumnsValues = "(`id`,`name`) VALUES (?,?)";
        Collection<String> columns = new LinkedList<>();
        columns.add("id");
        columns.add("name");
        assertEquals(expectedInsertColumnsValues, MySQLExpressionBuilder.buildInsertColumnsValues(columns));
    }
    
    @Test
    public void assertBuildInsertBatchColumnsValues() {
        String expectedInsertColumnsValues = "(`id`,`name`) VALUES (?,?),(?,?),(?,?)";
        Collection<String> columns = new LinkedList<>();
        columns.add("id");
        columns.add("name");
        assertEquals(expectedInsertColumnsValues, MySQLExpressionBuilder.buildInsertColumnsValues(columns, 3));
    }
    
    @Test
    public void assertBuildUpdateTable() {
        String expectedUpdateTable = "UPDATE t_user";
        assertEquals(expectedUpdateTable, MySQLExpressionBuilder.buildUpdateTable("t_user"));
    }
    
    @Test
    public void assertBuildSetColumns() {
        String expectedSetColumns = " SET id=?,name=?";
        Collection<String> columns = new LinkedList<>();
        columns.add("id");
        columns.add("name");
        assertEquals(expectedSetColumns, MySQLExpressionBuilder.buildSetColumns(columns));
    }
}
