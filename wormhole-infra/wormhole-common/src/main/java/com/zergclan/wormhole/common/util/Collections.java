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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Util tools for collection.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Collections {
    
    /**
     * Partition collection.
     *
     * @param collection collection
     * @param size size
     * @param <T> class type of collection element
     * @return partitioned collection
     */
    public static <T> Collection<Collection<T>> partition(final Collection<T> collection, final int size) {
        Validator.notNull(collection, "error : collection partition input collection can not be null");
        Validator.preState(size > 0, "error : collection partition input size [size] less than 0");
        Collection<Collection<T>> result = new LinkedList<>();
        int collectionSize = collection.size();
        if (size >= collectionSize) {
            result.add(collection);
            return result;
        }
        Iterator<T> iterator = collection.iterator();
        Collection<T> sizedEach = new LinkedList<>();
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            sizedEach.add(iterator.next());
            if (size == sizedEach.size() || count == collectionSize) {
                result.add(sizedEach);
                sizedEach = new LinkedList<>();
            }
        }
        return result;
    }
    
    /**
     * Is same collection.
     *
     * @param source source
     * @param target target
     * @param <T> class type of collection
     * @return is same or not
     */
    public static <T> boolean isSame(final Collection<T> source, final Collection<T> target) {
        Validator.notNull(source, "error : collections is same source collection can not be null");
        Validator.notNull(target, "error : collections is same target collection can not be null");
        int sourceSize = source.size();
        int targetSize = target.size();
        if (0 == targetSize && 0 == sourceSize) {
            return true;
        }
        if (targetSize != sourceSize) {
            return false;
        }
        Iterator<T> targetIterator = target.iterator();
        Iterator<T> sourceIterator = source.iterator();
        while (targetIterator.hasNext()) {
            T targetElement = targetIterator.next();
            T sourceElement = sourceIterator.next();
            if (!targetElement.equals(sourceElement)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Has same element.
     *
     * @param source source
     * @param target target
     * @param <T> class type of collection
     * @return Has same element or not
     */
    public static <T> boolean hasSameElement(final Collection<T> source, final Collection<T> target) {
        Validator.notNull(source, "error : collection compare source collection can not be null");
        Validator.notNull(target, "error : collection compare target collection can not be null");
        int sourceSize = source.size();
        int targetSize = target.size();
        if (0 == targetSize && 0 == sourceSize) {
            return true;
        }
        return new LinkedHashSet<>(target).equals(new LinkedHashSet<>(source));
    }
}
