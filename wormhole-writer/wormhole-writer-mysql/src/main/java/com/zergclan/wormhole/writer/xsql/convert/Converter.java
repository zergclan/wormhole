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

package com.zergclan.wormhole.writer.xsql.convert;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class Converter<T> {

    private static final Map<Class<?>, Converter<?>> CONVERTER_MAP = new HashMap<>();

    static {
        CONVERTER_MAP.put(Short.TYPE, new ShortConverter(true));
        CONVERTER_MAP.put(Short.class, new ShortConverter(true));
        CONVERTER_MAP.put(Integer.TYPE, new IntegerConverter(true));
        CONVERTER_MAP.put(Integer.class, new IntegerConverter(true));
        CONVERTER_MAP.put(Long.TYPE, new LongConverter(true));
        CONVERTER_MAP.put(Long.class, new LongConverter(true));
        CONVERTER_MAP.put(Float.TYPE, new FloatConverter(true));
        CONVERTER_MAP.put(Float.class, new FloatConverter(true));
        CONVERTER_MAP.put(Double.TYPE, new DoubleConverter(true));
        CONVERTER_MAP.put(Double.class, new DoubleConverter(true));
        CONVERTER_MAP.put(Boolean.TYPE, new BooleanConverter(true));
        CONVERTER_MAP.put(Boolean.class, new BooleanConverter(true));
        CONVERTER_MAP.put(String.class, new StringConverter());
        CONVERTER_MAP.put(Date.class, new DateConverter());
        CONVERTER_MAP.put(byte[].class, new ByteArrayConverter());
        CONVERTER_MAP.put(BigDecimal.class, new BigDecimalConverter(false));
        CONVERTER_MAP.put(Timestamp.class, new TimestampConverter());
    }

    private static Converter<Enum<?>> enumConverter = new EnumConverter();

    /**
     * get converter.
     * @param type {@link Class}
     * @param <T> {@link Class}
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> Converter<T> getConverter(final Class<T> type) {
        Converter<T> converter = (Converter<T>) CONVERTER_MAP.get(type);
        if (converter == null) {
            if (Enum.class.isAssignableFrom(type)) {
                return (Converter<T>) enumConverter;
            }

            throw new RuntimeException("not converter : " + type);
        }

        return converter;
    }

    protected abstract T convert(Object o);

    /**
     * convert.
     * @param o {@link Object}
     * @param clazz {@link Class}
     * @return T
     */
    public T convert(final Object o, final Class<?> clazz) {
        return convert(o);
    }
}
