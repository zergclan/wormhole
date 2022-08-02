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

package com.zergclan.wormhole.metadata.api;

import com.zergclan.wormhole.metadata.core.datasource.DataSourceType;
import com.zergclan.wormhole.metadata.core.datasource.SchemaMetaData;
import com.zergclan.wormhole.metadata.core.datasource.TableMetaData;

import java.util.Collection;
import java.util.Collections;

/**
 * The root interface from which all data source metadata objects shall be derived in Wormhole.
 */
public interface DataSourceMetaData extends MetaData {
    
    /**
     * Get data source type.
     *
     * @return data source type
     */
    DataSourceType getDataSourceType();
    
    /**
     * Get driver class name.
     *
     * @return driver class name
     */
    String getDriverClassName();

    /**
     * Get jdbc url.
     *
     * @return jdbc url
     */
    String getJdbcUrl();

    /**
     * Get username.
     *
     * @return username
     */
    String getUsername();

    /**
     * Get password.
     *
     * @return password
     */
    String getPassword();

    /**
     * Register {@link SchemaMetaData}.
     *
     * @param schemaMetaData {@link SchemaMetaData}
     * @return is registered or not
     */
    boolean registerSchema(SchemaMetaData schemaMetaData);
    
    /**
     * Get {@link TableMetaData}.
     *
     * @param name name
     * @return {@link TableMetaData}
     */
    TableMetaData getTable(String name);
    
    /**
     * Get related schema names.
     *
     * @return related schema names
     */
    default Collection<String> getRelatedSchemaNames() {
        return Collections.emptyList();
    }
}
