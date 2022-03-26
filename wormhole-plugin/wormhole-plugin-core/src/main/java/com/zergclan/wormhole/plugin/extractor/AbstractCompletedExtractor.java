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

package com.zergclan.wormhole.plugin.extractor;

import com.zergclan.wormhole.common.util.StringUtil;
import com.zergclan.wormhole.data.core.DataGroup;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.catched.CachedSourceMetaData;
import com.zergclan.wormhole.metadata.core.node.DataNodeMetaData;
import com.zergclan.wormhole.plugin.api.Extractor;

import java.util.Collection;
import java.util.Map;

/**
 * Abstract completed extractor.
 */
public abstract class AbstractCompletedExtractor implements Extractor<DataGroup> {

    @Override
    public Collection<DataGroup> extract(final CachedSourceMetaData cachedSource) {
        String extractSQl = getExtractSQl(cachedSource);
        return doExtract(cachedSource.getDataSource(), cachedSource.getDataNodes(), extractSQl);
    }

    private String getExtractSQl(final CachedSourceMetaData cachedSource) {
        String result = cachedSource.getActualSql();
        if (StringUtil.isBlank(result)) {
            return generatorExtractSQl(cachedSource.getTable(), cachedSource.getConditionSql(), cachedSource.getDataNodes());
        }
        return result;
    }

    /**
     * Generator extract SQl.
     *
     * @param table table
     * @param conditionSql condition sql
     * @param dataNodes data nodes
     * @return extract SQl
     */
    protected abstract String generatorExtractSQl(String table, String conditionSql, Map<String, DataNodeMetaData> dataNodes);

    /**
     * Do extract {@link DataGroup}.
     *
     * @param dataSource data source
     * @param dataNodes data nodes
     * @param extractSQl extract SQl
     * @return {@link DataGroup}
     */
    protected abstract Collection<DataGroup> doExtract(DataSourceMetaData dataSource, Map<String, DataNodeMetaData> dataNodes, String extractSQl);
}
