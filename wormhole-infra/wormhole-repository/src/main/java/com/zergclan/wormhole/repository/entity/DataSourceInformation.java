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

package com.zergclan.wormhole.repository.entity;

import com.zergclan.wormhole.repository.DbTypeEnum;
import lombok.Data;

/**
 * Information about connecting to the data source.
 */
@Data
public final class DataSourceInformation {

    private String id;

    private String dbName;

    private String dbUser;

    private String dbPassword;

    private String dbType;

    private String jdbcUrl;

    /**
     * Get database driver className.
     * @return Driver className.
     */
    public String getDriverClassname() {
        return DbTypeEnum.getDbType(this.dbType).getDriverClassName();
    }

    /**
     * Get database testSql.
     * @return TestSql.
     */
    public String getTestSql() {
        return DbTypeEnum.getDbType(this.dbType).getTestSql();
    }
}
