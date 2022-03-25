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

import com.zergclan.wormhole.data.api.BatchedDataGroup;
import com.zergclan.wormhole.data.core.result.BatchedLoadResult;
import com.zergclan.wormhole.data.core.result.MysqlLoadResult;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.catched.CachedTargetMetaData;
import com.zergclan.wormhole.plugin.loader.AbstractBatchedLoader;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;
import java.util.Map;
import java.util.List;

/**
 * Batched loader of MySQL.
 */
public final class MySQLBatchedLoader extends AbstractBatchedLoader {

    @Override
    protected BatchedLoadResult standardLoad(final BatchedDataGroup data, final CachedTargetMetaData cachedTarget) {
        //1.new JdbcTemplate
        DataSourceMetaData dataSourceMetaData = cachedTarget.getSource();
        JdbcTemplate jdbcTemplate = JdbcTemplateCreator.create(dataSourceMetaData);
        //2.generate sql
        SqlGenerator sqlGenerator = new CachedTargetMetaDataSqlGenerator(cachedTarget);
        //3.compare date
        SqlHelper sqlHelper = new SqlHelper(jdbcTemplate, sqlGenerator);
        List<Map<String, String>> targetData = sqlHelper.executeSelect(data.getAllDataGroups());
        Map<String, List<Map<String, String>>> incrementalData = compareData(data.getAllDataGroups(),
                targetData, cachedTarget.getUniqueNodes(), cachedTarget.getCompareNodes());
        //4.handle Incremental data  insert or update
        sqlHelper.executeBatchInsert(incrementalData.get("add"));
        incrementalData.get("update").forEach(map -> {
            sqlHelper.executeUpdate(map);
        });
        //5.return
        MysqlLoadResult mysqlLoadResult = new MysqlLoadResult();
        mysqlLoadResult.setLoadFlag(Boolean.TRUE);
        mysqlLoadResult.setDataNum(data.getAllDataGroups().size());
        mysqlLoadResult.setAddNum(incrementalData.get("add").size());
        mysqlLoadResult.setUpdateNum(incrementalData.get("update").size());
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

    private Map<String, List<Map<String, String>>> compareData(final Collection sourceData,
                                                  final Collection targetData,
                                                  final Collection<String> uniqueNodes,
                                                  final Collection<String> compareNodes) {
        //TODO Calculate incremental data and add batchNo
        return null;
    }
}
