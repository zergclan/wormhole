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

package com.zergclan.wormhole.reader.mysql.domain;

import com.zergclan.wormhole.reader.mysql.entity.ColumnMetaData;
import com.zergclan.wormhole.reader.mysql.entity.IndexMetaData;
import com.zergclan.wormhole.reader.mysql.entity.TableMetaData;
import java.util.List;

/**
 * Get metadata interface.
 */
public interface MetadataDOM {

    /**
     * Query all tables metadata.
     * @param dataSourceId Registered data source id.
     * @param tableSchema The database to be queried.
     * @return Meta information for all tables.
     */
    List<TableMetaData> queryAllTables(String dataSourceId, String tableSchema);

    /**
     * Query all columns metadata.
     * @param dataSourceId Registered data source id.
     * @param tableSchema The database to be queried.
     * @param tableName The tableName to be queried.
     * @return Meta information for all columns.
     */
    List<ColumnMetaData> queryAllColumns(String dataSourceId, String tableSchema, String tableName);

    /**
     * Query unique index by table.
     * @param dataSourceId Registered data source id.
     * @param tableSchema The database to be queried.
     * @param tableName The tableName to be queried.
     * @return Meta information for table indexes.
     */
    List<IndexMetaData> queryUniqueIndexByTable(String dataSourceId, String tableSchema, String tableName);

}