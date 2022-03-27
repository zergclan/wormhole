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

package com.zergclan.wormhole.bus.demo.listener;

import com.google.common.eventbus.Subscribe;
import com.zergclan.wormhole.bus.demo.message.PlanMessage;
import com.zergclan.wormhole.bus.demo.message.TaskMessage;

/**
 * Event listener.
 */
public final class Event2Listener {

    /**
     * Test handle plan event.
     *
     * @param planMessage Plan message object.
     */
    @Subscribe
    public void planEvent(final PlanMessage planMessage) {
        System.out.println("planEvent2:" + planMessage.getDesc());
    }

    /**
     * Test handle task event.
     *
     * @param taskMessage Task message object.
     */
    @Subscribe
    public void taskEvent(final TaskMessage taskMessage) {
        System.out.println("taskEvent2:" + taskMessage.getDesc());
    }

}
