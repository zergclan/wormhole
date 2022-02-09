package com.zergclan.wormhole.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql util.
 */
public class SqlUtil {

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
