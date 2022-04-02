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

package com.zergclan.wormhole.plugin.mysql.loader;

import com.zergclan.wormhole.data.core.BatchedDataGroup;
import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.data.core.result.BatchedLoadResult;
import com.zergclan.wormhole.data.core.result.MysqlLoadResult;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.catched.CachedTargetMetaData;
import com.zergclan.wormhole.plugin.loader.AbstractBatchedLoader;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.*;

/**
 * Batched loader of MySQL.
 */
public final class MySQLBatchedLoader extends AbstractBatchedLoader {

    @Override
    protected BatchedLoadResult standardLoad(final BatchedDataGroup data, final CachedTargetMetaData cachedTarget) {

        MysqlLoadResult mysqlLoadResult = new MysqlLoadResult();
        mysqlLoadResult.setDataNum(data.getDataGroups().size());

        //1.new JdbcTemplate
        DataSourceMetaData dataSourceMetaData = cachedTarget.getDataSource();
        JdbcTemplate jdbcTemplate = JdbcTemplateCreator.create(dataSourceMetaData);

        //2.generate sql
        SqlGenerator sqlGenerator = new CachedTargetMetaDataSqlGenerator(cachedTarget);

        //3.compare date
        SqlHelper sqlHelper = new SqlHelper(jdbcTemplate, sqlGenerator);
        List<Map<String, String>> targetData = null;
        try {
            targetData = sqlHelper.executeSelect(data.getDataGroups());
        } catch (SQLException e) {
            String errInfo = e.getErrorCode() + e.getSQLState();
            Map<String, Collection<DataGroup>> errMap = new HashMap<>(8);
            errMap.put(errInfo, data.getDataGroups());
        }
        Map<String, Collection<DataGroup>> incrementalData = compareData(data.getDataGroups(),
                targetData, cachedTarget.getUniqueNodes(), cachedTarget.getCompareNodes());

        //4.handle Incremental data  insert or update
        Collection<DataGroup> addData = incrementalData.get("add");
        Collection<DataGroup> modifyData = incrementalData.get("modify");
        if (addData.size() > 0) {
            try {
                sqlHelper.executeBatchInsert(addData);
            } catch (SQLException e) {
                String errInfo = e.getErrorCode() + e.getSQLState();
                Map<String, Collection<DataGroup>> errMap = new HashMap<>(8);
                errMap.put(errInfo, addData);
            }
        }
        if (modifyData.size() > 0) {
            for (DataGroup dataGroup : modifyData) {
                try {
                    sqlHelper.executeUpdate(dataGroup);
                } catch (SQLException e) {
                    String errInfo = e.getErrorCode() + e.getSQLState();
                    Map<String, Collection<DataGroup>> errMap = new HashMap<>(8);
                    Collection<DataGroup> collection = new ArrayList<>();
                    collection.add(dataGroup);
                    errMap.put(errInfo, collection);
                }
            }

        }

        //5.return
        return new BatchedLoadResult(true, mysqlLoadResult);
    }

    @Override
    protected BatchedLoadResult transactionLoad(final BatchedDataGroup data, final CachedTargetMetaData cachedTarget) {
        return null;
    }
    
    @Override
    public String getType() {
        return "MySQL";
    }

    private Map<String, Collection<DataGroup>> compareData(final Collection sourceData,
                                                  final Collection targetData,
                                                  final Collection<String> uniqueNodes,
                                                  final Collection<String> compareNodes) {
        Map<String, Collection<DataGroup>> incrementMap = new HashMap<>(8);
        Collection<DataGroup> addData = new LinkedList<>();
        Collection<DataGroup> modifyData = new LinkedList<>();
        //TODO Calculate incremental data and add batchNo
        incrementMap.put("add", addData);
        incrementMap.put("modify", modifyData);
        return incrementMap;
    }
}
