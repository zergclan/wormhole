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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Json mapper.
 */
public final class JsonConverter extends ObjectMapper {
    
    private static final JsonConverter DEFAULT;
    
    private static final JsonConverter NON_NULL;
    
    private static final JsonConverter NON_EMPTY;
    
    private final ObjectMapper objectMapper;
    
    private JsonConverter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    static {
        DEFAULT = new JsonConverter(initDefault());
        NON_NULL = new JsonConverter(initDefault().setSerializationInclusion(Include.NON_NULL));
        NON_EMPTY = new JsonConverter(initDefault().setSerializationInclusion(Include.NON_EMPTY));
    }
    
    private static ObjectMapper initDefault() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }
    
    /**
     * New instance of default.
     *
     * @return {@link JsonConverter}
     */
    public static JsonConverter defaultInstance() {
        return DEFAULT;
    }
    
    /**
     * New instance of ignore null values.
     *
     * @return {@link JsonConverter}
     */
    public static JsonConverter ignoreNullInstance() {
        return NON_NULL;
    }
    
    /**
     * New instance of ignore empty values.
     *
     * @return {@link JsonConverter}
     */
    public static JsonConverter ignoreEmptyInstance() {
        return NON_EMPTY;
    }
    
    /**
     * Convert bean object to json.
     *
     * @param bean bean object
     * @param <T> class type of bean object/{@link Arrays}/{@link Collection}/{@link Map}
     * @return json
     *
     * @throws JsonProcessingException {@link JsonProcessingException}
     */
    public <T> String toJson(final T bean) throws JsonProcessingException {
        Assert.notNull(bean, "error : toJson args bean is null");
        return objectMapper.writeValueAsString(bean);
    }
    
    /**
     * Convert json to bean object shallowly.
     *
     * @param json json
     * @param clazz clazz
     * @param <T> class type of bean object/{@link Arrays}/{@link Collection}/{@link Map} cannot be nested
     * @return bean object
     *
     * @throws JsonProcessingException {@link JsonProcessingException}
     */
    public <T> T shallowParse(final String json, final Class<T> clazz) throws JsonProcessingException {
        Assert.hasText(json, "error : parse json shallowly arg json must has content");
        Assert.notNull(clazz, "error : parse json shallowly arg clazz is null");
        return objectMapper.readValue(json, clazz);
    }
    
    /**
     * Convert json to bean object deeply.
     *
     * @param json json
     * @param javaType {@link JavaType}
     * @param <T> class type of bean object/{@link Arrays}/{@link Collection}/{@link Map}
     * @return bean object
     *
     * @throws JsonProcessingException {@link JsonProcessingException}
     */
    public <T> T deepParse(final String json, final JavaType javaType) throws JsonProcessingException {
        Assert.hasText(json, "error : parse json deeply arg json must has content");
        Assert.notNull(javaType, "error : parse json deeply arg javaType is null");
        return objectMapper.readValue(json, javaType);
    }
    
    /**
     * Build {@link JavaType} of {@link Collection}.
     *
     * @param collectionClazz clazz of {@link Collection}
     * @param elementClazz clazz of {@link Collection} elements
     * @param <T> class type of {@link Collection} elements
     * @return {@link JavaType} of {@link Collection}
     */
    public <T> JavaType buildType(final Class<? extends Collection<T>> collectionClazz, final Class<T> elementClazz) {
        Assert.notNull(collectionClazz, "error : build java type for collection arg collection clazz is null");
        Assert.notNull(elementClazz, "error : build java type for collection arg element clazz is null");
        return objectMapper.getTypeFactory().constructCollectionType(collectionClazz, elementClazz);
    }
    
    /**
     * Build {@link JavaType} of {@link Map}.
     *
     * @param mapClazz clazz of {@link Map}
     * @param keyClazz clazz of {@link Map} keys
     * @param valueClazz clazz of {@link Map} values
     * @param <K> class type of {@link Map} keys
     * @param <V> class type of {@link Map} values
     * @return {@link JavaType} of {@link Map}
     */
    public <K, V> JavaType buildType(final Class<? extends Map<K, V>> mapClazz, final Class<K> keyClazz, final Class<V> valueClazz) {
        Assert.notNull(mapClazz, "error : build java type for map arg map clazz is null");
        Assert.notNull(keyClazz, "error : build java type for map arg key clazz is null");
        Assert.notNull(valueClazz, "error : build java type for map arg value clazz is null");
        return objectMapper.getTypeFactory().constructMapType(mapClazz, keyClazz, valueClazz);
    }
}
