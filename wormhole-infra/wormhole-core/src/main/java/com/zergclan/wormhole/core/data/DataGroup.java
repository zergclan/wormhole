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

package com.zergclan.wormhole.core.data;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * The root interface from which all converted data objects shall be derived in Wormhole.
 */
public interface DataGroup extends Serializable {

    /**
     * get data nodes.
     *
     * @return plan id
     */
    /**
     * get data nodes.
     *
     * @return {@link Optional}
     */
    Optional<Map<String, DataNode<?>>> getDataNodes();
    
    /**
     * Init {@link DataGroup}.
     *
     * @param dataNodes {@link DataNode}
     */
    void init(Map<String, DataNode<?>> dataNodes);

    /**
     * Append {@link DataNode}.
     *
     * @param name name of {@link DataNode}
     * @param dataNode {@link DataNode}
     * @return removed {@link DataNode}
     */
    Optional<DataNode<?>> append(String name, DataNode<?> dataNode);
}
