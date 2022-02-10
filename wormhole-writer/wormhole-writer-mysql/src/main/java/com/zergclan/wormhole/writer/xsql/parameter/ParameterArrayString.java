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

public class ParameterArrayString extends Parameter {
    @SuppressWarnings("unchecked")
    @Override
    public void sql(final StringBuilder sb, final Object params) {
        sb.append(getFixed());
        List<String> list = (List<String>) getGetter().get(params);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("?");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public int set(final PreparedStatement ps, final Object params, final int index) throws SQLException {
        List<String> list = (List<String>) getGetter().get(params);
        for (String v : list) {
            ps.setString(index, v);
        }
        return index;
    }
}
