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

package com.zergclan.wormhole.console.infra.util;

import com.zergclan.wormhole.tool.constant.MarkConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean mapper.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BeanMapper {
    
    private static final Map<String, BeanCopier> BEAN_COPIER_CONTAINER = new ConcurrentHashMap<>(16);
    
    private static final Map<String, MapperFacade> MAPPER_FACADE_CONTAINER = new ConcurrentHashMap<>(16);
    
    /**
     * Copy bean object shallowly.
     *
     * @param source source bean
     * @param target target bean
     * @param <S> class type of source bean
     * @param <T> class type of target bean
     */
    public static <S, T> void shallowCopy(final S source, final T target) {
        Assert.notNull(source, "error : copy bean shallowly args source is null");
        Assert.notNull(target, "error : copy bean shallowly args target is null");
        createBeanCopier(source.getClass(), target.getClass()).copy(source, target, null);
    }
    
    /**
     * Copy bean object shallowly by custom mapping.
     *
     * @param source source bean
     * @param target target bean
     * @param mappings mappings of bean property
     * @param <S> class type of source bean
     * @param <T> class type of target bean
     */
    public static <S, T> void shallowCopy(final S source, final T target, final Map<String, String> mappings) {
        Assert.notNull(source, "error : copy bean shallowly args source is null");
        Assert.notNull(target, "error : copy bean shallowly args target is null");
        Assert.notEmpty(mappings, "error : copy bean shallowly args mappings is empty");
        createMapperFacade(source.getClass(), target.getClass(), mappings).map(source, target);
    }
   
    /**
     * Copy bean object to map shallowly.
     *
     * @param source source bean
     * @param target target map
     * @param <S> class type of source bean
     */
    @SuppressWarnings("unchecked")
    public static <S> void copyToMap(final S source, final Map<String, Object> target) {
        Assert.notNull(source, "error : beanToMap args source bean is null");
        Assert.notNull(target, "error : beanToMap args target map is null");
        BeanMap.create(source).forEach((key, value) -> {
            if (null != value) {
                target.put(key.toString(), value);
            }
        });
    }
    
    /**
     * Copy map to bean object shallowly.
     *
     * @param source source map
     * @param target target bean
     * @param <T> class type of target bean
     */
    public static <T> void mapToBean(final Map<String, Object> source, final T target) {
        Assert.notNull(source, "error : beanToMap args source map is null");
        Assert.notNull(target, "error : beanToMap args target bean is null");
        BeanMap.create(target).putAll(source);
    }
    
    /**
     * Create {@link BeanCopier}.
     *
     * @param sourceClazz source {@link Class}
     * @param targetClazz target {@link Class}
     * @param <S> class type of source class
     * @param <T> class type of target class
     * @return {@link BeanCopier}
     */
    private static <S, T> BeanCopier createBeanCopier(final Class<S> sourceClazz, final Class<T> targetClazz) {
        return getBeanCopier(createKey(sourceClazz, targetClazz), sourceClazz, targetClazz);
    }
    
    /**
     * create {@link MapperFacade}.
     *
     * @param sourceClazz source {@link Class}
     * @param targetClazz target {@link Class}
     * @param mappings mappings of bean property
     * @param <S> class type of source class
     * @param <T> class type of target class
     * @return {@link MapperFacade}
     */
    private static <S, T> MapperFacade createMapperFacade(final Class<S> sourceClazz, final Class<T> targetClazz, final Map<String, String> mappings) {
        return getMapperFacade(createKey(sourceClazz, targetClazz), sourceClazz, targetClazz, mappings);
    }
    
    /**
     * Get {@link BeanCopier}.
     *
     * @param key mapper key of {@link BeanCopier}
     * @param sourceClazz source {@link Class}
     * @param targetClazz target {@link Class}
     * @param <S> class type of source class
     * @param <T> class type of target class
     * @return {@link BeanCopier}
     */
    private static <S, T> BeanCopier getBeanCopier(final String key, final Class<S> sourceClazz, final Class<T> targetClazz) {
        BeanCopier beanCopier = BEAN_COPIER_CONTAINER.get(key);
        if (null == beanCopier) {
            beanCopier = BeanCopier.create(sourceClazz, targetClazz, false);
            BEAN_COPIER_CONTAINER.put(key, beanCopier);
            return beanCopier;
        }
        return beanCopier;
    }
    
    /**
     * Get {@link MapperFacade}.
     *
     * @param key mapper key of {@link MapperFacade}
     * @param sourceClazz source {@link Class}
     * @param targetClazz target {@link Class}
     * @param mappings mappings of bean property
     * @param <S> class type of source class
     * @param <T> class type of target class
     * @return {@link MapperFacade}
     */
    @SuppressWarnings("all")
    private static <S, T> MapperFacade getMapperFacade(final String key, final Class<S> sourceClazz, final Class<T> targetClazz, final Map<String, String> mappings) {
        MapperFacade mapperFacade = MAPPER_FACADE_CONTAINER.get(key);
        if (Objects.isNull(mapperFacade)) {
            MapperFactory factory = new DefaultMapperFactory.Builder().build();
            ClassMapBuilder classMapBuilder = factory.classMap(sourceClazz, targetClazz);
            mappings.forEach(classMapBuilder::field);
            classMapBuilder.byDefault().register();
            mapperFacade = factory.getMapperFacade();
            MAPPER_FACADE_CONTAINER.put(key, mapperFacade);
        }
        return mapperFacade;
    }
    
    /**
     * create key of mapper.
     *
     * @param sourceClazz source {@link Class}
     * @param targetClazz target {@link Class}
     * @param <S> class type of source class
     * @param <T> class type of target class
     * @return key of mapper
     */
    private static <S, T> String createKey(final Class<S> sourceClazz, final Class<T> targetClazz) {
        return sourceClazz.getCanonicalName() + MarkConstant.SPACE + targetClazz.getCanonicalName();
    }
}
