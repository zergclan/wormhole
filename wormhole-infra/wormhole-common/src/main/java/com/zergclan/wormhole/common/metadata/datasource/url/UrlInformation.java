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

package com.zergclan.wormhole.common.metadata.datasource.url;

import com.zergclan.wormhole.common.metadata.exception.UnrecognizedDatabaseURLException;
import com.zergclan.wormhole.tool.util.StringUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Url information.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class UrlInformation {
    
    private static final String SCHEMA_PATTERN = "(?<schema>[\\w+:%]+)\\s*";
    
    private static final String AUTHORITY_PATTERN = "(?://(?<authority>[^/?#]*))?\\s*";
    
    private static final String PATH_PATTERN = "(?:/(?!\\s*/)(?<path>[^?#]*))?";
    
    private static final String QUERY_PATTERN = "(?:\\?(?!\\s*\\?)(?<query>[^#]*))?";
    
    private static final Pattern CONNECTION_URL_PATTERN = Pattern.compile(SCHEMA_PATTERN + AUTHORITY_PATTERN + PATH_PATTERN + QUERY_PATTERN, Pattern.CASE_INSENSITIVE);
    
    private static final String AUTHORITY_GROUP_KEY = "authority";
    
    private static final String PATH_GROUP_KEY = "path";
    
    private static final String QUERY_GROUP_KEY = "query";
    
    private final String url;
    
    private final String host;
    
    private final int port;
    
    private final String catalog;
    
    private final Properties queryProperties;
    
    /**
     * Build {@link UrlInformation}.
     *
     * @param url url
     * @return {@link UrlInformation}
     */
    public static UrlInformation build(final String url) {
        return UrlInformationParser.parseUrl(url);
    }
    
    /**
     * Parser for {@link UrlInformation}.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class UrlInformationParser {
        
        private static UrlInformation parseUrl(final String url) {
            Matcher matcher = CONNECTION_URL_PATTERN.matcher(url);
            if (matcher.matches()) {
                String authority = matcher.group(AUTHORITY_GROUP_KEY);
                if (null == authority) {
                    throw new UnrecognizedDatabaseURLException(url, CONNECTION_URL_PATTERN.pattern().replaceAll("%", "%%"));
                }
                return new UrlInformation(url, parseHost(authority), parsePort(authority), matcher.group(PATH_GROUP_KEY), parseQueryProperties(matcher.group(QUERY_GROUP_KEY)));
            }
            throw new UnrecognizedDatabaseURLException(url, CONNECTION_URL_PATTERN.pattern().replaceAll("%", "%%"));
        }
        
        private static String parseHost(final String authority) {
            if (!authority.contains(":")) {
                return authority;
            }
            return authority.split(":")[0];
        }
        
        private static int parsePort(final String authority) {
            if (!authority.contains(":")) {
                return -1;
            }
            String port = authority.split(":")[1];
            if (port.contains(",")) {
                port = port.split(",")[0];
            }
            return Integer.parseInt(port);
        }
        
        private static Properties parseQueryProperties(final String query) {
            if (StringUtil.isBlank(query)) {
                return new Properties();
            }
            Properties result = new Properties();
            for (String each : query.split("&")) {
                String[] property = each.split("=", 2);
                result.setProperty(property[0], property[1]);
            }
            return result;
        }
    }
}
