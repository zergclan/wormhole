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

package com.zergclan.wormhole.common.metadata.user;

import com.zergclan.wormhole.common.metadata.WormholeMetadata;
import lombok.Getter;
import lombok.Setter;

/**
 * Unique type of Wormhole user.
 */
@Getter
public enum WormholeUser implements WormholeMetadata {
    
    /**
     * Administrator of Wormhole.
     */
    ADMINISTRATOR("admin", "admin");
    
    @Setter
    private String loginName;
    
    @Setter
    private String secretKey;
    
    WormholeUser(final String loginName, final String secretKey) {
        this.loginName = loginName;
        this.secretKey = secretKey;
    }
}
