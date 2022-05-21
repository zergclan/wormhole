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

package com.zergclan.wormhole.metadata.core.resource.dialect;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.metadata.api.DataSourceMetaData;
import com.zergclan.wormhole.metadata.core.resource.DatabaseType;
import com.zergclan.wormhole.metadata.core.resource.SchemaMetaData;
import com.zergclan.wormhole.metadata.core.resource.TableMetaData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Data source metadata of H2.
 */
@RequiredArgsConstructor
public final class H2DataSourceMetaData implements DataSourceMetaData {

    private static final DatabaseType TYPE = DatabaseType.H2;

    private final String driverClassName;

    private final String jdbcUrl;

    @Getter
    private final String username;

    @Getter
    private final String password;

    @Getter
    private final Map<String, SchemaMetaData> schemas = new LinkedHashMap<>();
    
    @Override
    public String getDataSourceType() {
        return TYPE.getName();
    }
    
    @Override
    public String getDriverClassName() {
        return driverClassName;
    }
    
    @Override
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    @Override
    public boolean registerSchema(final SchemaMetaData schemaMetaData) {
        schemas.put(schemaMetaData.getIdentifier(), schemaMetaData);
        return true;
    }
    
    @Override
    public SchemaMetaData getSchema(final String name) {
        return schemas.get(name);
    }

    @Override
    public TableMetaData getTable(final String name) {
        String[] split = name.split(MarkConstant.POINT);
        return getSchema(split[0]).getTable(split[1]);
    }

    @Override
    public String getIdentifier() {
        return TYPE.getName() + MarkConstant.SPACE + jdbcUrl + MarkConstant.SPACE + username + MarkConstant.AT + password;
    }
}
