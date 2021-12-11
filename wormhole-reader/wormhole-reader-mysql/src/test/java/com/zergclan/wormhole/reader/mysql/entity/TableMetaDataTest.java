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

package com.zergclan.wormhole.reader.mysql.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test tableMetaData class constant values.
 */
public final class TableMetaDataTest {

    @Test
    public void assertValues() {
        assertEquals("tableName", TableMetaData.TABLE_NAME);
        assertEquals("tableSchema", TableMetaData.TABLE_SCHEMA);
        assertEquals("tableComment", TableMetaData.TABLE_COMMENT);
        assertEquals("columns", TableMetaData.COLUMNS);
        assertEquals("indexes", TableMetaData.INDEXES);
    }

}
