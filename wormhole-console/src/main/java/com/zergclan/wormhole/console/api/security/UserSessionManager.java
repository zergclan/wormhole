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

package com.zergclan.wormhole.console.api.security;

import com.zergclan.wormhole.console.application.domain.entity.UserInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for {@link UserSession}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserSessionManager {
    
    private static final Map<String, UserSession> SESSION_CONTAINER;
    
    static {
        SESSION_CONTAINER = new ConcurrentHashMap<>(16, 1);
        SESSION_CONTAINER.put("wormhole-console-test-token", new UserSession("root", "root"));
    }
    
    /**
     * Get {@link UserSession}.
     *
     * @param token token
     * @return {@link UserSession}
     */
    public static Optional<UserSession> getUserSession(final String token) {
        UserSession userSession = SESSION_CONTAINER.get(token);
        return null == userSession ? Optional.empty() : Optional.of(userSession);
    }
    
    /**
     * Create {@link UserSession} by {@link UserInfo}.
     *
     * @param username username
     * @param password username
     * @return token
     */
    public static String createUserSession(final String username, final String password) {
        String token = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        SESSION_CONTAINER.put(token, new UserSession(username, password));
        return token;
    }
    
    /**
     * Clear {@link UserSession}.
     *
     * @param token token
     */
    public static void clearUserSession(final String token) {
        SESSION_CONTAINER.remove(token);
    }
}
