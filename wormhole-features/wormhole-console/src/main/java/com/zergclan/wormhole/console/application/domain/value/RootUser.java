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

/**
 * Root user of Wormhole.
 */
@Getter
public enum RootUser {

    ROOT("root", "root");

    private final String loginName;
    

    private final String secretKey;
    
    RootUser(final String loginName, final String secretKey) {
        this.loginName = loginName;
        this.secretKey = secretKey;
    }

    /**
     * Is root user of Wormhole.
     *
     * @param loginName login name
     * @param secretKey secret key
     * @return is root user or not
     */
    public boolean isRoot(final String loginName, final String secretKey) {
        return this.loginName.equals(loginName) && this.secretKey.equals(secretKey);
    }
}
