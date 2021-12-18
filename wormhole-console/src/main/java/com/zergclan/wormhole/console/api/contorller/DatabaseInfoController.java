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
import com.zergclan.wormhole.console.application.domain.entity.DatabaseInfo;
import com.zergclan.wormhole.console.application.service.DatabaseInfoService;
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
 * Controller of {@link DatabaseInfo}.
 */
@RestController
@RequestMapping("/database")
public class DatabaseInfoController extends AbstractRestController {
    
    @Resource
    private DatabaseInfoService databaseInfoService;
    
    /**
     * Add {@link DatabaseInfo}.
     *
     * @param databaseInfo {@link DatabaseInfo}
     * @return {@link HttpResult}
     */
    @PostMapping
    public HttpResult<Void> add(@RequestBody final DatabaseInfo databaseInfo) {
        databaseInfo.setOperator(getUserSession().getId());
        databaseInfoService.add(databaseInfo);
        return success();
    }

    /**
     * Edit {@link DatabaseInfo}.
     *
     * @param databaseInfo {@link DatabaseInfo}
     * @return {@link HttpResult}
     */
    @PutMapping
    public HttpResult<Void> editById(@RequestBody final DatabaseInfo databaseInfo) {
        databaseInfoService.editById(databaseInfo);
        return success();
    }

    /**
     * Remove {@link DatabaseInfo} by id.
     *
     * @param id id
     * @return {@link HttpResult}
     */
    @DeleteMapping("/{id}")
    public HttpResult<Void> removeById(@PathVariable(value = "id") final Integer id) {
        databaseInfoService.removeById(id);
        return success();
    }

    /**
     * Get {@link DatabaseInfo} by id.
     *
     * @param id id
     * @return {@link HttpResult}
     */
    @GetMapping("/{id}")
    public HttpResult<DatabaseInfo> getById(@PathVariable(value = "id") final Integer id) {
        return success(databaseInfoService.getById(id));
    }

    /**
     * List all {@link DatabaseInfo}.
     *
     * @return {@link HttpResult}
     */
    @GetMapping("/list")
    public HttpResult<List<DatabaseInfo>> listAll() {
        return success(new ArrayList<>(databaseInfoService.listAll()));
    }

    /**
     * List {@link DatabaseInfo} by {@link PageQuery}.
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageData}
     */
    @PostMapping("/page")
    public HttpResult<PageData<DatabaseInfo>> listByPage(@RequestBody final PageQuery<DatabaseInfo> pageQuery) {
        return success(databaseInfoService.listByPage(pageQuery));
    }
}
