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

package com.zergclan.wormhole.writer.xsql.parameter;

import com.zergclan.wormhole.common.WormholeException;

import java.lang.reflect.Field;

public class FieldGetterBean extends FieldGetter {

    private Field field;

    public FieldGetterBean(final Class<?> clazz, final String name) {
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            try {
                field = c.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                continue;
            }

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            break;
        }
        if (field == null) {
            throw new WormholeException("field not exist: " + clazz.getName() + "." + name);
        }
    }

    @Override
    public Object get(final Object bean) {
        if (field == null) {
            return null;
        }

        try {
            return field.get(bean);
        } catch (IllegalArgumentException
                | IllegalAccessException e) {
            System.out.println("IllegalArgumentException or IllegalAccessException");
            return null;
        }
    }
}
