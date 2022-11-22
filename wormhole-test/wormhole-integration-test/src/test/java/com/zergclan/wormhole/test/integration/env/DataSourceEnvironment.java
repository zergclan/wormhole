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

import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.tool.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * Environment of data source.
 */
@Getter
@Setter
public final class DataSourceEnvironment {
    
    private String databaseType;
    
    private String dockerImageName;
    
    private AssertPart assertPart;
    
    /**
     * Build.
     *
     * @param dataSource data source
     * @param assertPart assert part
     * @return {@link DataSourceEnvironment}
     */
    public static DataSourceEnvironment build(final String dataSource, final AssertPart assertPart) {
        DataSourceEnvironment result = new DataSourceEnvironment();
        if (dataSource.contains(MarkConstant.AT)) {
            String[] typeAndVersion = StringUtil.twoPartsSplit(dataSource, MarkConstant.AT);
            result.setDatabaseType(typeAndVersion[0]);
            result.setDockerImageName(typeAndVersion[1]);
        }
        result.setAssertPart(assertPart);
        return result;
    }
}
