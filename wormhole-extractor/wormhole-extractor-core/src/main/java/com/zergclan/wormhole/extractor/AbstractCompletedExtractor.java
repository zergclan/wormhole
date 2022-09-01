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

package com.zergclan.wormhole.extractor;

import com.zergclan.wormhole.common.data.node.DataGroup;
import com.zergclan.wormhole.common.expression.ExpressionBuilder;
import com.zergclan.wormhole.common.expression.ExpressionBuilderFactory;
import com.zergclan.wormhole.common.metadata.catched.CachedSourceMetaData;
import com.zergclan.wormhole.common.metadata.datasource.DataSourceMetaData;
import com.zergclan.wormhole.common.metadata.plan.node.DataNodeMetaData;
import com.zergclan.wormhole.tool.util.StringUtil;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

/**
 * Abstract completed extractor.
 */
public abstract class AbstractCompletedExtractor implements WormholeExtractor<DataGroup> {
    
    private ExpressionBuilder expressionBuilder;
    
    private CachedSourceMetaData cachedSource;
    
    @Override
    public void init(final CachedSourceMetaData cachedSource) {
        expressionBuilder = ExpressionBuilderFactory.getInstance(cachedSource);
        this.cachedSource = cachedSource;
    }
    
    @Override
    public Collection<DataGroup> extract() throws SQLException {
        return doExtract(cachedSource.getDataSource(), cachedSource.getDataNodes(), getExtractSQl(cachedSource));
    }
    
    private String getExtractSQl(final CachedSourceMetaData cachedSource) {
        String result = cachedSource.getActualSql();
        if (StringUtil.isBlank(result)) {
            return expressionBuilder.buildSelect();
        }
        return result;
    }
    
    /**
     * Do extract {@link DataGroup}.
     *
     * @param dataSource data source
     * @param dataNodes data nodes
     * @param extractSQl extract SQl
     * @return {@link DataGroup}
     */
    protected abstract Collection<DataGroup> doExtract(DataSourceMetaData dataSource, Map<String, DataNodeMetaData> dataNodes, String extractSQl) throws SQLException;
}
