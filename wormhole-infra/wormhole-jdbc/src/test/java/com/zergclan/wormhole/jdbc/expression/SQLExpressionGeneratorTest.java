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

package com.zergclan.wormhole.jdbc.expression;

import com.zergclan.wormhole.common.metadata.datasource.DataSourceTypeFactory;
import com.zergclan.wormhole.tool.util.Collections;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SQLExpressionGeneratorTest {
    
    private static SQLExpressionGenerator generator;
    
    @BeforeAll
    public static void init() {
        String table = "t_user";
        Set<String> nodeNames = Collections.newLinkedHashSet(new String[] {"id", "username", "password", "create_time"});
        Set<String> uniqueNodeNames = Collections.newLinkedHashSet(new String[] {"username", "password"});
        String conditionSql = "id=1";
        generator = SQLExpressionGenerator.build(DataSourceTypeFactory.getInstance("MySQL"), table, nodeNames, conditionSql, uniqueNodeNames);
    }
    
    @Test
    public void assertGenerateInsertTable() {
        assertEquals("INSERT INTO t_user", generator.generateInsertTable());
    }
    
    @Test
    public void assertGenerateInsertColumns() {
        assertEquals("(`id`,`username`,`password`,`create_time`)", generator.generateInsertColumns());
    }
    
    @Test
    public void assertGenerateInsertValues() {
        assertEquals(" VALUES (?,?,?,?)", generator.generateInsertValues());
    }
    
    @Test
    public void assertGenerateUpdateTable() {
        assertEquals("UPDATE t_user", generator.generateUpdateTable());
    }
    
    @Test
    public void assertGenerateUpdateColumns() {
        assertEquals(" SET `id`=?,`username`=?,`password`=?,`create_time`=?", generator.generateUpdateColumns());
    }
    
    @Test
    public void assertGenerateSelectColumns() {
        assertEquals("SELECT t_user.`id` AS `id`,t_user.`username` AS `username`,t_user.`password` AS `password`,t_user.`create_time` AS `create_time`", generator.generateSelectColumns());
    }
    
    @Test
    public void assertGenerateFromTable() {
        assertEquals(" FROM t_user", generator.generateFromTable());
    }
    
    @Test
    public void assertGenerateWhereByConditionSql() {
        assertEquals(" WHERE id=1", generator.generateWhereByConditionSql());
    }
    
    @Test
    public void assertGenerateWhereByAllEquals() {
        assertEquals(" WHERE t_user.`username`=? AND t_user.`password`=?", generator.generateWhereByAllEquals());
    }
}
