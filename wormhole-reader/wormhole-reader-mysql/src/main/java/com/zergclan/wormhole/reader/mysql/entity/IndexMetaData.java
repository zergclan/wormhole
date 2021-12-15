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

/**
 * Meta data index.
 */
@Data
public final class IndexMetaData implements MetaData {

    public static final String TABLE_SCHEMA = "tableSchema";

    public static final String TABLE_NAME = "Table";

    public static final String NON_UNIQUE = "Non_unique";

    public static final String KEY_NAME = "Key_name";

    public static final String COLUMN_NAME = "Column_name";

    public static final String SEQ_IN_INDEX = "Seq_in_index";

    private String tableSchema;

    private String tableName;

    private Boolean isUnique;

    private String keyName;

    private String columnName;

    private int seqInIndex;

    @Override
    public String getIdentifier() {
        return tableSchema + SystemConstant.IDENTIFIER_SPACE + tableName + SystemConstant.IDENTIFIER_SPACE + keyName;
    }
}
