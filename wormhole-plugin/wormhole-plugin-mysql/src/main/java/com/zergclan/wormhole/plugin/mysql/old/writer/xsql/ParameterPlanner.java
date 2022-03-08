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

package com.zergclan.wormhole.plugin.mysql.old.writer.xsql;

import com.zergclan.wormhole.plugin.mysql.old.writer.util.SqlUtil;
import com.zergclan.wormhole.plugin.mysql.old.writer.xsql.parameter.FieldGetterArray;
import com.zergclan.wormhole.plugin.mysql.old.writer.xsql.parameter.FieldGetterBean;
import com.zergclan.wormhole.plugin.mysql.old.writer.xsql.parameter.FieldGetterMap;
import com.zergclan.wormhole.plugin.mysql.old.writer.xsql.parameter.Parameter;
import com.zergclan.wormhole.plugin.mysql.old.writer.xsql.parameter.ParameterHandler;
import com.zergclan.wormhole.plugin.mysql.old.writer.xsql.parameter.ParameterObject;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ParameterPlanner,can create ParameterHandler.
 */
public class ParameterPlanner {

    private static final Pattern VAR = Pattern.compile("\\$([SsIiLlDdFfTtBbPpMmNnOo])\\{([_0-9a-zA-Z]+)\\}");

    private static final Map<String, Class<? extends Parameter>> PARAMETER_MAP = new HashMap<>();

    static {
        PARAMETER_MAP.put("O", ParameterObject.class);
//        PARAMETER_MAP.put("S", ParameterString.class);
//        PARAMETER_MAP.put("I", ParameterInteger.class);
//        PARAMETER_MAP.put("L", ParameterLong.class);
//        PARAMETER_MAP.put("D", ParameterDouble.class);
//        PARAMETER_MAP.put("F", ParameterFloat.class);
//        PARAMETER_MAP.put("T", ParameterDate.class);
//        PARAMETER_MAP.put("B", ParameterBigDecimal.class);
//        PARAMETER_MAP.put("P", ParameterTimestamp.class);
//        PARAMETER_MAP.put("M", ParameterArrayString.class);
//        PARAMETER_MAP.put("N", ParameterArrayInt.class);
    }

    /**
     * create ParameterHandler.
     * @param sql {@link String}
     * @param param {@link Object}
     * @return ParameterHandler
     */
    @SneakyThrows
    public ParameterHandler plan(final String sql, final Object param) {
        Class<?> clazz = param.getClass();
        List<Parameter> segments = new ArrayList<>();
        int begin = 0;
        Matcher m = VAR.matcher(sql);
        while (m.find()) {
            int end = m.start();
            String fixed = sql.substring(begin, end);
            begin = m.end();
            // handle param
            String type = m.group(1);
            String name = m.group(2);

            Class<? extends Parameter> c = PARAMETER_MAP.get(type);
            if (c == null) {
                c = ParameterObject.class;
            }
            Parameter p = c.newInstance();

            p.setFixed(fixed);

            if (Object[].class.equals(clazz)) {
                p.setGetter(new FieldGetterArray(segments.size()));
            } else if (Map.class.isAssignableFrom(clazz)) {
                p.setGetter(new FieldGetterMap(name));
            } else {
                String name2 = SqlUtil.sqlToJava(name.toLowerCase());
                p.setGetter(new FieldGetterBean(clazz, name2));
            }

            segments.add(p);
        }
        String tail = sql.substring(begin);

        return new ParameterHandler(segments, tail);
    }
}
