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

package com.zergclan.wormhole.common.expression;

import com.zergclan.wormhole.common.metadata.catched.CachedMetaData;
import com.zergclan.wormhole.common.metadata.catched.CachedSourceMetaData;
import com.zergclan.wormhole.common.metadata.catched.CachedTargetMetaData;
import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.tool.spi.WormholeServiceLoader;
import com.zergclan.wormhole.tool.spi.scene.typed.TypedSPIRegistry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Expression builder factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExpressionBuilderFactory {
    
    static {
        WormholeServiceLoader.register(ExpressionBuilder.class);
    }
    
    /**
     * Get {@link ExpressionBuilder}.
     *
     * @param cachedMetaData {@link CachedMetaData}
     * @return {@link ExpressionBuilder}
     */
    public static ExpressionBuilder getInstance(final CachedMetaData cachedMetaData) {
        if (cachedMetaData instanceof CachedSourceMetaData) {
            return getSourceExpressionBuilder((CachedSourceMetaData) cachedMetaData);
        } else if (cachedMetaData instanceof CachedTargetMetaData) {
            return getTargetExpressionBuilder((CachedTargetMetaData) cachedMetaData);
        } else {
            throw new UnsupportedOperationException();
        }
    }
    
    private static ExpressionBuilder getSourceExpressionBuilder(final CachedSourceMetaData cachedSourceMetaData) {
        String dataSourceType = cachedSourceMetaData.getDataSource().getDataSourceType().getType();
        ExpressionBuilder result = TypedSPIRegistry.getRegisteredService(ExpressionBuilder.class, dataSourceType + MarkConstant.AT + "source");
        result.init(cachedSourceMetaData);
        return result;
    }
    
    private static ExpressionBuilder getTargetExpressionBuilder(final CachedTargetMetaData cachedTargetMetaData) {
        String dataSourceType = cachedTargetMetaData.getDataSource().getDataSourceType().getType();
        ExpressionBuilder result = TypedSPIRegistry.getRegisteredService(ExpressionBuilder.class, dataSourceType + MarkConstant.AT + "target");
        result.init(cachedTargetMetaData);
        return result;
    }
}
