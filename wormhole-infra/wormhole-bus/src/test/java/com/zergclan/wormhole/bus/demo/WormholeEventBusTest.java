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

package com.zergclan.wormhole.bus.demo;

import com.zergclan.wormhole.bus.demo.listener.Event2Listener;
import com.zergclan.wormhole.bus.demo.listener.EventListener;
import com.zergclan.wormhole.bus.demo.message.PlanMessage;
import com.zergclan.wormhole.bus.demo.message.TaskMessage;
import org.junit.jupiter.api.Test;

/**
 * Event bus.
 */
public final class WormholeEventBusTest {

    /**
     * Test event bus.
     */
    @Test
    public void testEventBus() {
        EventListener eventListener = new EventListener();
        WormholeEventBus.register(eventListener);
        WormholeEventBus.post(new PlanMessage("Plan message."));
        WormholeEventBus.post(new TaskMessage("Task message."));
        System.out.println("------------");
        Event2Listener eventListener2 = new Event2Listener();
        WormholeEventBus.register(eventListener2);
        WormholeEventBus.post(new PlanMessage("Plan message."));
        WormholeEventBus.post(new TaskMessage("Task message."));
        System.out.println("------------");
        WormholeEventBus.unregister(eventListener);
        WormholeEventBus.post(new PlanMessage("Plan message."));
        WormholeEventBus.post(new TaskMessage("Task message."));
    }

}
