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

package com.zergclan.wormhole.common.data.result;

import java.util.Collection;

/**
 * The interface of Load result.
 */
public interface LoadResult {
    
    /**
     * get total row.
     *
     * @return total row
     */
    int getTotalRow();

    /**
     * Get insert row.
     *
     * @return insert row
     */
    int getInsertRow();
    
    /**
     * Get update row.
     *
     * @return update row
     */
    int getUpdateRow();
    
    /**
     * Get error row.
     *
     * @return error row
     */
    int getErrorRow();
    
    /**
     * Get same row.
     *
     * @return same row
     */
    int getSameRow();
    
    /**
     * get error info.
     *
     * @return map
     */
    Collection<ErrorDataGroup> getErrorData();
}
