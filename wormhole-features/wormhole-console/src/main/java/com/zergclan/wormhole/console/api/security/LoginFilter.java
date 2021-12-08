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

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Filter for Login with token.
 */
@Component
public final class LoginFilter extends OncePerRequestFilter {
    
    public static final String TOKEN = "token";
    
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws ServletException, IOException {
        getToken(request).flatMap(UserSessionManager::getUserSession).ifPresent(this::setAuthentication);
        chain.doFilter(request, response);
    }
    
    private void setAuthentication(final UserSession userSession) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userSession, userSession.getPassword(), userSession.getAuthorities()));
    }
    
    private Optional<String> getToken(final HttpServletRequest request) {
        String token = request.getHeader(TOKEN);
        return null == token ? Optional.empty() : Optional.of(token);
    }
}
