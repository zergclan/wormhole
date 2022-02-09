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

import com.zergclan.wormhole.common.util.DateUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

class DateConverter extends Converter<Date> {

    @Override
    public Date convert(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Date) {
            return (Date) o;
        }
        String s = o.toString();

        Class<?> clazz = o.getClass();
        if (clazz.getName().equals("oracle.sql.TIMESTAMP")) {
            try {
                Method method = clazz.getMethod("timestampValue", (Class<?>[]) null);

                return (Date) method.invoke(o, (Object[]) null);
            } catch (NoSuchMethodException
                    | IllegalAccessException
                    | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new RuntimeException("con not trans '" + s + "' to date type ", e);
            }
        }

        return DateUtil.parse(s, "yyyy-MM-dd HH:mm:ss");
    }
}
