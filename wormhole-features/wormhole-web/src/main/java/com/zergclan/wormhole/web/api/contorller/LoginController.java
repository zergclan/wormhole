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

package com.zergclan.wormhole.web.api.contorller;

import com.zergclan.wormhole.web.api.vo.HttpResult;
import com.zergclan.wormhole.web.api.vo.LoginVO;
import com.zergclan.wormhole.web.application.pojo.dto.UserLoginDTO;
import com.zergclan.wormhole.web.application.service.UserActionService;
import com.zergclan.wormhole.web.infra.anticorruption.AntiCorruptionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Controller of Login.
 */
@RestController
public final class LoginController extends AbstractRestController {

    @Resource
    private UserActionService userActionService;

    /**
     * Login.
     *
     * @param loginVO {@link LoginVO}
     * @return {@link HttpResult}
     */
    @PostMapping(value = "/login")
    public HttpResult<String> login(@RequestBody final LoginVO loginVO) {
        UserLoginDTO userLoginDTO = AntiCorruptionService.userLoginVOToDTO(loginVO);
        String sessionToken = this.userActionService.login(userLoginDTO);
        return success(sessionToken);
    }
}
