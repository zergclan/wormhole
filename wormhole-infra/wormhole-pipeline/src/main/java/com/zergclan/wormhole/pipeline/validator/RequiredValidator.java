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

package com.zergclan.wormhole.pipeline.validator;

import com.zergclan.wormhole.common.data.node.WormholeDataNode;
import com.zergclan.wormhole.common.exception.WormholeException;

/**
 * Required data node validator.
 */
public final class RequiredValidator implements WormholeValidator<WormholeDataNode<?>> {
    
    @Override
    public void validate(final WormholeDataNode<?> dataNode) {
        Object value = dataNode.getValue();
        if (null == value) {
            throw new WormholeException("Required value can not be null");
        }
    }
}
