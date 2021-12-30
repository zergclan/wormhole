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
import com.zergclan.wormhole.console.api.vo.PageQuery;
import com.zergclan.wormhole.console.application.domain.entity.UserInfo;
import com.zergclan.wormhole.console.application.service.UserInfoService;
import com.zergclan.wormhole.console.infra.repository.PageData;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller of {@link UserInfo}.
 */
@RestController
@RequestMapping("/user")
public class UserInfoController extends AbstractRestController {
    
    @Resource
    private UserInfoService userInfoService;

    /**
     * Add {@link UserInfo}.
     *
     * @param userInfo {@link UserInfo}
     * @return {@link HttpResult}
     */
    @PostMapping
    public HttpResult<Void> add(@RequestBody final UserInfo userInfo) {
        userInfoService.add(userInfo);
        return success();
    }
    
    /**
     * Edit {@link UserInfo} by id.
     *
     * @param id id
     * @param userInfo {@link UserInfo}
     * @return {@link HttpResult}
     */
    @PutMapping("/{id}")
    public HttpResult<Void> editById(@PathVariable(value = "id") final Integer id, @RequestBody final UserInfo userInfo) {
        userInfoService.editById(id, userInfo);
        return userInfoService.editById(id, userInfo) ? success() : failed();
    }
    
    /**
     * Remove {@link UserInfo} by id.
     *
     * @param id id
     * @return {@link HttpResult}
     */
    @DeleteMapping("/{id}")
    public HttpResult<Void> removeById(@PathVariable(value = "id") final Integer id) {
        userInfoService.removeById(id);
        return success();
    }

    /**
     * Get {@link UserInfo} by id.
     *
     * @param id id
     * @return {@link HttpResult}
     */
    @GetMapping("/{id}")
    public HttpResult<UserInfo> getById(@PathVariable(value = "id") final Integer id) {
        return success(userInfoService.getById(id));
    }

    /**
     * Get list of {@link UserInfo}.
     *
     * @return {@link HttpResult}
     */
    @GetMapping("/list")
    public HttpResult<List<UserInfo>> listAll() {
        return success(new ArrayList<>(userInfoService.listAll()));
    }
    
    /**
     * Get {@link PageData} of {@link UserInfo}.
     *
     * @param page {@link PageQuery}
     * @return {@link HttpResult}
     */
    @PostMapping("/page")
    public HttpResult<PageData<UserInfo>> listByPage(@RequestBody final PageQuery<UserInfo> page) {
        return success(userInfoService.listByPage(page));
    }
}
