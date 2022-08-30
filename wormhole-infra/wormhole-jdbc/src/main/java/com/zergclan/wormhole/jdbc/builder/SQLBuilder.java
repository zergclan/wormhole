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

package com.zergclan.wormhole.jdbc.builder;

import com.zergclan.wormhole.common.metadata.datasource.DataSourceType;
import com.zergclan.wormhole.common.metadata.plan.node.DataNodeMetaData;
import com.zergclan.wormhole.tool.constant.SQLKeywordConstant;
import com.zergclan.wormhole.tool.util.StringUtil;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Builder for SQL.
 */
@RequiredArgsConstructor
public final class SQLBuilder {
    
    private final DataSourceType dataSourceType;
    
    private final String table;
    
    private final Map<String, DataNodeMetaData> dataNodes;
    
    /**
     * Build for select SQL.
     *
     * @return select SQL
     */
    public String buildSelectSQL() {
        // TODO build SQL for select
        return "";
    }
    
    /**
     * Build for select SQL.
     *
     * @param conditionSQL condition SQL
     * @return select SQL
     */
    public String buildSelectSQL(final String conditionSQL) {
        return buildSelectSQL() + buildConditionSQL(conditionSQL);
    }
    
    private String buildConditionSQL(final String conditionSQL) {
        return StringUtil.isBlank(conditionSQL) ? "" : SQLKeywordConstant.WHERE + conditionSQL;
    }
    
    /**
     * Build for insert SQL.
     *
     * @return insert SQL
     */
    public String buildInsertSQL() {
        // TODO build SQL for insert
        return "";
    }
    
    /**
     * Build for update SQL.
     *
     * @return update SQL
     */
    public String buildUpdateSQL() {
        // TODO build SQL for update
        return "";
    }
}
