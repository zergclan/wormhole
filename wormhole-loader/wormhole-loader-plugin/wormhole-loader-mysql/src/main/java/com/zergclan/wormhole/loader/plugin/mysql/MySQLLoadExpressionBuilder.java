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

package com.zergclan.wormhole.loader.plugin.mysql;

import com.zergclan.wormhole.common.expression.ExpressionBuilder;
import com.zergclan.wormhole.common.metadata.catched.CachedMetaData;

/**
 * Load expression builder of MySQL.
 */
public final class MySQLLoadExpressionBuilder implements ExpressionBuilder {
    
    @Override
    public void init(final CachedMetaData cachedMetaData) {
    }
    
    @Override
    public String buildSelect() {
        return null;
    }
    
    @Override
    public String buildInsert() {
        return null;
    }
    
    @Override
    public String buildUpdate() {
        return null;
    }
    
    @Override
    public String getType() {
        return "MySQL@target";
    }
}
