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

package com.zergclan.wormhole.console.api.vo;

import com.zergclan.wormhole.common.metadata.authorization.user.WormholeUser;
import com.zergclan.wormhole.console.api.security.UserSessionManager;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Login result.
 */
@NoArgsConstructor
@Data
public final class LoginResult implements Serializable {
    
    private static final long serialVersionUID = -7036855054412530232L;
    
    private boolean logined;
    
    private String token;
    
    @Builder(toBuilder = true)
    public LoginResult(final boolean logined, final String token) {
        this.logined = logined;
        this.token = token;
    }
    
    /**
     * Success.
     *
     * @param user {@link WormholeUser}
     * @return {@link LoginResult}
     */
    public static LoginResult success(final WormholeUser user) {
        return new LoginResult().toBuilder().logined(true).token(UserSessionManager.createUserSession(user.getUsername(), user.getPassword())).build();
    }
    
    /**
     * Failed.
     *
     * @return {@link LoginResult}
     */
    public static LoginResult failed() {
        return new LoginResult().toBuilder().logined(false).build();
    }
}
