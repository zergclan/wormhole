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

package com.zergclan.wormhole.reader.mysql;

import com.zergclan.wormhole.core.metadata.ColumnMetaData;
import com.zergclan.wormhole.core.metadata.IndexMetaData;
import com.zergclan.wormhole.core.metadata.TableMetaData;
import com.zergclan.wormhole.extracter.Extractor;

import java.util.Collection;
import java.util.Map;

/**
 * Extractor for MySQL.
 */
public final class MySQLExtractor implements Extractor {
    
    @Override
    public Collection<TableMetaData> extractTables() {
        // TODO
        return null;
    }
    
    @Override
    public Collection<ColumnMetaData> extractColumns(final TableMetaData table) {
        // TODO
        return null;
    }
    
    @Override
    public Collection<IndexMetaData> extractIndexes(final TableMetaData table) {
        // TODO
        return null;
    }
    
    @Override
    public Collection<Map<String, Object>> extractDatum(final Map<String, ColumnMetaData> columns) {
        // TODO
        return null;
    }
}
