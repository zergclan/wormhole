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

package com.zergclan.wormhole.plugin.mysql.old.writer.xsql.parameter;

import java.util.Collection;
import java.util.Iterator;

public class FieldGetterSelectCondition extends FieldGetter {

    private final String name;

    private final FieldGetter elementFieldGetter;

    public FieldGetterSelectCondition(final String name, final FieldGetter elementFieldGetter) {
        this.name = name;
        this.elementFieldGetter = elementFieldGetter;
    }

    @Override
    public Object get(final Object params) {
        Collection collection = (Collection)params;
        StringBuilder stringBuilder = new StringBuilder("");
        String[] conditionArr = name.split("-");
        Iterator<Object> iterator = collection.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append("'");
            for(String columnName : conditionArr) {
                stringBuilder.append(elementFieldGetter.get(columnName)).append("-");
            }
            stringBuilder.substring(0, stringBuilder.length() - 1);
            stringBuilder.append("',");
        }
        stringBuilder.substring(0, stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

}
