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

package com.zergclan.wormhole.bootstrap.engine;

import com.zergclan.wormhole.common.metadata.authorization.AuthenticationResult;
import com.zergclan.wormhole.common.metadata.authorization.AuthorizationMetaData;
import com.zergclan.wormhole.common.metadata.authorization.user.WormholeUser;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Authorization execution engine.
 */
@RequiredArgsConstructor
public final class AuthorizationExecutionEngine {
    
    private final AuthorizationMetaData authorization;
    
    /**
     * Authenticate.
     *
     * @param username username
     * @param password password
     * @return {@link AuthenticationResult}
     */
    public AuthenticationResult authenticate(final String username, final String password) {
        Optional<WormholeUser> user = authorization.getUser(username);
        if (user.isPresent()) {
            if (password.equals(user.get().getPassword())) {
                return new AuthenticationResult(true, user.get());
            }
        }
        return new AuthenticationResult(false, null);
    }
}
