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

package com.zergclan.wormhole.tool.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Value extractor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValueExtractor {
    
    /**
     * Extract value or default.
     *
     * @param input input
     * @param defaultValue default value
     * @param <T> class type of input
     * @return value
     */
    public static <T> T extractValueOrDefault(final T input, final T defaultValue) {
        return Objects.isNull(input) ? defaultValue : input;
    }
    
    /**
     * Extract required string value by name.
     *
     * @param properties properties
     * @param name name
     * @return value
     */
    public static String extractRequiredString(final Properties properties, final String name) {
        Validator.notNull(properties, "error: extract required string arg properties can not be null");
        Validator.notBlank(name, "error: extract required string arg name can not blank");
        Object valueObject = properties.get(name);
        Validator.notNull(valueObject, "error: extract required string value can not be null, detail kay:value [%s:%s]", name, valueObject);
        return valueObject.toString();
    }
    
    /**
     * Extract required or default string value by name.
     *
     * @param properties properties
     * @param name name
     * @param defaultValue default value
     * @return value
     */
    public static String extractRequiredString(final Properties properties, final String name, final String defaultValue) {
        Validator.notNull(properties, "error: extract required string arg properties can not be null");
        Validator.notBlank(name, "error: extract required string arg name can not blank");
        Validator.notNull(name, "error: extract required string arg default value can not null");
        Object valueObject = properties.get(name);
        return null == valueObject ? defaultValue : valueObject.toString();
    }
    
    /**
     * Extract required int value by name.
     *
     * @param properties properties
     * @param name name
     * @return value
     */
    public static Integer extractRequiredInt(final Properties properties, final String name) {
        Validator.notNull(properties, "error: extract required int arg properties can not be null");
        Validator.notBlank(name, "error: extract required int arg name can not blank");
        Object valueObject = properties.get(name);
        Validator.notNull(valueObject, "error: extract required int value can not be null, detail kay:value [%s:%s]", name, valueObject);
        return Integer.parseInt(valueObject.toString());
    }
    
    /**
     * Extract required or default int value by name.
     *
     * @param properties properties
     * @param name name
     * @param defaultValue default value
     * @return value
     */
    public static Integer extractRequiredInt(final Properties properties, final String name, final int defaultValue) {
        Validator.notNull(properties, "error: extract required int arg properties can not be null");
        Validator.notBlank(name, "error: extract required int arg name can not blank");
        Object valueObject = properties.get(name);
        return null == valueObject ? defaultValue : Integer.parseInt(valueObject.toString());
    }
    
    /**
     * Extract required boolean value by name.
     *
     * @param properties properties
     * @param name name
     * @return value
     */
    public static Boolean extractRequiredBoolean(final Properties properties, final String name) {
        Validator.notNull(properties, "error: extract required boolean arg properties can not be null");
        Validator.notBlank(name, "error: extract required boolean arg name can not blank");
        Object valueObject = properties.get(name);
        Validator.notNull(valueObject, "error: extract required boolean value can not be null, detail kay:value [%s:%s]", name, valueObject);
        return Boolean.parseBoolean(valueObject.toString());
    }
    
    /**
     * Extract required or default boolean value by name.
     *
     * @param properties properties
     * @param name name
     * @param defaultValue default value
     * @return value
     */
    public static Boolean extractRequiredBoolean(final Properties properties, final String name, final boolean defaultValue) {
        Validator.notNull(properties, "error: extract required boolean arg properties can not be null");
        Validator.notBlank(name, "error: extract required boolean arg name can not blank");
        Object valueObject = properties.get(name);
        return null == valueObject ? defaultValue : Boolean.parseBoolean(valueObject.toString());
    }
    
    /**
     * Extract required array by name.
     *
     * @param properties properties
     * @param name name
     * @param delimiter delimiter
     * @return array
     */
    public static String[] extractRequiredArray(final Properties properties, final String name, final String delimiter) {
        Validator.notNull(properties, "error: extract required array arg properties can not be null");
        Validator.notBlank(name, "error: extract required array arg name can not blank");
        Validator.notBlank(name, "error: extract required array arg delimiter can not blank");
        Object valueObject = properties.get(name);
        Validator.notNull(valueObject, "error: extract required boolean value can not be null, detail kay:value [%s:%s]", name, valueObject);
        Collection<String> strings = StringUtil.deduplicateSplit(valueObject.toString(), delimiter);
        String[] values = new String[strings.size()];
        return strings.toArray(values);
    }
    
    /**
     * Extract required map by name.
     *
     * @param properties properties
     * @param ignore ignore
     * @return map
     */
    public static Map<String, String> extractRequiredMap(final Properties properties, final String... ignore) {
        Validator.notNull(properties, "error: extract required map arg properties can not be null");
        for (String each : ignore) {
            properties.remove(each);
        }
        Validator.preState(!properties.isEmpty(), "error : extract required map related properties can not be empty");
        Map<String, String> result = new HashMap<>(properties.size(), 1);
        Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            result.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return result;
    }
}
