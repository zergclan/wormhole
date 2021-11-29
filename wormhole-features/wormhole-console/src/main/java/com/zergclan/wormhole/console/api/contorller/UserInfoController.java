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

package com.zergclan.wormhole.console.api.contorller;

import com.zergclan.wormhole.console.api.vo.HttpResult;
import com.zergclan.wormhole.console.application.domain.entity.UserInfo;
import com.zergclan.wormhole.console.application.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Controller of {@link UserInfo}.
 */
@RestController
public final class UserInfoController extends AbstractRestController {

    @Resource
    private UserInfoService userInfoService;

    /**
     * Add {@link UserInfo}.
     *
     * @param userInfo {@link UserInfo}
     * @return {@link HttpResult}
     */
    @PostMapping("/user")
    public HttpResult<Void> add(@RequestBody final UserInfo userInfo) {
        userInfoService.save(userInfo);
        return success();
    }
    
    /**
     * Add {@link UserInfo}.
     *
     * @param userInfo {@link UserInfo}
     * @return {@link HttpResult}
     */
    @PutMapping("/user")
    public HttpResult<Void> update(@RequestBody final UserInfo userInfo) {
        userInfoService.update(userInfo);
        return success();
    }
    
    

    /**
     * Get {@link UserInfo} by id.
     * @param id id
     * @return {@link HttpResult}
     */
    @GetMapping("/user/{id}")
    public HttpResult<UserInfo> getById(@PathVariable(value = "id") final Integer id) {
        UserInfo userInfo = userInfoService.getById(id);
        return success(userInfo);
    }
}
