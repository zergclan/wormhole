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

package com.zergclan.wormhole.common.exception;

/**
 * Basic exception of Wormhole.
 */
public abstract class WormholeException extends RuntimeException {
    
    /**
     * Constructs an exception with formatted error message and args.
     *
     * @param errorMessage formatted error message
     * @param args args of error message
     */
    protected WormholeException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }
    
    /**
     * Constructs an exception with cause.
     *
     * @param cause error cause
     */
    protected WormholeException(final Throwable cause) {
        super(cause);
    }
    
    /**
     * Constructs an exception with error message and cause.
     *
     * @param message error message
     * @param cause error cause
     */
    protected WormholeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
