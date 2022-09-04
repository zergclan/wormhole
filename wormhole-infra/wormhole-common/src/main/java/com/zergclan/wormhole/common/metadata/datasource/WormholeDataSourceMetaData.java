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

package com.zergclan.wormhole.common.metadata.datasource;

import com.zergclan.wormhole.common.WormholeMetaData;
import com.zergclan.wormhole.common.metadata.database.DatabaseType;
import com.zergclan.wormhole.common.metadata.datasource.url.UrlInformation;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.tool.util.StringUtil;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Data source meta data of Wormhole.
 */
@Getter
public final class WormholeDataSourceMetaData implements WormholeMetaData {
    
    private final String identifier;
    
    private final DatabaseType databaseType;
    
    private final UrlInformation urlInformation;
    
    private final String username;
    
    private final String password;
    
    private final DataSourcePoolMetadata pool;
    
    private final Map<String, SchemaMetaData> schemas = new LinkedHashMap<>();
    
    public WormholeDataSourceMetaData(final String identifier, final String databaseType, final String url, final String username, final String password, final DataSourcePoolMetadata pool) {
        this.databaseType = DataSourceTypeFactory.getInstance(databaseType);
        urlInformation = UrlInformation.build(url);
        this.identifier = identifier;
        this.username = username;
        this.password = password;
        this.pool = pool;
    }
    
    /**
     * Get driver class name.
     *
     * @return driver class name
     */
    public String getDriverClassName() {
        return databaseType.getDriverClassName();
    }
    
    /**
     * Get jdbc url.
     *
     * @return jdbc url
     */
    public String getJdbcUrl() {
        return urlInformation.getUrl();
    }
    
    /**
     * Register {@link SchemaMetaData}.
     *
     * @param schemaMetaData {@link SchemaMetaData}
     * @return is registered or not
     */
    public boolean registerSchema(final SchemaMetaData schemaMetaData) {
        if (schemas.containsKey(schemaMetaData.getName())) {
            return false;
        }
        schemas.put(schemaMetaData.getName(), schemaMetaData);
        return true;
    }
    
    /**
     * Get {@link TableMetaData}.
     *
     * @param name name
     * @return {@link TableMetaData}
     */
    public TableMetaData getTable(final String name) {
        if (name.contains(MarkConstant.POINT)) {
            String[] split = StringUtil.twoPartsSplit(name, MarkConstant.POINT);
            return getTable(split[0], split[1]);
        }
        return getTable(urlInformation.getCatalog(), name);
    }
    
    private TableMetaData getTable(final String schemaName, final String tableName) {
        return schemas.get(schemaName).getTable(tableName);
    }
    
    /**
     * Get related schema names.
     *
     * @return related schema names
     */
    public Collection<String> getRelatedSchemaNames() {
        return Collections.singletonList(urlInformation.getCatalog());
    }
}
