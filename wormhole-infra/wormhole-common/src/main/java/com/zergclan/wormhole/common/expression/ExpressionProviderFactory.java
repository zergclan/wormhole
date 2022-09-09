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
 * Expression provider factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpressionProviderFactory {
    
    static {
        WormholeServiceLoader.register(ExpressionProvider.class);
    }
    
    /**
     * Get {@link ExpressionProvider}.
     *
     * @param cachedMetaData {@link CachedMetaData}
     * @return {@link ExpressionProvider}
     */
    public static ExpressionProvider getInstance(final CachedMetaData cachedMetaData) {
        ExpressionProvider result;
        if (cachedMetaData instanceof CachedSourceMetaData) {
            result = initExecuteExpressionProvider((CachedSourceMetaData) cachedMetaData);
        } else if (cachedMetaData instanceof CachedTargetMetaData) {
            result = initExecuteExpressionProvider((CachedTargetMetaData) cachedMetaData);
        } else {
            throw new UnsupportedOperationException();
        }
        result.init(cachedMetaData);
        return result;
    }
    
    private static ExpressionProvider initExecuteExpressionProvider(final CachedSourceMetaData cachedSourceMetaData) {
        String type = "extract" + MarkConstant.SPACE + cachedSourceMetaData.getDatabaseType().getType();
        return TypedSPIRegistry.getRegisteredService(ExpressionProvider.class, type);
    }
    
    private static ExpressionProvider initExecuteExpressionProvider(final CachedTargetMetaData cachedTargetMetaData) {
        String type = "load" + MarkConstant.SPACE + cachedTargetMetaData.getDatabaseType().getType();
        return TypedSPIRegistry.getRegisteredService(ExpressionProvider.class, type);
    }
}
