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

package com.zergclan.wormhole.jdbc.execute;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Executor for SQL.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SQLExecutor {
    
    /**
     * Execute for query.
     *
     * @param connection connection
     * @param sql sql
     * @return {@link ResultSet}
     * @throws SQLException exception {@link SQLException}
     */
    public static ResultSet executeQuery(final Connection connection, final String sql) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            return preparedStatement.executeQuery();
        }
    }
    
    /**
     * Execute for query.
     *
     * @param connection connection
     * @param sql execute sql
     * @param parameters parameters
     * @return {@link ResultSet}
     * @throws SQLException exception {@link SQLException}
     */
    public static ResultSet executeQuery(final Connection connection, final String sql, final Object[] parameters) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParameters(preparedStatement, parameters);
            return preparedStatement.executeQuery();
        }
    }
    
    /**
     * Execute for update.
     *
     * @param connection connection
     * @param sql execute sql
     * @param parameters parameters
     * @return update rows
     * @throws SQLException exception {@link SQLException}
     */
    public static int executeUpdate(final Connection connection, final String sql, final Object[] parameters) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParameters(preparedStatement, parameters);
            return preparedStatement.executeUpdate();
        }
    }
    
    /**
     * Execute for batch.
     *
     * @param connection connection
     * @param sql execute sql
     * @param batchedParameters batched parameters
     * @return update rows of batch
     * @throws SQLException exception {@link SQLException}
     */
    public static int[] executeBatch(final Connection connection, final String sql, final Collection<Object[]> batchedParameters) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Object[] each : batchedParameters) {
                setParameters(preparedStatement, each);
                preparedStatement.addBatch();
            }
            return preparedStatement.executeBatch();
        }
    }
    
    private static void setParameters(final PreparedStatement preparedStatement, final Object[] parameters) throws SQLException {
        int length = parameters.length;
        for (int i = 0; i < length; i++) {
            preparedStatement.setObject(i + 1, parameters[i]);
        }
    }
}
