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

package com.zergclan.wormhole.console.api.contorller.log;

import com.zergclan.wormhole.console.api.contorller.AbstractRestController;
import com.zergclan.wormhole.console.api.vo.HttpResult;
import com.zergclan.wormhole.console.api.vo.PageQuery;
import com.zergclan.wormhole.console.application.domain.metrics.ErrorDataLog;
import com.zergclan.wormhole.console.application.service.log.ErrorDataLogService;
import com.zergclan.wormhole.console.infra.repository.PageData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Controller of {@link ErrorDataLog}.
 */
@RestController
@RequestMapping("/metrics/error/data")
public class ErrorDataLogController extends AbstractRestController {
    
    @Resource
    private ErrorDataLogService errorDataLogService;
    
    /**
     * List {@link ErrorDataLog} by {@link PageQuery}.
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageData}
     */
    @PostMapping("/page")
    public HttpResult<PageData<ErrorDataLog>> listByPage(@RequestBody final PageQuery<ErrorDataLog> pageQuery) {
        return success(errorDataLogService.listByPage(pageQuery));
    }
}
