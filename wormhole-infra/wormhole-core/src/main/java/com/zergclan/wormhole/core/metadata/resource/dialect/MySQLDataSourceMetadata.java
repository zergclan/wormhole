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

package com.zergclan.wormhole.core.metadata.resource.dialect;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.common.util.StringUtil;
import com.zergclan.wormhole.core.metadata.DataSourceMetadata;
import com.zergclan.wormhole.core.metadata.resource.DatabaseType;
import com.zergclan.wormhole.core.metadata.resource.SchemaMetadata;
import com.zergclan.wormhole.core.metadata.resource.TableMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * Data source metadata of MySQL.
 */
@RequiredArgsConstructor
public final class MySQLDataSourceMetadata implements DataSourceMetadata {
    
    private static final DatabaseType TYPE = DatabaseType.MYSQL;
    
    private final String host;
    
    private final int port;
    
    @Getter
    private final String username;
    
    @Getter
    private final String password;
    
    private final String catalog;
    
    private final Properties parameters;
    
    @Getter
    private final Map<String, SchemaMetadata> schemas = new LinkedHashMap<>();
    
    @Override
    public String getDriverClassName() {
        return TYPE.getDefaultDriverClassName();
    }
    
    @Override
    public String getJdbcUrl() {
        return TYPE.getProtocol() + host + MarkConstant.COLON + port + MarkConstant.FORWARD_SLASH + catalog + parseParameter(parameters);
    }
    
    private String parseParameter(final Properties parameterProperties) {
        StringJoiner stringJoiner = new StringJoiner(MarkConstant.AND);
        for (Map.Entry<Object, Object> entry : parameterProperties.entrySet()) {
            stringJoiner.add(entry.getKey() + MarkConstant.EQUAL + entry.getValue());
        }
        String result = stringJoiner.toString();
        return StringUtil.isBlank(result) ? "" : MarkConstant.QUESTION + result;
    }
    
    @Override
    public boolean registerSchema(final SchemaMetadata schemaMetaData) {
        schemas.put(schemaMetaData.getIdentifier(), schemaMetaData);
        return true;
    }
    
    @Override
    public SchemaMetadata getSchema(final String name) {
        return schemas.get(name);
    }

    @Override
    public TableMetadata getTable(final String name) {
        String[] split = name.split(MarkConstant.POINT);
        return getSchema(split[0]).getTable(split[1]);
    }

    @Override
    public String getIdentifier() {
        return TYPE.getName() + MarkConstant.SPACE + host + MarkConstant.COLON + port + MarkConstant.COLON + catalog + MarkConstant.SPACE + username + MarkConstant.AT + password;
    }
}
