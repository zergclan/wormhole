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

package com.zergclan.wormhole.metadata.core.loader;

import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.resource.dialect.H2DataSourceMetaData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class DataSourceManagerTest {

    private DataSourceMetaData dataSourceMetaData;

    @BeforeAll
    public void init() throws SQLException {
        dataSourceMetaData = new H2DataSourceMetaData("org.h2.Driver",
                "jdbc:h2:file:~/.h2/test;AUTO_SERVER=TRUE;MODE=mysql",
                "root", "root");
    }

    @Test
    public void assertRegister() {
        DataSourceManager.register(dataSourceMetaData);
        assertNotNull(DataSourceManager.get(dataSourceMetaData));
    }

    @Test
    public void assertRefresh() {
        DataSourceManager.refresh(dataSourceMetaData);
        assertNotNull(DataSourceManager.get(dataSourceMetaData));
    }
}
