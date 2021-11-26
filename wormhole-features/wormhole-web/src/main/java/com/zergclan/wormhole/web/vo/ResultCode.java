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

package com.zergclan.wormhole.web.vo;

import lombok.Getter;

/**
 * Code for http result.
 */
@Getter
public enum ResultCode {
    
    /**
     * Result code for success.
     */
    SUCCESS(100000, "SUCCESS"),
    
    /**
     * Result code for bad request.
     */
    BAD_REQUEST(400000, "BAD_REQUEST"),
    
    /**
     * Result code for failed.
     */
    FAILED(500000, "FAILED"),
    
    /**
     * Result code for error.
     */
    ERROR(999999, "ERROR");
    
    /**
     * Code.
     */
    private final int code;
    
    /**
     * Message.
     */
    private final String message;
    
    ResultCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}
