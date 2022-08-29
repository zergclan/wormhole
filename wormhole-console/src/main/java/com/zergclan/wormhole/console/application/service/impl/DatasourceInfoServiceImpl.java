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

import com.zergclan.wormhole.tool.constant.MarkConstant;
import com.zergclan.wormhole.console.api.vo.PageQuery;
import com.zergclan.wormhole.console.api.vo.ResultCode;
import com.zergclan.wormhole.console.application.domain.entity.DatabaseInfo;
import com.zergclan.wormhole.console.application.domain.entity.DatasourceInfo;
import com.zergclan.wormhole.console.application.domain.value.DatasourceType;
import com.zergclan.wormhole.console.application.service.DatasourceInfoService;
import com.zergclan.wormhole.console.infra.exception.WormholeWebException;
import com.zergclan.wormhole.console.infra.repository.BaseRepository;
import com.zergclan.wormhole.console.infra.repository.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Optional;

/**
 * Implemented Service of {@link DatasourceInfoService}.
 */
@Service(value = "datasourceInfoService")
public class DatasourceInfoServiceImpl implements DatasourceInfoService {

    @Resource
    private BaseRepository<DatasourceInfo> datasourceInfoRepository;
    
    @Resource
    private BaseRepository<DatabaseInfo> databaseInfoRepository;

    @Override
    public void add(final DatasourceInfo datasourceInfo) {
        datasourceInfoRepository.add(datasourceInfo);
    }

    @Override
    public boolean editById(final DatasourceInfo datasourceInfo) {
        return datasourceInfoRepository.edit(datasourceInfo.getId(), datasourceInfo);
    }

    @Override
    public boolean removeById(final Integer id) {
        return datasourceInfoRepository.remove(id);
    }

    @Override
    public DatasourceInfo getById(final Integer id) {
        return datasourceInfoRepository.get(id);
    }

    @Override
    public Collection<DatasourceInfo> listAll() {
        return datasourceInfoRepository.listAll();
    }

    @Override
    public PageData<DatasourceInfo> listByPage(final PageQuery<DatasourceInfo> pageQuery) {
        PageData<DatasourceInfo> result = datasourceInfoRepository.listByPage(pageQuery);
        Collection<DatasourceInfo> items = result.getItems();
        if (!items.isEmpty()) {
            initOwner(items);
        }
        return result;
    }
    
    private void initOwner(final Collection<DatasourceInfo> items) {
        DatabaseInfo databaseInfo;
        for (DatasourceInfo each : items) {
            databaseInfo = databaseInfoRepository.get(each.getDatabaseId());
            each.setOwner(createDatasourceOwner(databaseInfo));
        }
    }
    
    private String createDatasourceOwner(final DatabaseInfo databaseInfo) {
        return getDatabaseType(databaseInfo) + MarkConstant.SPACE + databaseInfo.getHost() + MarkConstant.COLON + databaseInfo.getPort();
    }
    
    private String getDatabaseType(final DatabaseInfo databaseInfo) {
        Integer type = databaseInfo.getType();
        Optional<String> name = DatasourceType.getNameByCode(type);
        if (name.isPresent()) {
            return name.get();
        }
        throw new WormholeWebException(ResultCode.ERROR.getCode(), "error : database type [" + type + "] not exist");
    }
}
