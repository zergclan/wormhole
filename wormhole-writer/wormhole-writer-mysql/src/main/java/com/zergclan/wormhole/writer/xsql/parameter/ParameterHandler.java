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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ParameterHandler {

    private final List<Parameter> vars;

    private final String tail;

    public ParameterHandler(final List<Parameter> vars, final String tail) {
        this.vars = vars;
        this.tail = tail;
    }

    /**
     * set param.
     *
     * @param ps {@link PreparedStatement}
     * @param params {@link Object}
     * @throws SQLException notNull
     */
    public void setParameters(final PreparedStatement ps, final Object params) throws SQLException {
        int index = 1;
        for (Parameter p : vars) {
            index = p.set(ps, params, index);
            index++;
        }
    }

    /**
     * set batch param.
     * @param ps {@link PreparedStatement}
     * @param listData {@link List}
     * @throws SQLException notNull
     */
    public void setBatchParameters(final PreparedStatement ps, final List<Object> listData) throws SQLException {
        int index = 1;
        for (Object params : listData) {
            for (Parameter p : vars) {
                index = p.set(ps, params, index);
                index++;
            }
        }
    }

    /**
     * replace format sql with ?.
     * @param params {@link Object}
     * @return String
     */
    public String getSql(final Object params) {
        StringBuilder sb = new StringBuilder();
        for (Parameter p : vars) {
            p.sql(sb, params);
        }
        sb.append(tail);
        return sb.toString();
    }
}
