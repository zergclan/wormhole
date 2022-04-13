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

package com.zergclan.wormhole.plugin.mysql.builder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MySQLExpressionBuilderTest {
    
    private static MySQLExpressionBuilder builder;
    
    @BeforeAll
    public static void init() {
        builder = new MySQLExpressionBuilder();
    }
    
    @Test
    public void assertWrapColumn() {
        String expectedColumn = "`name`";
        assertEquals(expectedColumn, builder.wrapColumn("name"));
    }
    
    @Test
    public void assertBuildSelectColumn() {
        String expectedSelectColumn = "t_user.`name` AS `name`";
        assertEquals(expectedSelectColumn, builder.buildSelectColumn("t_user", "name"));
    }
    
    @Test
    public void assertBuildSelectColumns() {
        String expectedSelectColumns = "SELECT t_user.`id` AS `id`,t_user.`name` AS `name`";
        Collection<String> columns = new LinkedList<>();
        columns.add("id");
        columns.add("name");
        assertEquals(expectedSelectColumns, builder.buildSelectColumns("t_user", columns));
    }
    
    @Test
    public void assertBuildFromTable() {
        String expectedFromTable = " FROM t_user";
        assertEquals(expectedFromTable, builder.buildFromTable("t_user"));
    }
    
    @Test
    public void assertBuildWhere() {
        String expectedWhere = " WHERE id = 1";
        assertEquals(expectedWhere, builder.buildWhere("id = 1"));
    }
}
