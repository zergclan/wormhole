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

package com.zergclan.wormhole.data.core.result;

import com.zergclan.wormhole.data.core.DataGroup;
import lombok.Data;

import java.util.Map;

/**
 * Result value of database loader.
 */
@Data
public final class MysqlLoadResult implements LoadResult {

    private int totalRow;
    
    private int insertRow;
    
    private int updateRow;
    
    private int errorRow;
    
    private int sameRow;
    
    private Map<DataGroup, String> errInfo;
    
    /**
     * Increment insert row.
     */
    public void incrementInsertRow() {
        insertRow++;
    }
    
    /**
     * Increment update row.
     */
    public void incrementUpdateRow() {
        updateRow++;
    }
    
    /**
     * Increment same row.
     */
    public void incrementSameRow() {
        sameRow++;
    }
    
    /**
     * Increment error row.
     */
    public void incrementErrorRow() {
        errorRow++;
    }
}
