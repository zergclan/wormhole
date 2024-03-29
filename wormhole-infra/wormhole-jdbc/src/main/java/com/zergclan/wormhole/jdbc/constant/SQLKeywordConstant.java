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

package com.zergclan.wormhole.jdbc.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constant of SQL keyword.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SQLKeywordConstant {
    
    public static final String INSERT = "INSERT ";
    
    public static final String INTO = "INTO ";
    
    public static final String VALUES = " VALUES ";
    
    public static final String UPDATE = "UPDATE ";
    
    public static final String SET = " SET ";
    
    public static final String SELECT = "SELECT ";
    
    public static final String AS = " AS ";
    
    public static final String FROM = " FROM ";
    
    public static final String WHERE = " WHERE ";
    
    public static final String AND = " AND ";
}
