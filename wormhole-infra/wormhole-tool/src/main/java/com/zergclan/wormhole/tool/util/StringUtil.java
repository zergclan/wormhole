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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Util tools for String.
 * <p>
 *     Mainly for internal use within the framework,
 *     referenced {@code org.apache.commons.lang3.Validate} from
 *     <a href="https://commons.apache.org/proper/commons-lang/">Apache Commons Lang</a>
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtil {
    
    /**
     * Is blank validation of char sequence.
     *
     * @param charSequence char sequence
     * @return is blank or not
     */
    public static boolean isBlank(final CharSequence charSequence) {
        final int strLen = charSequence == null ? 0 : charSequence.length();
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Split deduplicate.
     *
     * @param input input
     * @param delimiter delimiter
     * @return elements
     */
    public static Collection<String> deduplicateSplit(final String input, final String delimiter) {
        if (isBlank(input)) {
            return new LinkedList<>();
        }
        String[] split = input.split(delimiter);
        int length = split.length;
        Set<String> result = new LinkedHashSet<>(length, 1);
        for (int i = 0; i < length; i++) {
            result.add(split[i].trim());
        }
        return result;
    }
    
    /**
     * Split in two parts.
     *
     * @param input input
     * @param delimiter delimiter
     * @return two parts
     */
    public static String[] twoPartsSplit(final String input, final String delimiter) {
        int index = input.indexOf(delimiter);
        int lastIndex = input.lastIndexOf(delimiter);
        boolean state = index == lastIndex && index > 0 && index < input.length() - 2;
        Validator.preState(state, "error: Illegal input arg: {%d} in two parts split", input);
        String[] result = new String[2];
        result[0] = input.substring(0, index);
        result[1] = input.substring(index + 1);
        return result;
    }
    
    /**
     * <p>Joins the elements of the provided {@code Iterator} into
     * a single String containing the provided elements.</p>
     *
     * <p>No delimiter is added before or after the list.
     * A {@code null} separator is the same as an empty String ("").</p>
     *
     * @param iterator  the {@code Iterator} of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @return the joined String, {@code null} if null iterator input
     */
    public static String join(final Iterator<String> iterator, final String separator) {
        if (!iterator.hasNext()) {
            return "";
        }
        StringJoiner stringJoiner = new StringJoiner(separator);
        String each;
        while (iterator.hasNext()) {
            each = iterator.next();
            stringJoiner.add(each);
        }
        return stringJoiner.toString();
    }
    
    /**
     * Returns the string representation of the {@code Object} argument.
     *
     * @param object an {@code Object}
     * @return if the argument is {@code null}, then a string equal to {@code ""}, otherwise, the value of  {@code obj.toString()} is returned.
     */
    public static String valueOf(final Object object) {
        return null == object ? "" : object.toString();
    }
}
