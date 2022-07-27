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

package com.zergclan.wormhole.test.integration.framework.util;

import com.zergclan.wormhole.test.integration.framework.container.DockerContainerDefinition;
import com.zergclan.wormhole.test.integration.framework.container.storage.atomic.MySQLITContainer;
import org.testcontainers.containers.Network;

public final class JDBCExecutor {
    
    public static void main(String[] args) {
        DockerContainerDefinition dockerContainerDefinition = new DockerContainerDefinition("mirror", "MySQL", 3306);
        MySQLITContainer mySQLITContainer = new MySQLITContainer(dockerContainerDefinition);
        mySQLITContainer.setNetwork(Network.newNetwork());
        mySQLITContainer.start();
    
        final String host = mySQLITContainer.getHost();
        final Integer firstMappedPort = mySQLITContainer.getFirstMappedPort();
    
        System.out.println(host);
        System.out.println(firstMappedPort);
    }
}
