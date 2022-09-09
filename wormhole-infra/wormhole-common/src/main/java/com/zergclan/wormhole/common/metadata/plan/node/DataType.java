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

package com.zergclan.wormhole.common.metadata.plan.node;

import com.zergclan.wormhole.common.exception.WormholeException;

/**
 * Data type.
 */
public enum DataType {
    
    OBJECT, TEXT, INT, LONG, MONETARY, DATA_TIME, PATTERNED_DATA_TIME, CODE;
    
    /**
     * Value of type.
     *
     * @param dataType data type
     * @return {@link DataType}
     */
    public static DataType valueOfColumnType(final String dataType) {
        if ("VARCHAR".equalsIgnoreCase(dataType)) {
            return TEXT;
        }
        if ("INT".equalsIgnoreCase(dataType)) {
            return INT;
        }
        if ("BIGINT".equalsIgnoreCase(dataType)) {
            return LONG;
        }
        if ("DECIMAL".equalsIgnoreCase(dataType)) {
            return MONETARY;
        }
        if ("DATETIME".equalsIgnoreCase(dataType) || "TIMESTAMP".equalsIgnoreCase(dataType)) {
            return DATA_TIME;
        }
        throw new WormholeException("error : No enum constant in DataNodeTypeMetaData.DataType value of [%s]", dataType);
    }
}
