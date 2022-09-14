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

package com.zergclan.wormhole.console.application.service.impl;

import com.zergclan.wormhole.common.metadata.authorization.AuthenticationResult;
import com.zergclan.wormhole.console.api.vo.LoginResult;
import com.zergclan.wormhole.console.application.context.SpringContextContainer;
import com.zergclan.wormhole.console.application.domain.entity.UserInfo;
import com.zergclan.wormhole.console.application.service.LoginService;
import org.springframework.stereotype.Service;

/**
 * Implemented Service of {@link LoginService}.
 */
@Service(value = "loginService")
public class LoginServiceImpl implements LoginService {
    
    @Override
    public LoginResult login(final UserInfo userInfo) {
        AuthenticationResult authenticationResult = SpringContextContainer.getExecutionEngine().authenticate(userInfo.getUsername(), userInfo.getPassword());
        if (authenticationResult.isSuccess()) {
            return LoginResult.success(authenticationResult.getResultData());
        }
        return LoginResult.failed();
    }
}
