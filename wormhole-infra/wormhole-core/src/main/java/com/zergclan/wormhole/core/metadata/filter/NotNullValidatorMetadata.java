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

package com.zergclan.wormhole.core.metadata.filter;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.common.util.Validator;

import java.util.Properties;

/**
 * Not null validator implemented of {@link FilterMetadata}.
 */
public final class NotNullValidatorMetadata implements FilterMetadata {

    private final String taskIdentifier;

    private final int order;

    // FIXME refactoring by enum
    private final String type;

    private final String name;

    // FIXME refactoring using builder pattern
    public NotNullValidatorMetadata(final String taskIdentifier, final int order, final String type, final Properties props) {
        this.taskIdentifier = taskIdentifier;
        this.order = order;
        this.type = type;
        String name = props.getProperty("name");
        Validator.notNull(name, "error : not null validator sourceName can not be null, task identifier: [%s]", taskIdentifier);
        this.name = name;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getIdentifier() {
        return taskIdentifier + MarkConstant.SPACE + type + MarkConstant.SPACE + order;
    }
}
