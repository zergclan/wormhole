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

package com.zergclan.wormhole.bus.memory;

import com.google.common.eventbus.EventBus;
import com.zergclan.wormhole.bus.api.Event;
import com.zergclan.wormhole.bus.api.EventListener;

/**
 * Event bus operation.
 */
public final class WormholeEventBus {
    
    private static final EventBus EVENTBUS = EventBusHolder.INSTANCE.eventBus;

    /**
     * Register event listener.
     * You can register multiple.
     *
     * @param listener Listener object.
     * @param <T> Class T extend Event.
     */
    public static <T extends Event> void register(final EventListener<T> listener) {
        EVENTBUS.register(listener);
    }

    /**
     * Unregister event listener.
     * Required and registered as the same object.
     *
     * @param listener Listener object.
     * @param <T> Class T extend Event.
     */
    public static <T extends Event> void unregister(final EventListener<T> listener) {
        EVENTBUS.unregister(listener);
    }

    /**
     * Send message.
     * Listener processing is performed according to the object type,
     * and if there are multiple listener objects processing the same message,
     * they will be executed at the same time.
     *
     * @param event Event message object.
     */
    public static void post(final Event event) {
        EVENTBUS.post(event);
    }

    /**
     * For creating event bus singleton object.
     */
    private enum EventBusHolder {

        INSTANCE("wormhole");

        private final EventBus eventBus;

        EventBusHolder(final String identifier) {
            eventBus = new EventBus(identifier);
        }
    }

}
