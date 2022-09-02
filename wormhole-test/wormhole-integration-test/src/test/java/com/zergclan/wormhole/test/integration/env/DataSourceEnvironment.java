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

package com.zergclan.wormhole.test.integration.env;

import com.zergclan.wormhole.common.metadata.database.DatabaseType;
import com.zergclan.wormhole.common.metadata.datasource.DataSourceTypeFactory;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.tool.util.StringUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Environment of data source.
 */
@RequiredArgsConstructor
@Getter
public final class DataSourceEnvironment {
    
    private final DatabaseType databaseType;
    
    private final int port;
    
    public DataSourceEnvironment(final String datasource) {
        if (datasource.contains(MarkConstant.COLON)) {
            String[] typeAndPort = StringUtil.twoPartsSplit(datasource, MarkConstant.COLON);
            databaseType = DataSourceTypeFactory.getInstance(typeAndPort[0]);
            port = Integer.parseInt(typeAndPort[1]);
        } else {
            databaseType = DataSourceTypeFactory.getInstance(datasource);
            port = databaseType.getDefaultPort();
        }
    }
}
