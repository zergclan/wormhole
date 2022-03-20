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

package com.zergclan.wormhole.plugin.loader;

import com.zergclan.wormhole.data.api.BatchedDataGroup;
import com.zergclan.wormhole.data.core.result.BatchedLoadResult;
import com.zergclan.wormhole.metadata.core.catched.CachedTargetMetaData;
import com.zergclan.wormhole.plugin.api.Loader;

/**
 * Abstract loader of {@link BatchedDataGroup}.
 */
public abstract class AbstractBatchedLoader implements Loader<BatchedDataGroup, BatchedLoadResult> {

    @Override
    public BatchedLoadResult load(final BatchedDataGroup data, final CachedTargetMetaData cachedTarget) {
        return cachedTarget.isTransaction() ? transactionLoad(data, cachedTarget) : standardLoad(data, cachedTarget);
    }

    protected abstract BatchedLoadResult standardLoad(BatchedDataGroup data, CachedTargetMetaData cachedTarget);

    protected abstract BatchedLoadResult transactionLoad(BatchedDataGroup data, CachedTargetMetaData cachedTarget);
}
