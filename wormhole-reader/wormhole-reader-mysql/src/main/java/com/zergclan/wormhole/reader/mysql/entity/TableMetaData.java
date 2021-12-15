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

package com.zergclan.wormhole.reader.mysql.entity;

import com.zergclan.wormhole.common.SystemConstant;
import com.zergclan.wormhole.core.metadata.MetaData;
import lombok.Data;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Meta data table.
 */
@Data
public final class TableMetaData implements MetaData {

    public static final String TABLE_NAME = "tableName";

    public static final String TABLE_SCHEMA = "tableSchema";

    public static final String TABLE_COMMENT = "tableComment";

    public static final String COLUMNS = "columns";

    public static final String INDEXES = "indexes";

    private String tableName;

    private String tableSchema;

    private String tableComment;

    private Map<String, ColumnMetaData> columns = new LinkedHashMap<>();

    private Map<String, IndexMetaData> indexes = new LinkedHashMap<>();

    @Override
    public String getIdentifier() {
        return tableSchema + SystemConstant.IDENTIFIER_SPACE + tableName;
    }
}
