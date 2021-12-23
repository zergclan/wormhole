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

import com.zergclan.wormhole.common.SystemConstant;
import com.zergclan.wormhole.console.api.vo.HttpResult;
import com.zergclan.wormhole.console.api.vo.PageQuery;
import com.zergclan.wormhole.console.application.domain.entity.DatabaseInfo;
import com.zergclan.wormhole.console.application.domain.entity.DatasourceInfo;
import com.zergclan.wormhole.console.application.domain.value.DatasourceType;
import com.zergclan.wormhole.console.application.service.DatabaseInfoService;
import com.zergclan.wormhole.console.application.service.DatasourceInfoService;
import com.zergclan.wormhole.console.infra.exception.WormholeWebException;
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
import java.util.Collection;
import java.util.List;

/**
 * Controller of {@link DatasourceInfo}.
 */
@RestController
@RequestMapping("/datasource")
public class DatasourceInfoController extends AbstractRestController {

    @Resource
    private DatasourceInfoService datasourceInfoService;
    
    @Resource
    private DatabaseInfoService databaseInfoService;

    /**
     * Add {@link DatasourceInfo}.
     *
     * @param datasourceInfo {@link DatasourceInfo}
     * @return {@link HttpResult}
     */
    @PostMapping
    public HttpResult<Void> add(@RequestBody final DatasourceInfo datasourceInfo) {
        datasourceInfo.setOperator(getUserSession().getId());
        datasourceInfoService.add(datasourceInfo);
        return success();
    }

    /**
     * Edit {@link DatasourceInfo}.
     *
     * @param datasourceInfo {@link DatasourceInfo}
     * @return {@link HttpResult}
     */
    @PutMapping
    public HttpResult<Void> editById(@RequestBody final DatasourceInfo datasourceInfo) {
        datasourceInfoService.editById(datasourceInfo);
        return success();
    }

    /**
     * Remove {@link DatasourceInfo} by id.
     *
     * @param id id
     * @return {@link HttpResult}
     */
    @DeleteMapping("/{id}")
    public HttpResult<Void> removeById(@PathVariable(value = "id") final Integer id) {
        datasourceInfoService.removeById(id);
        return success();
    }

    /**
     * Get {@link DatasourceInfo} by id.
     *
     * @param id id
     * @return {@link HttpResult}
     */
    @GetMapping("/{id}")
    public HttpResult<DatasourceInfo> getById(@PathVariable(value = "id") final Integer id) {
        return success(datasourceInfoService.getById(id));
    }

    /**
     * List all {@link DatasourceInfo}.
     *
     * @return {@link HttpResult}
     */
    @GetMapping("/list")
    public HttpResult<List<DatasourceInfo>> listAll() {
        return success(new ArrayList<>(datasourceInfoService.listAll()));
    }

    /**
     * List {@link DatasourceInfo} by {@link PageQuery}.
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageData}
     */
    @PostMapping("/page")
    public HttpResult<PageData<DatasourceInfo>> listByPage(@RequestBody final PageQuery<DatasourceInfo> pageQuery) {
        PageData<DatasourceInfo> datasourceInfos = datasourceInfoService.listByPage(pageQuery);
        Collection<DatasourceInfo> items = datasourceInfos.getItems();
        if (items.isEmpty()) {
            return success(datasourceInfos);
        }
        DatabaseInfo databaseInfo;
        for (DatasourceInfo each : items) {
            databaseInfo = databaseInfoService.getById(each.getDatabaseId());
            each.setOwner(createDatasourceOwner(databaseInfo));
        }
        return success(datasourceInfos);
    }
    
    private String createDatasourceOwner(final DatabaseInfo databaseInfo) {
        return getDatabaseName(databaseInfo) + SystemConstant.SPACE + databaseInfo.getHost() + ":" + databaseInfo.getPort();
    }
    
    private String getDatabaseName(final DatabaseInfo databaseInfo) {
        Integer type = databaseInfo.getType();
        DatasourceType[] values = DatasourceType.values();
        for (DatasourceType each : values) {
            if (each.getCode().equals(type)) {
                return each.getName();
            }
        }
        throw new WormholeWebException(500, "databaseInfo");
    }
}
