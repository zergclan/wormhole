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
import com.zergclan.wormhole.console.api.vo.ResultCode;

/**
 * The base class from which all rest controller shall be derived in Wormhole.
 */
public abstract class AbstractRestController {

    /**
     * Success only.
     *
     * @return http result {@link HttpResult}
     */
    protected HttpResult<Void> success() {
        return new HttpResult<Void>().toBuilder().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage()).build();
    }

    /**
     * Success with data.
     *
     * @param data result data
     * @param <T> class type of data
     * @return http result {@link HttpResult}
     */
    protected <T> HttpResult<T> success(final T data) {
        return new HttpResult<T>().toBuilder().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage()).data(data).build();
    }

    /**
     * Success with code and data.
     *
     * @param resultCode {@link ResultCode}
     * @param data result data
     * @param <T> class type of data
     * @return http result {@link HttpResult}
     */
    protected <T> HttpResult<T> success(final ResultCode resultCode, final T data) {
        return new HttpResult<T>().toBuilder().code(resultCode.getCode()).message(resultCode.getMessage()).data(data).build();
    }

    /**
     * Failed with code.
     *
     * @param resultCode {@link ResultCode}
     * @return http result {@link HttpResult}
     */
    protected <T> HttpResult<T> failed(final ResultCode resultCode, final T data) {
        return new HttpResult<T>().toBuilder().code(resultCode.getCode()).message(resultCode.getMessage()).data(data).build();
    }
}
