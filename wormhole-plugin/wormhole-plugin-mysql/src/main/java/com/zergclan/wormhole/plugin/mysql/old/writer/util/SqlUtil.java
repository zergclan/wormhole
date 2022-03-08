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

package com.zergclan.wormhole.plugin.mysql.old.writer.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql util.
 */
public final class SqlUtil {

    /**
     * sql to java.
     * @param field {@link String}
     * @return String
     */
    public static String sqlToJava(final String field) {
        String[] a = field.split("_");
        StringBuffer sb = new StringBuffer(a[0]);
        for (int i = 1; i < a.length; i++) {
            String b = a[i];
            if (b.length() == 0) {
                continue;
            }
            sb.append(Character.toUpperCase(b.charAt(0)));
            sb.append(b.substring(1));
        }
        return sb.toString();
    }

    /**
     * get the String after values.
     * @param sql {@link String}
     * @return String
     */
    public static String trimValues(final String sql) {
        String patternString = "[Vv][Aa][Ll][Uu][Ee][Ss]]";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(sql);
        return matcher.find() ? sql.substring(matcher.start()) : "";
    }
}
