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

package com.zergclan.wormhole.common.util;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class CollectionsTest {
    
    @Test
    public void assertPartition() {
        Collection<String> collection = new LinkedList<>();
        collection.add("aa");
        collection.add("aa");
        collection.add("bb");
        collection.add("bb");
        collection.add("cc");
        Collection<Collection<String>> actual = Collections.partition(collection, 2);
        assertEquals(3, actual.size());
        int count = 0;
        for (Collection<String> each : actual) {
            if (0 == count) {
                assertEquals(2, each.size());
                Iterator<String> iterator = each.iterator();
                assertEquals("aa", iterator.next());
                assertEquals("aa", iterator.next());
            } else if (1 == count) {
                assertEquals(2, each.size());
                Iterator<String> iterator = each.iterator();
                assertEquals("bb", iterator.next());
                assertEquals("bb", iterator.next());
            } else {
                assertEquals(1, each.size());
                assertEquals("cc", each.iterator().next());
            }
            count++;
        }
    }
    
    @Test
    public void assertIsSame() {
        Collection<String> sourceCollection = new LinkedList<>();
        sourceCollection.add("aaa");
        sourceCollection.add("bbb");
        Collection<String> targetCollection1 = new LinkedList<>();
        targetCollection1.add("aaa");
        targetCollection1.add("bbb");
        assertTrue(Collections.isSame(sourceCollection, targetCollection1));
        Collection<String> targetCollection2 = new LinkedList<>();
        targetCollection2.add("bbb");
        targetCollection2.add("aaa");
        assertFalse(Collections.isSame(sourceCollection, targetCollection2));
        Collection<String> targetCollection3 = new LinkedList<>();
        targetCollection3.add("aaa");
        targetCollection3.add("bbb");
        targetCollection3.add("bbb");
        assertFalse(Collections.isSame(sourceCollection, targetCollection3));
    }
    
    @Test
    public void assertHasSameElement() {
        Collection<String> sourceCollection = new LinkedList<>();
        sourceCollection.add("aaa");
        sourceCollection.add("bbb");
        sourceCollection.add("ccc");
        Collection<String> targetCollection1 = new LinkedList<>();
        targetCollection1.add("aaa");
        targetCollection1.add("bbb");
        targetCollection1.add("ccc");
        assertTrue(Collections.hasSameElement(sourceCollection, targetCollection1));
        Collection<String> targetCollection2 = new LinkedList<>();
        targetCollection2.add("aaa");
        targetCollection2.add("ccc");
        targetCollection2.add("bbb");
        assertTrue(Collections.hasSameElement(sourceCollection, targetCollection2));
        Collection<String> targetCollection3 = new LinkedList<>();
        targetCollection3.add("aaa");
        targetCollection3.add("bbb");
        targetCollection3.add("bbb");
        targetCollection3.add("ccc");
        assertTrue(Collections.hasSameElement(sourceCollection, targetCollection3));
        Collection<String> targetCollection4 = new LinkedList<>();
        targetCollection4.add("aaa");
        targetCollection4.add("bbb");
        targetCollection4.add("ccc");
        targetCollection4.add("ddd");
        assertFalse(Collections.hasSameElement(sourceCollection, targetCollection4));
    }
}
