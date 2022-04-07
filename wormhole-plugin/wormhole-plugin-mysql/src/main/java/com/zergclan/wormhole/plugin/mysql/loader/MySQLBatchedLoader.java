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
import com.zergclan.wormhole.plugin.mysql.util.JdbcTemplateCreator;
import com.zergclan.wormhole.plugin.mysql.xsql.SqlGenerator;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Batched loader of MySQL.
 */
public final class MySQLBatchedLoader extends AbstractBatchedLoader {

    @Override
    protected BatchedLoadResult standardLoad(final BatchedDataGroup data, final CachedTargetMetaData cachedTarget) {

        MysqlLoadResult mysqlLoadResult = new MysqlLoadResult();
        Collection<DataGroup> dataGroups = data.getDataGroups();
        mysqlLoadResult.setDataNum(dataGroups.size());

        //1.new JdbcTemplate
        DataSourceMetaData dataSourceMetaData = cachedTarget.getDataSource();
        JdbcTemplate jdbcTemplate = JdbcTemplateCreator.create(dataSourceMetaData);

        //2.generate sql
        SqlGenerator sqlGenerator = new CachedTargetMetaDataSqlGenerator(cachedTarget);

        //3.compare date and handle data
        SqlHelper sqlHelper = new SqlHelper(jdbcTemplate, sqlGenerator);
        Map<DataGroup, String> errMap = new HashMap<>(8);
        Iterator<DataGroup> iterator = dataGroups.iterator();
        while (iterator.hasNext()) {
            DataGroup dataGroup = iterator.next();
            int successNum = 0;
            int failNum = 0;
            List<Map<String, Object>> targetData = null;
            try {
                targetData = sqlHelper.executeSelect(dataGroup);
                if (null == targetData || targetData.size() == 0) {
                    sqlHelper.executeInsert(dataGroup);
                } else {
                    boolean flag = compareData(dataGroup, targetData.get(0), cachedTarget.getCompareNodes());
                    if (flag) {
                        sqlHelper.executeUpdate(dataGroup);
                    }
                }
                successNum++;
            } catch (SQLException e) {
                System.out.println(e);
                String errInfo = e.getErrorCode() + e.getSQLState();
                errMap.put(dataGroup, errInfo);
                failNum++;
            } finally {
                mysqlLoadResult.setSuccessNum(successNum);
                mysqlLoadResult.setFailNum(failNum);
                mysqlLoadResult.setErrInfo(errMap);
            }
        }

        //4.return
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

    private boolean compareData(final DataGroup sourceData,
                                final Map<String, Object> targetData,
                                final Collection<String> compareNodes) {
        boolean result = false;
        Iterator<String> iterator = compareNodes.iterator();
        while (iterator.hasNext()) {
            String fieldName = iterator.next();
            Object sourceValue = sourceData.getDataNode(fieldName).getValue();
            Object targetValue = targetData.get(fieldName);
            if (null != sourceValue && null != targetValue) {
                if (!sourceValue.toString().equals(targetValue.toString())) {
                    return true;
                }
            } else if (null == sourceValue && null == targetValue) {
                result = false;
            } else {
                return true;
            }
        }
        return result;
    }
}
