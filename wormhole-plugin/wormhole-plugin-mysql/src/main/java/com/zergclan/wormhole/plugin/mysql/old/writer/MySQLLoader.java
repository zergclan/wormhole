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

package com.zergclan.wormhole.plugin.mysql.old.writer;

import com.zergclan.wormhole.data.api.DataGroup;
import com.zergclan.wormhole.metadata.core.task.TargetMetaData;
import com.zergclan.wormhole.plugin.api.Loader;
import lombok.Setter;

import javax.sql.DataSource;

/**
 * Loader for MySQL.
 */
@Setter
public final class MySQLLoader implements Loader<DataGroup> {

    private final LoadHandler loadHandler;

    public MySQLLoader(final DataSource dataSource) {
        this.loadHandler = new LoadHandler(dataSource);
    }
    
    @Override
    public void load(final DataGroup dataGroup, final TargetMetaData target) {
        // FIXME load implemented
    }
    
//    private void loaderData() {
//        JdbcLoadContent jdbcLoadContent = (JdbcLoadContent) loadContent;
//        Collection<Map<String, Object>> listData = jdbcLoadContent.getLoadData();
//        Map<String, TableMetadata> tables = jdbcLoadContent.getTables();
//        for (Map.Entry<String, TableMetadata> entry : tables.entrySet()) {
//            String table = entry.getKey();
//            TableMetadata tableMetadata = entry.getValue();
//            System.out.println("load data into " + table + " start...");
//            List<Map<String, Object>> insertData = new LinkedList<>();
//            List<Map<String, Object>> updateData = new LinkedList<>();
//            for (Map<String, Object> mapData : listData) {
//                int count = executeSelect(tableMetadata, mapData);
//                if (count > 0) {
//                    updateData.add(mapData);
//                } else {
//                    insertData.add(mapData);
//                }
//            }
//            if (insertData.size() > 0) {
//                executeBatchInsert(tableMetadata, insertData);
//            }
//            if (updateData.size() > 0) {
//                executeBatchUpdate(tableMetadata, updateData);
//            }
//            System.out.println("load data into " + table + " end...");
//        }
//    }
//
//    private int executeSelect(final TableMetadata tableMetadata, final Map<String, Object> mapData) {
//        return loadHandler.executeSelect(tableMetadata, mapData);
//    }
//
//    private void executeBatchInsert(final TableMetadata tableMetadata, final List<Map<String, Object>> insertData) {
//        loadHandler.executeBatchInsert(tableMetadata, insertData);
//    }
//
//    private void executeBatchUpdate(final TableMetadata tableMetadata, final List<Map<String, Object>> updateData) {
//        loadHandler.executeBatchUpdate(tableMetadata, updateData);
//    }
}
