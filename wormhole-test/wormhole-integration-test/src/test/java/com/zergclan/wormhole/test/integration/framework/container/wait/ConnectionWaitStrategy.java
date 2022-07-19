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

package com.zergclan.wormhole.test.integration.framework.container.wait;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.testcontainers.containers.wait.strategy.AbstractWaitStrategy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Wait strategy implemented via connection checking.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConnectionWaitStrategy extends AbstractWaitStrategy {
    
    private final Callable<Connection> connectionSupplier;
    
    /**
     * build wait strategy of JDBC connection.
     *
     * @param jdbcUrl jdbc url
     * @param user user
     * @param password password
     * @return {@link ConnectionWaitStrategy}
     */
    public static ConnectionWaitStrategy buildJDBCConnectionWaitStrategy(final String jdbcUrl, final String user, final String password) {
        return new ConnectionWaitStrategy(() -> DriverManager.getConnection(jdbcUrl, user, password));
    }
    
    @Override
    protected void waitUntilReady() {
        Unreliables.retryUntilSuccess((int) startupTimeout.getSeconds(), TimeUnit.SECONDS, () -> {
            getRateLimiter().doWhenReady(() -> {
                try (Connection unused = connectionSupplier.call()) {
                    log.info("Container ready");
                    // CHECKSTYLE:OFF
                } catch (final Exception ex) {
                    // CHECKSTYLE:ON
                    throw new RuntimeException("Not Ready yet.", ex);
                }
            });
            return true;
        });
    }
}
