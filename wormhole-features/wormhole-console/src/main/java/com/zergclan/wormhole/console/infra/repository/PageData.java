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

package com.zergclan.wormhole.console.infra.repository;

import com.zergclan.wormhole.console.application.domain.entity.BasePO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Data queried by {@link PageQuery}.
 *
 * @param <T> class type of persistent object
 */
@Getter
@Setter
public final class PageData<T extends BasePO> implements Serializable {
    
    private static final long serialVersionUID = -8162517704403906697L;
    
    private final Integer page;
    
    private final Integer size;
    
    private Integer total;
    
    private Integer totalPage;
    
    private Collection<T> items;
    
    public PageData(final int page, final int size) {
        this.page = page;
        this.size = size;
        this.total = 0;
        this.totalPage = 0;
        this.items = new LinkedList<>();
    }
    
    /**
     * init by data queried.
     *
     * @param total total
     * @param items items
     * @return {@link PageData}
     */
    public PageData<T> initData(final int total, final Collection<T> items) {
        this.total = total;
        this.items = items;
        this.totalPage = (total + size - 1) / size;
        return this;
    }
}
