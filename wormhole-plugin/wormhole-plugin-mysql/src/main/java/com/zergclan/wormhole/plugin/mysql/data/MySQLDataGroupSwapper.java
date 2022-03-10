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

package com.zergclan.wormhole.plugin.mysql.data;

import com.zergclan.wormhole.common.exception.WormholeException;
import com.zergclan.wormhole.common.util.DateUtil;
import com.zergclan.wormhole.core.api.Swapper;
import com.zergclan.wormhole.core.api.data.DataGroup;
import com.zergclan.wormhole.core.api.data.DataNode;
import com.zergclan.wormhole.core.data.BigDecimalDataNode;
import com.zergclan.wormhole.core.data.IntegerDataNode;
import com.zergclan.wormhole.core.data.LocalDateTimeDataNode;
import com.zergclan.wormhole.core.data.LongDataNode;
import com.zergclan.wormhole.core.data.StringDataNode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * MySQL data group swapper.
 */
public final class MySQLDataGroupSwapper implements Swapper<Map<String, Object>, DataGroup> {
    
    @Override
    public DataGroup swapToTarget(final Map<String, Object> dataMap) {
        Map<String, DataNode<?>> dataNodes = new LinkedHashMap<>();
        Iterator<Map.Entry<String, Object>> iterator = dataMap.entrySet().iterator();
        Map.Entry<String, Object> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            String name = entry.getKey();
            Object value = entry.getValue();
            dataNodes.put(name, Objects.isNull(value) ? null : createDataNode(name, value));
        }
        return new MySQLDataGroup(dataNodes);
    }

    private DataNode<?> createDataNode(final String name, final Object value) {
        if (value instanceof String) {
            return new StringDataNode(name, value.toString());
        } else if (value instanceof Integer) {
            return new IntegerDataNode(name, Integer.parseInt(value.toString()));
        } else if (value instanceof Long) {
            return new LongDataNode(name, Long.parseLong(value.toString()));
        } else if (value instanceof BigDecimal) {
            return new BigDecimalDataNode(name, new BigDecimal(value.toString()));
        } else if (value instanceof Date) {
            return new LocalDateTimeDataNode(name, DateUtil.swapToLocalDateTime((Date) value));
        } else if (value instanceof LocalDateTime) {
            return new LocalDateTimeDataNode(name, (LocalDateTime) value);
        } else {
            throw new WormholeException("error : create data node failed name:[%s], value:[%s]", name, value);
        }
    }

    @Override
    public Map<String, Object> swapToSource(final DataGroup dataGroup) {
        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, DataNode<?>> dataNodes = dataGroup.getDataNodes();
        Iterator<Map.Entry<String, DataNode<?>>> iterator = dataNodes.entrySet().iterator();
        Map.Entry<String, DataNode<?>> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            result.put(entry.getKey(), entry.getValue().getValue());
        }
        return result;
    }
}