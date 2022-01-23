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

package com.zergclan.wormhole.writer.xsql;

import com.zergclan.wormhole.core.metadata.resource.IndexMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * generate sql statement
 */
public class SqlGenerator {

    public static String createSelectSql(final TableMetadata tableMetadata) {
        Map<String, IndexMetadata> indexes = tableMetadata.getIndexes();
        Iterator<Map.Entry<String, IndexMetadata>> it = indexes.entrySet().iterator();
        IndexMetadata indexMetadata = it.next().getValue();
        Collection<String> columnNames = indexMetadata.getColumnNames();
        String selectStr = " select count(1) from " + tableMetadata.getName() + " where 1=1 ";
        for(int i=0; i<columnNames.size(); i++) {
            String column = (String)((List)columnNames).get(i);
            selectStr += " and " + column + " = ? ";
        }
        return selectStr;
    }

    public static String createBatchInsertSql(final TableMetadata tableMetadata) {
        String batchInsertSql = "";
        return batchInsertSql;
    }

    public static String createBatchUpdateSql(final TableMetadata tableMetadata) {
        String batchUpdateSql = "";
        return batchUpdateSql;
    }
}
