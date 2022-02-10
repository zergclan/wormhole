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

package com.zergclan.wormhole.writer.xsql.convert;

import com.zergclan.wormhole.common.WormholeException;

import java.sql.Blob;
import java.sql.SQLException;

class ByteArrayConverter extends Converter<byte[]> {
    @Override
    public byte[] convert(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof byte[]) {
            return (byte[]) o;
        }
        if (o instanceof Blob) {
            Blob blob = (Blob) o;
            try {
                return blob.getBytes(1, (int) blob.length());
            } catch (SQLException e) {
                throw new WormholeException("can not trans " + o.getClass().getSimpleName() + " to byte[]");
            }
        }

        throw new WormholeException("can not trans " + o.getClass().getSimpleName() + " to byte[]");
    }
}
