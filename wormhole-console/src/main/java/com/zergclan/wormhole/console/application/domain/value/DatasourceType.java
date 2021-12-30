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

package com.zergclan.wormhole.console.application.domain.value;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum DatasourceType {
    
    MYSQL(0, "MySQL");
    
    private final Integer code;
    
    private final String name;
    
    DatasourceType(final Integer code, final String name) {
        this.code = code;
        this.name = name;
    }
    
    /**
     * Get {@link DatasourceType} name by code.
     *
     * @param code code
     * @return name
     */
    public static Optional<String> getNameByCode(final Integer code) {
        DatasourceType[] values = values();
        for (DatasourceType each : values) {
            if (each.getCode().equals(code)) {
                return Optional.of(each.getName());
            }
        }
        return Optional.empty();
    }
}
