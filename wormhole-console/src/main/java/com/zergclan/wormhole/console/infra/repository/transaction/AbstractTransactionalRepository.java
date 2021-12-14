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

package com.zergclan.wormhole.console.infra.repository.transaction;

import com.zergclan.wormhole.console.api.vo.PageQuery;
import com.zergclan.wormhole.console.application.domain.entity.AbstractPO;
import com.zergclan.wormhole.console.infra.repository.BaseRepository;
import com.zergclan.wormhole.console.infra.repository.PageData;
import com.zergclan.wormhole.console.infra.repository.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * The abstract bean from which all transactional repository shall be derived in Wormhole.
 *
 * @param <T> class type of persistent object
 */
public class AbstractTransactionalRepository<T extends AbstractPO> implements BaseRepository<T> {
    
    @Autowired
    private BaseMapper<T> baseMapper;
    
    private Class<T> clazz;
    
    public AbstractTransactionalRepository() {
        super();
        this.initClazz();
    }
    
    @SuppressWarnings("all")
    private void initClazz() {
        Class<? extends BaseRepository> clazz = this.getClass();
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] types = parameterizedType.getActualTypeArguments();
            this.clazz = (Class<T>) types[0];
        }
    }
    
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void add(final T po) {
        final LocalDateTime now = LocalDateTime.now();
        po.setCreateTime(now);
        po.setModifyTime(now);
        baseMapper.insert(po);
    }
    
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void addBatch(final Collection<T> batch) {
        final LocalDateTime now = LocalDateTime.now();
        for (T each : batch) {
            each.setCreateTime(now);
            each.setModifyTime(now);
        }
        baseMapper.insertBatch(batch);
    }
    
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public boolean edit(final Integer id, final T po) {
        po.setId(id);
        po.setModifyTime(LocalDateTime.now());
        return baseMapper.updateById(po) == 1;
    }
    
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void remove(final Integer id) {
        baseMapper.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    @Override
    public T get(final Integer id) {
        return baseMapper.get(id);
    }
    
    @Transactional(readOnly = true)
    @Override
    public T getOne(final T po) {
        return baseMapper.getOne(po);
    }
    
    @Transactional(readOnly = true)
    @Override
    public int count() {
        return baseMapper.countAll();
    }
    
    @Transactional(readOnly = true)
    @Override
    public Collection<T> listAll() {
        return baseMapper.listAll();
    }
    
    @Transactional(readOnly = true)
    @Override
    public Collection<T> list(final Collection<Integer> ids) {
        return baseMapper.listInIds(ids);
    }
    
    @Transactional(readOnly = true)
    @Override
    public PageData<T> listByPage(final PageQuery<T> pageQuery) {
        PageData<T> result = new PageData<>(pageQuery.getPage(), pageQuery.getSize());
        int total = baseMapper.countByPage(pageQuery.getQuery());
        return 0 == total ? result : result.initData(total, baseMapper.page(pageQuery));
    }
}
